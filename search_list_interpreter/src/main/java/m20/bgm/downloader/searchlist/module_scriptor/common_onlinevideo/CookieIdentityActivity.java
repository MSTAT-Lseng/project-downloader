package m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;

import m20.bgm.downloader.R;
import m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo.scriptor.SearchTaskerInterpreter;
import m20.bgm.downloader.utils.UIUtils;

public class CookieIdentityActivity extends Activity {

    private static int $loadpage;
    private static String $module_path, $cookie_varstorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tasker_cookie_identify);

        $module_path = getIntent().getStringExtra("module-path");
        $cookie_varstorage = getIntent().getStringExtra("cookie-varstorage");
        String url = getIntent().getStringExtra("url");

        $loadpage = 0;

        ((Toolbar) findViewById(R.id.main_toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭页面
            }
        });

        AgentWeb agentWeb = AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) findViewById(R.id.agentweb), new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go(url);

        // 深色模式适配
        if (UIUtils.isDarkMode(this)) {
            ((Toolbar) findViewById(R.id.main_toolbar)).setBackgroundColor(getResources().getColor(R.color.blue_600)); // 标题栏
            ((LinearLayout) findViewById(R.id.agentweb)).setBackgroundColor(getResources().getColor(R.color.grey_900));
        }

    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            $loadpage = $loadpage + 1;

            view.evaluateJavascript("document.cookie", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    // get and task cookie str.
                    String taskedValue = value.substring(1, value.length() - 1);

                    if ($loadpage > 1) {
                        // if $loadpage > 1, storage cookie str and finish activity.
                        // next module request data use storage cookie data.
                        SearchTaskerInterpreter.makeCookieData(CookieIdentityActivity.this, $module_path, $cookie_varstorage, taskedValue);

                        Toast.makeText(CookieIdentityActivity.this, "如果已完成安全验证，点击返回键重新搜索即可获得结果。", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    };

}
