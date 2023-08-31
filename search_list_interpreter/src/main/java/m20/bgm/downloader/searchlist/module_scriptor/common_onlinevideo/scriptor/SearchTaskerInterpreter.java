package m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo.scriptor;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import m20.bgm.downloader.Config;
import m20.bgm.downloader.searchlist.module_scriptor.ModuleOpener;
import m20.bgm.downloader.searchlist.module_scriptor.module_opener.ManifestJSONParser;
import m20.bgm.downloader.utils.FileUtils;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SearchTaskerInterpreter {

    public static String TASKER_CHECK_DATA_RESULTS = "javascript:checkErrorResult();";
    public static String TASKER_CHECK_DATA_RESULTS_OK = "SUCCESS";

    public static String TASKER_INTERPRETER_DATA = "javascript:parserResultItem();";
    public static String TASKER_RESULTS_SPLIT = "\\{bgd_items_split\\}";

    public static String TASKER_CHECK_PAGES = "javascript:getPagesParam();";

    public static String TASK_NULL = "ul";

    public static String SEARCH_TASKER_COOKIE_VAR = "search_tasker_cookie";

    // define global variables.
    private String $modulePath, $keyword;
    public ManifestJSONParser $moduleManifest;
    private SearchTaskerConfigJSONParser $taskerConfig;
    private int $moduleType, $indexPage;
    private Activity $activity;

    /**
     * Initialize global variables.
     *
     * @param activity   activity
     * @param modulePath modulePath
     * @param keyword    keyword
     * @param indexPage  indexPage
     */
    public SearchTaskerInterpreter(Activity activity, String modulePath, String keyword, int indexPage) {
        this.$modulePath = modulePath;
        this.$keyword = keyword;
        this.$indexPage = indexPage;
        this.$activity = activity;
        Gson gson = new Gson();
        this.$moduleManifest = gson.fromJson(ModuleOpener.getAssetsStringData(activity, modulePath, "Manifest.json"), ManifestJSONParser.class);
        this.$taskerConfig = gson.fromJson(ModuleOpener.getAssetsStringData(activity, modulePath, "search_tasker_config.json"), SearchTaskerConfigJSONParser.class);
    }

    /**
     * get urlstr
     * */
    public String getUrlStr() {
        return $taskerConfig.getSearch_link() + $taskerConfig.getData_params()
                .replace("{bgd_search_keyword}", URLEncoder.encode($keyword))
                .replace("{bgd_pages}", $indexPage + "");
    }

    /**
     * get request Call object.
     */
    public Call getRequestCallObject() {
        String searchMethod = $taskerConfig.getSearch_method();
        String searchCookie = getCookieData(SEARCH_TASKER_COOKIE_VAR).replace("\n", "");

        Log.e("cookie", searchCookie);

        if (searchMethod.equals("GET")) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout($taskerConfig.getTimeout_seconds(), TimeUnit.SECONDS)
                    .writeTimeout($taskerConfig.getTimeout_seconds(), TimeUnit.SECONDS)
                    .readTimeout($taskerConfig.getTimeout_seconds() + 10, TimeUnit.SECONDS) // set timeout
                    .build();
            final Request request = new Request.Builder()
                    .url(getUrlStr())
                    .removeHeader("User-Agent").addHeader("User-Agent", Config.commonUA)
                    .header("Cookie", searchCookie)
                    .build();

            Call call = okHttpClient.newCall(request);
            return call;
        }
        return null;
    }

    /**
     * get tasker cache files.
     * return cache file url.
     *
     * @param data html_data
     */
    public String getCacheFile(String data) {
        // get IDE script.
        String ideScript = ModuleOpener.getAssetsStringData_freePath($activity, "module_scriptor/search_tasker.js");

        // get task script.
        String taskScript = ModuleOpener.getAssetsStringData($activity, $modulePath, "search_tasker.js");
        // make cache html.
        String taskData = data.replace("\r\n", "{bgd_br}").replace("\n", "{bgd_br}").replace("\"", "\\\"").replace("script", "{bgd_script}");
        String moduleCacheStr = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<script>\nvar moduleProtocol = \"" + $moduleManifest.getConfig().getProtocol() + "\";\nvar moduleDomain = \"" + $moduleManifest.getConfig().getDomain() + "\";\n\n" + ideScript + "\n\nvar data = \"" + taskData + "\";\n\n" + taskScript + "\n</script>\n</body>\n</html>";
        // delete exist cache files.
        FileUtils.clearCacheFile($activity, "module_scriptor_cache/runner.htm", FileUtils.getCacheDirectory($activity, ""));
        // write cache html.
        FileUtils.writeDataToFile(moduleCacheStr, FileUtils.getCacheDirectory($activity, ""), "module_scriptor_cache/runner.htm");

        return "file://" + FileUtils.getCacheDirectory($activity, "") + "module_scriptor_cache/runner.htm";
    }

    /**
     * storage cookie data.
     * */
    public static void makeCookieData(Activity activity, String moduleName, String cookieVar, String data) {
        // storage cookie.
        // delete exist cookie files.
        FileUtils.clearCacheFile(activity, "module_cookie_cache/" + moduleName + "/" + cookieVar + ".txt", FileUtils.getCacheDirectory(activity, ""));
        // write cache html.
        FileUtils.writeDataToFile(data, FileUtils.getCacheDirectory(activity, ""), "module_cookie_cache/" + moduleName + "/" + cookieVar + ".txt");
    }
    /**
     * get cookie data.
     * */
    public String getCookieData(String cookieVar) {
        if (FileUtils.FileExists(FileUtils.getCacheDirectory($activity, "") + "module_cookie_cache/" + $modulePath + "/" + cookieVar + ".txt")) {
            return FileUtils.getFileContent(new File(FileUtils.getCacheDirectory($activity, "") + "module_cookie_cache/" + $modulePath + "/" + cookieVar + ".txt"));
        } else {
            return "";
        }
    }

    public static String[] interpreterItemData(String value) {
        // order must be name image link info.
        return new String[]{
                value.substring(value.indexOf("{bgd_items_name}") + "{bgd_items_name}".length(), value.indexOf("{bgd_items_image}")), // name
                value.substring(value.indexOf("{bgd_items_image}") + "{bgd_items_image}".length(), value.indexOf("{bgd_items_link}")),  // image
                value.substring(value.indexOf("{bgd_items_link}") + "{bgd_items_link}".length(), value.indexOf("{bgd_items_info}")), // link
                // Details Do extra handling for line breaks.
                value.substring(value.indexOf("{bgd_items_info}") + "{bgd_items_info}".length()).replace("\\n", "").replace("{bgd_br}", "\n").replace("{bgd_script}", "script") /* info */};
    }

    /**
     * get module manifest parser.
     */
    public ManifestJSONParser getModuleManifest() {
        return $moduleManifest;
    }

    /**
     * get tasker config parser.
     */
    public SearchTaskerConfigJSONParser getTaskerConfig() {
        return $taskerConfig;
    }
}
