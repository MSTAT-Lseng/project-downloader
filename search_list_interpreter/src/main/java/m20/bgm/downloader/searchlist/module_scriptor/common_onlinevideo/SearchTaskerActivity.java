package m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;

import org.xutils.x;

import java.io.IOException;

import m20.bgm.downloader.R;
import m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo.scriptor.SearchTaskerInterpreter;
import m20.bgm.downloader.utils.IntentUtils;
import m20.bgm.downloader.utils.UIUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchTaskerActivity extends Activity {

    private static int $modulePage, $indexPage;
    private static String $modulePath, $keyword, $manifestDomain, $manifestProtocol;
    private static boolean $moduleEnabledPagesMode;
    private SearchTaskerInterpreter $searchTaskerInterpreter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        $indexPage = getIntent().getIntExtra("page", 1);
        $modulePath = getIntent().getStringExtra("path");
        $keyword = getIntent().getStringExtra("keyword");

        $searchTaskerInterpreter = new SearchTaskerInterpreter(this, $modulePath, $keyword, $indexPage);

        SearchTaskerInterpreter searchTaskerInterpreter = $searchTaskerInterpreter;
        $modulePage = searchTaskerInterpreter.getModuleManifest().getConfig().getPages();
        $moduleEnabledPagesMode = searchTaskerInterpreter.getTaskerConfig().getEnabled_pages_mode();
        $manifestDomain = searchTaskerInterpreter.getModuleManifest().getConfig().getDomain();
        $manifestProtocol = searchTaskerInterpreter.getModuleManifest().getConfig().getProtocol();

        // initialization page
        setContentView(R.layout.activity_common_onlinevideo_searchresult_shower);
        ((Toolbar) findViewById(R.id.main_toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close activity.
            }
        }); // Toolbar back button settings.
        ((Toolbar) findViewById(R.id.main_toolbar)).setTitle(searchTaskerInterpreter.getModuleManifest().getInfo().getName()); // UI interface set module name.

        Call call = searchTaskerInterpreter.getRequestCallObject();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SearchTaskerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.search_loading)).setText("加载失败：" + e.toString()); // UI interface set loading failed hint.
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                SearchTaskerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AgentWeb agentWeb = AgentWeb.with(SearchTaskerActivity.this)
                                .setAgentWebParent((LinearLayout) findViewById(R.id.view_agentweb), new LinearLayout.LayoutParams(-1, -1))
                                .useDefaultIndicator()
                                .setWebViewClient(mWebViewClient)
                                .createAgentWeb()
                                .ready()
                                .go(searchTaskerInterpreter.getCacheFile(data));

                    }
                });

            }
        });

        // dark mode.
        if (UIUtils.isDarkMode(this)) {
            ((LinearLayout) findViewById(R.id.main_frame)).setBackgroundColor(getResources().getColor(R.color.grey_900)); // background color
            ((Toolbar) findViewById(R.id.main_toolbar)).setBackgroundColor(getResources().getColor(R.color.blue_600)); // Toolbar

            ((TextView) findViewById(R.id.search_loading)).setTextColor(getResources().getColor(R.color.grey_400));
        }

    }


    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            // step 1: data check.
            view.evaluateJavascript(SearchTaskerInterpreter.TASKER_CHECK_DATA_RESULTS, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    runInterpreter(SearchTaskerActivity.this, value, view);
                }
            });

        }
    };

    private void runInterpreter(Activity activity, String value, WebView view) {
        String taskValue = value.substring(1, value.length() - 1);
        if (taskValue.equals(SearchTaskerInterpreter.TASKER_CHECK_DATA_RESULTS_OK)) {
            runInterpreterStep2(activity, view);
        } else {
            // display error information.
            if (taskValue.equals(SearchTaskerInterpreter.TASK_NULL)) {
                ((TextView) activity.findViewById(R.id.search_loading)).setText("在处理数据时发生错误：检查数据失败");
            } else if (taskValue.equals("require-identify")) {
                // require cookie identify
                Toast.makeText(activity, "需要进行安全验证后重新搜索", Toast.LENGTH_SHORT).show();
                activity.startActivity(new Intent(activity, CookieIdentityActivity.class).putExtra("module-path", $modulePath).putExtra("cookie-varstorage", SearchTaskerInterpreter.SEARCH_TASKER_COOKIE_VAR).putExtra("url", $searchTaskerInterpreter.getUrlStr()));
                finish();
            } else {
                ((TextView) activity.findViewById(R.id.search_loading)).setText(taskValue);
            }
        }
    }

    private static void runInterpreterStep2(Activity activity, WebView view) {
        // step 2. task data.
        view.evaluateJavascript(SearchTaskerInterpreter.TASKER_INTERPRETER_DATA, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                String taskValue = value.substring(1, value.length() - 1);

                if (taskValue.equals(SearchTaskerInterpreter.TASK_NULL)) {
                    ((TextView) activity.findViewById(R.id.search_loading)).setText("在处理数据时发生错误：处理结果失败");
                } else {
                    // visible shower and hide loading hint.
                    ((TextView) activity.findViewById(R.id.search_loading)).setVisibility(View.GONE);
                    ((LinearLayout) activity.findViewById(R.id.search_result)).setVisibility(View.VISIBLE);

                    String[] _result = taskValue.split(SearchTaskerInterpreter.TASKER_RESULTS_SPLIT);
                    for (int i = 0; i < _result.length; i++) {
                        // Set exception capture for a single parsing result to prevent an error from affecting the display of the entire list.
                        try {
                            String[] result = SearchTaskerInterpreter.interpreterItemData(_result[i]);
                            runInterpreterStep2(activity, result);
                        } catch (Exception ignore) {
                        }
                    }

                    runInterpreterStep3(activity, view);
                }

            }
        });
    }

    private static void runInterpreterStep2(Activity activity, String[] result) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View result_view = layoutInflater.inflate(R.layout.common_image_title_subtitle_list_widget, null); // load inflater.
        result_view.setLayoutParams(layoutParams);
        ((TextView) result_view.findViewById(R.id.common_itst_list_title)).setText(result[0]); // set item name
        x.image().bind(((ImageView) result_view.findViewById(R.id.common_itst_list_image)), result[1]); // set item image
        if (UIUtils.isDarkMode(activity)) {
            ((LinearLayout) result_view.findViewById(R.id.main_hr_1)).setBackgroundColor(activity.getResources().getColor(R.color.grey_800));
            ((TextView) result_view.findViewById(R.id.common_itst_list_title)).setTextColor(activity.getResources().getColor(R.color.grey_400));
            ((TextView) result_view.findViewById(R.id.common_itst_list_subtitle)).setTextColor(activity.getResources().getColor(R.color.grey_600));
        }
        ((TextView) result_view.findViewById(R.id.common_itst_list_subtitle)).setText(result[3]); // load item info
        ((LinearLayout) result_view.findViewById(R.id.common_image_title_subtitle_list_widget)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                IntentUtils.openUrl(result[2], activity); // 浏览器打开番剧条目
                return true;
            }
        });
        ((LinearLayout) result_view.findViewById(R.id.common_image_title_subtitle_list_widget)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取模块页数
                if ($modulePage == 3) {
                    activity.startActivity(new Intent(activity, DetailsBrowseActivity.class)
                            .putExtra("info_link", result[2])
                            .putExtra("item_name", result[0])
                            .putExtra("item_image", result[1])
                            .putExtra("item_introduce", result[3])
                            .putExtra("path", $modulePath)
                            .putExtra("domain", $manifestDomain)
                            .putExtra("protocol", $manifestProtocol));
                } else if ($modulePage == 2) {
                    // 只有两页，直接跳转到 View 界面
                    activity.startActivity(new Intent(activity, ContentPlayerActivity.class)
                            .putExtra("play_url", result[2])
                            .putExtra("path", $modulePath)
                            .putExtra("two_page", 1));
                }
            }
        });

        ((LinearLayout) activity.findViewById(R.id.search_result)).addView(result_view); // 添加 View
    }

    private static void runInterpreterStep3(Activity activity, WebView webView) {
        // 第三步，匹配页数信息
        TextView endPageTextView = new TextView(activity);
        endPageTextView.setText("已经到底啦~");
        endPageTextView.setPadding(40, 40, 40, 40);
        if (UIUtils.isDarkMode(activity)) {
            endPageTextView.setTextColor(activity.getResources().getColor(R.color.grey_500));
        }
        endPageTextView.setGravity(Gravity.CENTER);

        if ($moduleEnabledPagesMode) {
            // 启用页数匹配
            // 第二步，进行结果解析
            webView.evaluateJavascript(SearchTaskerInterpreter.TASKER_CHECK_PAGES, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    String taskValue = value.substring(1, value.length() - 1);

                    if (taskValue.equals(SearchTaskerInterpreter.TASK_NULL)) {
                        Toast.makeText(activity, "加载页数信息出现错误", Toast.LENGTH_SHORT).show();
                    } else {
                        runInterpreterStep3(activity, taskValue, endPageTextView);
                    }
                }
            });
        } else {
            // 未启用页数匹配
            ((LinearLayout) activity.findViewById(R.id.search_result)).addView(endPageTextView);
        }
    }

    private static void runInterpreterStep3(Activity activity, String value, TextView endPageTextView) {
        int totalPage = Integer.parseInt(value);
        if (totalPage == $indexPage) {
            // 最后一页
            ((LinearLayout) activity.findViewById(R.id.search_result)).addView(endPageTextView);
        } else {
            // 不是最后一页
            TextView textView = new TextView(activity);
            textView.setPadding(40, 40, 40, 0);
            textView.setText("页数：" + $indexPage + " / " + totalPage + " 页");
            if (UIUtils.isDarkMode(activity)) {
                textView.setTextColor(activity.getResources().getColor(R.color.grey_500));
            }
            textView.setGravity(Gravity.CENTER);

            LinearLayout linearLayout = new LinearLayout(activity);
            linearLayout.setPadding(40, 10, 40, 60);
            linearLayout.setGravity(Gravity.CENTER);

            Button button = new Button(activity);
            button.setText("下一页");
            if (UIUtils.isDarkMode(activity)) {
                button.setBackgroundColor(activity.getResources().getColor(R.color.grey_800));
                button.setTextColor(activity.getResources().getColor(R.color.grey_400));
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, SearchTaskerActivity.class).putExtra("path", $modulePath).putExtra("keyword", $keyword).putExtra("page", $indexPage + 1).putExtra("module_type", "assets"));
                    activity.finish(); // 关闭当前页
                }
            });

            linearLayout.addView(button);

            ((LinearLayout) activity.findViewById(R.id.search_result)).addView(textView);
            ((LinearLayout) activity.findViewById(R.id.search_result)).addView(linearLayout);
        }
    }

}
