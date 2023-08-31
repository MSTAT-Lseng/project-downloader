package m20.bgm.downloader.searchlist.module_scriptor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo.SearchTaskerActivity;
import m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo.scriptor.SearchTaskerInterpreter;
import m20.bgm.downloader.searchlist.module_scriptor.module_opener.ManifestJSONParser;

public class ModuleOpener {

    public static final int PARSER_VERSION = 1; // 当前解析器版本

    public static final int SUCCESSFUL = 0x00001; // 解析成功

    public static final int ERROR_MODULE_PARSER_VERSION_TOO_LOW = 0x10001; // 错误：当前解析器版本过低

    /* 打开 Assets 内置搜索源模块
     * 传参 Activity，模块名称，搜索关键词
     * 返回响应代码，处理正确或错误。*/
    public static int openAssetsModule(Activity activity, String module_name, String keyword) {
        String manifestJson = getAssetsStringData(activity, module_name, "Manifest.json");

        // 解析 Manifest
        Gson gson = new Gson();
        ManifestJSONParser manifestJSONParsers = gson.fromJson(manifestJson, ManifestJSONParser.class); // 解析 JSON

        // 判断解析器版本是否过低
        if (manifestJSONParsers.getMin_version() > PARSER_VERSION) {
            return ERROR_MODULE_PARSER_VERSION_TOO_LOW; // 版本过低，返回错误并停止执行
        }

        // 版本正常，继续进行解析
        String moduleType = manifestJSONParsers.getConfig().getType(); // 获取模块类型
        int modulePageTag = manifestJSONParsers.getConfig().getDefault_page_tag(); // 获取默认模块页面下标

        if (moduleType.equals("common-onlinevideo")) {
            activity.startActivity(new Intent(activity, SearchTaskerActivity.class).putExtra("page", modulePageTag).putExtra("keyword", keyword).putExtra("path", module_name));
        }

        return SUCCESSFUL;
    }

    // 模块路径
    public static String getAssetsStringData(Activity activity, String module_name, String module_resources) {
        return getAssetsStringData_freePath(activity, "search_list_modules/" + module_name + "/" + module_resources);
    }

    // 自由路径
    public static String getAssetsStringData_freePath(Activity activity, String path) {
        //通过设备管理对象 获取Asset的资源路径
        AssetManager assetManager = activity.getApplicationContext().getAssets();

        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        StringBuffer sb = new StringBuffer();
        try {
            inputStream = assetManager.open(path);
            isr = new InputStreamReader(inputStream);
            br = new BufferedReader(isr);

            sb.append(br.readLine());
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append("\n" + line);
            }

            br.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
