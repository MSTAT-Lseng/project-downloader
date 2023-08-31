package m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import m20.bgm.downloader.Config;
import m20.bgm.downloader.R;
import m20.bgm.downloader.searchlist.module_scriptor.ModuleOpener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ContentPlayerActivity extends AppCompatActivity {

    private static String $path, $videoRecord, $playUrl, $taskedHTML;
    private static int $onlyTwoPages;
    public static boolean $trackerHint = false, $isFirstJsLoad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_onlinevideo_contentplayer);
        setSupportActionBar(findViewById(R.id.main_toolbar));
        ((Toolbar) findViewById(R.id.main_toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭页面
            }
        }); // 标题栏返回按钮设置

        $videoRecord = "";
        $taskedHTML = "";
        $isFirstJsLoad = false;
        $playUrl = getIntent().getStringExtra("play_url"); // 获取 URL
        $path = getIntent().getStringExtra("path"); // 获取模块名称
        $onlyTwoPages = getIntent().getIntExtra("two_page", 0); // 是否只有两个页面

        AgentWeb agentWeb = AgentWeb.with(ContentPlayerActivity.this)
                .setAgentWebParent((LinearLayout) findViewById(R.id.view_agentweb), new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go("about:blank");

        // 开启调试，可选择关闭
        // 调试教程可前往 https://www.jianshu.com/p/f3606a1a1c87 查看。
        agentWeb.getWebCreator().getWebView().setWebContentsDebuggingEnabled(true);

        agentWeb.getUrlLoader().loadData("<span style=\"display: block; padding: 5px; font-size: 14px\">加载中...</span>", "text/html", "UTF-8");

        // 加载数据
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url($playUrl).removeHeader("User-Agent").addHeader("User-Agent", Config.commonMobileUA)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ContentPlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // loadData 需要 '#', '%', '\', '?' 转换 %23, %25, %27, %3f ，loadDataWithBaseURL 不需要
                        agentWeb.getUrlLoader().loadData("<span style=\"display: block; padding: 5px; font-size: 14px\">加载失败：" + e.toString().replace("#", "%23").replace("%", "%25").replace("\\", "%27").replace("?", "%3f") + "</span>", "text/html", "UTF-8");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String data = response.body().string(); // 获取的数据
                ContentPlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadResponse(ContentPlayerActivity.this, data, agentWeb);
                    }
                });
            }
        });

        ((Toolbar) findViewById(R.id.main_toolbar)).setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 视频抓取跳转
                if ($videoRecord.equals("")) {
                    Toast.makeText(ContentPlayerActivity.this, "暂未抓取到视频资源", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(ContentPlayerActivity.this, ResourceTrackerActivity.class).putExtra("tracker_data", $videoRecord));
                }
                return true;
            }
        });

    }

    private static void loadResponse(Activity activity, String data, AgentWeb agentWeb) {
        // 获取匹配脚本
        String searchTasker = ModuleOpener.getAssetsStringData(activity, $path, "player_scriptor.js");
        // 获取环境脚本
        String moduleScriptor = ModuleOpener.getAssetsStringData_freePath(activity, "module_scriptor/player_scriptor_api.js");
        $taskedHTML = data + "<script>" + moduleScriptor + searchTasker + "</script>";
        agentWeb.getUrlLoader().loadUrl($playUrl);
    }

    private WebViewClient mWebViewClient = new WebViewClient() {

        // 待完善内容：限制页面跳转次数，防止跳转到其他页面。（非只有两个页面组成的搜索源模块）

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            // 只有两页组成的搜索源模块，额外拓展代码注入
            // 拦截视频资源
            String tasked_link = url;
            tasked_link = taskURL(tasked_link);

            // 如果只有两个页面，则需要拓宽一下可以注入代码的页面，匹配的是去掉了get参数和锚点的页面后缀名，如.html，.php等。
            boolean twoPagesExtend = false;
            if ($onlyTwoPages == 1) {
                String extended = ModuleOpener.getAssetsStringData(ContentPlayerActivity.this, $path, "script_extra_extended.txt");
                if (!extended.equals("disable")) {
                    String[] format = extended.split(",");
                    for (String s : format) {
                        if (tasked_link.endsWith(s)) {
                            twoPagesExtend = true;
                            break;
                        }
                    }
                }
            }

            if (twoPagesExtend) {
                view.evaluateJavascript(ModuleOpener.getAssetsStringData(ContentPlayerActivity.this, $path, "player_scriptor_extra.js"), new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {

                    }
                });
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

            // 拦截视频资源
            String tasked_link = request.getUrl().toString();
            tasked_link = taskURL(tasked_link);

            String mediaTracker = "mp4,flv,f4v,webm,m4v,mov,rmvb,wmv,avi,mpeg,ts,mkv,flc,m3u8";
            String[] mediaTrackerList = mediaTracker.split(",");

            for (int i = 0; i < mediaTrackerList.length; i++) {
                if (tasked_link.endsWith("." + mediaTrackerList[i])) {
                    $videoRecord = $videoRecord + request.getUrl().toString() + "{bgd_mime_type}" + mediaTrackerList[i] + "{bgd_res_time}" + new Date() + "{bgd_tracker_split}";
                    if (!$trackerHint) {
                        ContentPlayerActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ContentPlayerActivity.this, "截取到视频资源，点击右上角箭头图标查看详细链接", Toast.LENGTH_LONG).show();
                            }
                        });
                        $trackerHint = true;
                    }
                }
            }

            // 匹配目标注入的网址链接
            // 由于这些搜索源的域名会经常变化，暂不对域名进行完全匹配。
            String vdo_tasked_link = $playUrl.replaceFirst("//", "");
            vdo_tasked_link = vdo_tasked_link.substring(vdo_tasked_link.indexOf("/"));
            if (request.getUrl().toString().contains(vdo_tasked_link) && !$isFirstJsLoad) {
                // 加载含有自定义注入代码的HTML
                // 返回处理过的 WebResourceResponse
                Log.i("load_tasked_html", "OKAY");
                $isFirstJsLoad = true;
                return new WebResourceResponse("text/html", "UTF-8", new ByteArrayInputStream($taskedHTML.getBytes()));
            }

            return super.shouldInterceptRequest(view, request);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_onlinevideo_player_dump_video, menu);
        return true;
    }

    private static String taskURL(String url) {
        String tasked_link = url;
        if (tasked_link.contains("#")) {
            tasked_link = tasked_link.split("#")[0]; // 切掉锚点；
        }
        if (tasked_link.contains("?")) {
            tasked_link = tasked_link.split("\\?")[0]; // 切掉get；
        }
        return tasked_link;
    }

}
