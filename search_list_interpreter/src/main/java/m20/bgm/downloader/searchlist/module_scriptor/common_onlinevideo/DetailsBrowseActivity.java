package m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.IOException;

import m20.bgm.downloader.Config;
import m20.bgm.downloader.R;
import m20.bgm.downloader.searchlist.module_scriptor.ModuleOpener;
import m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo.scriptor.SearchTaskerInterpreter;
import m20.bgm.downloader.utils.FileUtils;
import m20.bgm.downloader.utils.IntentUtils;
import m20.bgm.downloader.utils.UIUtils;
import m20.bgm.downloader.utils.WaitingWatchUtils;
import m20.bgm.downloader.utils.WidgetUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsBrowseActivity extends Activity {

    private static String $modulePath, $configProtocol, $configDomain, $itemIntroduce, $itemName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_onlinevideo_details_browse);
        ((Toolbar) findViewById(R.id.main_toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭页面
            }
        }); // 标题栏关闭按钮设置

        // 传参获取
        $modulePath = getIntent().getStringExtra("path");
        $configProtocol = getIntent().getStringExtra("protocol");
        $configDomain = getIntent().getStringExtra("domain");
        $itemName = getIntent().getStringExtra("item_name");
        $itemIntroduce = getIntent().getStringExtra("item_introduce");
        String itemImage = getIntent().getStringExtra("item_image");
        String infoLink = getIntent().getStringExtra("info_link");

        ((TextView) findViewById(R.id.details_bgmtitle)).setText($itemName); // 设置名称

        // 设置番剧图像
        x.image().bind(((ImageView) findViewById(R.id.details_bgmimage)), itemImage, new ImageOptions.Builder().setRadius(DensityUtil.dip2px(5)).build()); // 设置番剧图像
        OkHttpClient imageHttpClient = new OkHttpClient();
        final Request imageRequest = new Request.Builder()
                .url(itemImage)
                .build();
        Call imageCall = imageHttpClient.newCall(imageRequest);
        imageCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("image_load", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] imageData = response.body().bytes(); // 图像 byte
                DetailsBrowseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (UIUtils.isDarkMode(DetailsBrowseActivity.this)) {
                            WidgetUtils.setBgmInfoBackgroundDark(imageData, (LinearLayout) findViewById(R.id.details_bgminfo_frame));
                        } else {
                            WidgetUtils.setBgmInfoBackground(imageData, (LinearLayout) findViewById(R.id.details_bgminfo_frame));
                        }
                    }
                });
            }
        });

        // 加载数据
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(infoLink).removeHeader("User-Agent").addHeader("User-Agent", Config.commonUA)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                DetailsBrowseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.details_loading)).setText("加载失败：" + e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String data = response.body().string(); // 获取的数据
                DetailsBrowseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        configResponse(DetailsBrowseActivity.this, data, mWebViewClient);
                    }
                });
            }
        });

        // 深色模式适配
        if (UIUtils.isDarkMode(this)) {
            ((LinearLayout) findViewById(R.id.main_frame)).setBackgroundColor(getResources().getColor(R.color.grey_900)); // 背景颜色
            ((Toolbar) findViewById(R.id.main_toolbar)).setBackgroundColor(getResources().getColor(R.color.blue_600)); // 标题栏

            ((TextView) findViewById(R.id.details_loading)).setTextColor(getResources().getColor(R.color.grey_400));
            ((TextView) findViewById(R.id.bgmintro)).setTextColor(getResources().getColor(R.color.grey_400));
            ((TextView) findViewById(R.id.details_introduce_content)).setTextColor(getResources().getColor(R.color.grey_600));
            ((TextView) findViewById(R.id.details_bgmtitle)).setTextColor(getResources().getColor(R.color.grey_200));
            ((TextView) findViewById(R.id.details_bgminfo)).setTextColor(getResources().getColor(R.color.grey_300));

        }
    }

    private static void configResponse(Activity activity, String data, WebViewClient webViewClient) {
        ((TextView) activity.findViewById(R.id.details_loading)).setVisibility(View.GONE); // 隐藏加载提示
        ((LinearLayout) activity.findViewById(R.id.details_bgminfo_frame)).setVisibility(View.VISIBLE); // 显示番剧信息
        ((LinearLayout) activity.findViewById(R.id.details_playlist_frame)).setVisibility(View.VISIBLE); // 显示剧集列表

        // 写入缓存配置
        // 获取匹配脚本
        String searchTasker = ModuleOpener.getAssetsStringData(activity, $modulePath, "browse_parser.js");
        // 获取环境脚本
        String moduleScriptor = ModuleOpener.getAssetsStringData_freePath(activity, "module_scriptor/search_tasker.js");
        // 制作HTML字段
        String tasked_data = data.replace("\r\n", "{bgd_br}").replace("\n", "{bgd_br}").replace("\"", "\\\"").replace("script", "{bgd_script}");
        String moduleCacheStr = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<script>\nvar moduleProtocol = \"" + $configProtocol + "\";\nvar moduleDomain = \"" + $configDomain + "\";\n\n" + moduleScriptor + "\n\nvar data = \"" + tasked_data + "\";\n\n" + searchTasker + "\n</script>\n</body>\n</html>";
        // 先删除以前的缓存文件
        FileUtils.clearCacheFile(activity, "module_scriptor_cache/browse_runner.htm", FileUtils.getCacheDirectory(activity, ""));
        // 写入模板 HTML
        FileUtils.writeDataToFile(moduleCacheStr, FileUtils.getCacheDirectory(activity, ""), "module_scriptor_cache/browse_runner.htm");
        // 运行模板
        AgentWeb agentWeb = AgentWeb.with(activity)
                .setAgentWebParent((LinearLayout) activity.findViewById(R.id.view_agentweb), new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebViewClient(webViewClient)
                .createAgentWeb()
                .ready()
                .go("file://" + FileUtils.getCacheDirectory(activity, "") + "module_scriptor_cache/browse_runner.htm");
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            // 获取简介信息
            view.evaluateJavascript("javascript:getIntroductionInfo();", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    runInterpreterStep(DetailsBrowseActivity.this, value, view);
                }
            });

        }
    };

    private static void runInterpreterStep(Activity activity, String value, WebView view) {
        String value_tasked = value.substring(1, value.length() - 1).replace("\\n", "\n").replace("{bgd_script}", "script");
        if (value_tasked.equals("STRING_EXTRA")) {
            // 使用传参过来的信息
            ((TextView) activity.findViewById(R.id.details_bgminfo)).setText($itemIntroduce); // 设置简介信息
        } else if (value_tasked.equals("NONE")) {
            // 不显示番剧简介信息
        } else if (value_tasked.equals(SearchTaskerInterpreter.TASK_NULL)) {
            ((TextView) activity.findViewById(R.id.details_bgminfo)).setText("简介信息加载错误：返回值异常");
        } else {
            ((TextView) activity.findViewById(R.id.details_bgminfo)).setText(value_tasked); // 设置简介信息
        }

        runInterpreterStep2(activity, view);
    }

    private static void runInterpreterStep2(Activity activity, WebView view) {
        // 获取介绍信息
        view.evaluateJavascript("javascript:getIntroduceInfo();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

                String value_tasked = value.substring(1, value.length() - 1).replace("\\n", "\n").replace("{bgd_script}", "script").replace("{bgd_br}", "\n");
                if (!value_tasked.equals("NONE")) {
                    if (value_tasked.equals(SearchTaskerInterpreter.TASK_NULL)) {
                        ((TextView) activity.findViewById(R.id.details_introduce_content)).setText("番剧介绍加载错误：返回值异常"); // 设置文本
                    } else {
                        ((TextView) activity.findViewById(R.id.details_introduce_content)).setText(value_tasked); // 设置文本
                    }
                    ((LinearLayout) activity.findViewById(R.id.details_introduce)).setVisibility(View.VISIBLE);
                }

                runInterpreterStep3(activity, view);

            }
        });
    }

    private static void runInterpreterStep3(Activity activity, WebView view) {
        // 进行结果解析
        view.evaluateJavascript("javascript:parserBrowseData();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

                String value_tasked = value.substring(1, value.length() - 1);
                if (value_tasked.endsWith("{STR_DATA_CHECKED}")) {
                    runInterpreterStep3(activity, value_tasked, view);
                } else {
                    Toast.makeText(activity, "解析资源列表时出现错误，可能是暂无片源。", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private static void runInterpreterStep3(Activity activity, String value_tasked, WebView view) {
        String[] channel_tasked = value_tasked.split("\\{bgd_item_split\\}");

        for (int i = 0; i < channel_tasked.length - 1; i++) {
            // 针对单条解析结果设置异常捕获，防止一条错误影响到整个列表的显示。
            try {
                String channelData = channel_tasked[i];

                String channelName = channelData.substring(0, channelData.indexOf("{bgd_name_tag}"));
                TextView textView = new TextView(activity);
                textView.setText(channelName);
                if (UIUtils.isDarkMode(activity)) {
                    textView.setTextColor(activity.getResources().getColor(R.color.grey_400));
                } else {
                    textView.setTextColor(activity.getResources().getColor(R.color.grey_800));
                }
                textView.setTextSize(15);
                textView.setPadding(0, 0, 0, 15);

                ((LinearLayout) activity.findViewById(R.id.details_playlist_frame)).addView(textView);

                FlexboxLayout flexboxLayout = new FlexboxLayout(activity);
                flexboxLayout.setPadding(0, 0, 0, 35);
                flexboxLayout.setFlexWrap(FlexWrap.WRAP);

                String[] tasked_itemlist = channelData.split("\\{bgd_name_tag\\}");
                for (int i2 = 1; i2 < tasked_itemlist.length; i2++) {
                    String itemlistData = tasked_itemlist[i2];

                    String itemName = itemlistData.substring(0, itemlistData.indexOf("{bgd_link_tag}"));
                    String itemLink = itemlistData.substring(itemlistData.indexOf("{bgd_link_tag}") + "{bgd_link_tag}".length());

                    Button button = new Button(activity);
                    button.setText(itemName);
                    if (UIUtils.isDarkMode(activity)) {
                        button.setBackgroundColor(activity.getResources().getColor(R.color.grey_900));
                        button.setTextColor(activity.getResources().getColor(R.color.grey_400));
                    }
                    button.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            runInterpreterWaitingWatch(activity, view, itemLink, button);
                            return true;
                        }
                    });
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.startActivity(new Intent(activity, ContentPlayerActivity.class)
                                    .putExtra("play_url", itemLink)
                                    .putExtra("path", $modulePath)); // 打开剧集页面
                        }
                    });

                    flexboxLayout.addView(button);

                }
                ((LinearLayout) activity.findViewById(R.id.details_playlist_frame)).addView(flexboxLayout);
            } catch (Exception ignore) {
            }
        }
    }

    private static void runInterpreterWaitingWatch(Activity activity, WebView view, String itemLink, Button button) {

        // 稍后再看功能适配
        view.evaluateJavascript("javascript:waitingWatchSupport();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (!value.substring(1, value.length() - 1).equals("NONE")) {
                    final String[] longClickSelection = {"在浏览器中打开", "添加到稍后再看"};
                    AlertDialog.Builder items = new AlertDialog.Builder(activity);
                    items.setTitle("选择操作");
                    items.setItems(longClickSelection, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (longClickSelection[which].toString().equals("在浏览器中打开")) {
                                IntentUtils.openUrl(itemLink, activity); // 用浏览器打开
                            } else if (longClickSelection[which].toString().equals("添加到稍后再看")) {
                                //添加到稍后再看
                                double id_random = Math.random();
                                String pkgdata = new WaitingWatchUtils().createWWCFormat(id_random, value.substring(1, value.length() - 1), $itemName + " " + button.getText(), itemLink);
                                new WaitingWatchUtils().insertKeyword(pkgdata, activity);
                                Toast.makeText(activity, "添加到稍后再看成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    items.create().show();
                } else {
                    // 暂未适配稍后再看
                    IntentUtils.openUrl(itemLink, activity);
                }
            }
        });

    }

}
