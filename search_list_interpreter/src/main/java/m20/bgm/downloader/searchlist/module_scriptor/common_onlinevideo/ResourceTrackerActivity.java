package m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import m20.bgm.downloader.BrowserActivity;
import m20.bgm.downloader.R;
import m20.bgm.downloader.utils.IntentUtils;
import m20.bgm.downloader.utils.UIUtils;

public class ResourceTrackerActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_onlinevideo_resource_tracker);
        ((Toolbar) findViewById(R.id.main_toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭页面
            }
        }); // 标题栏返回按钮设置

        String trackerData = getIntent().getStringExtra("tracker_data");

        String[] dataList = trackerData.split("\\{bgd_tracker_split\\}");
        for (int i = dataList.length - 1; i > -1 ; i--) {
            String listData = dataList[i];
            String link = listData.substring(0, listData.indexOf("{bgd_mime_type}"));
            String mimeType = listData.substring(listData.indexOf("{bgd_mime_type}") + "{bgd_mime_type}".length(), listData.indexOf("{bgd_res_time}"));
            String dataTime = listData.substring(listData.indexOf("{bgd_res_time}") + "{bgd_res_time}".length());

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LayoutInflater layoutInflater = LayoutInflater.from(ResourceTrackerActivity.this);
            View result_view = layoutInflater.inflate(R.layout.common_onlinevideo_restracker_item, null); // 加载条目布局
            result_view.setLayoutParams(layoutParams);
            ((TextView) result_view.findViewById(R.id.mikan_item_name)).setText(link); // 设置链接
            ((TextView) result_view.findViewById(R.id.mikan_item_info)).setText("类型：" + mimeType + " 抓取时间：" + dataTime);
            if (UIUtils.isDarkMode(ResourceTrackerActivity.this)) {
                ((TextView) result_view.findViewById(R.id.mikan_item_name)).setTextColor(getResources().getColor(R.color.grey_400)); // 设置番组名
                ((TextView) result_view.findViewById(R.id.mikan_item_info)).setTextColor(getResources().getColor(R.color.grey_600)); // 设置番组名
                ((LinearLayout) result_view.findViewById(R.id.main_hr_1)).setBackgroundColor(getResources().getColor(R.color.grey_800));
                ((ImageView) result_view.findViewById(R.id.copyicon)).setImageDrawable(getResources().getDrawable(R.drawable.copy_icon_light));
            }
            ((LinearLayout) result_view.findViewById(R.id.mikan_item)).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // 打开链接资源
                    IntentUtils.openUrl(link, ResourceTrackerActivity.this);
                    return true;
                }
            });
            ((LinearLayout) result_view.findViewById(R.id.mikan_item)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("res_tracker", link);
                    clipboardManager.setPrimaryClip(clipData); // 复制到剪贴板
                    Toast.makeText(ResourceTrackerActivity.this, "已复制链接到剪贴板", Toast.LENGTH_SHORT).show(); // 提示
                }
            });

            ((LinearLayout) findViewById(R.id.search_result)).addView(result_view); // 添加数据
        }

        // 深色模式适配
        if (UIUtils.isDarkMode(this)) {
            ((LinearLayout) findViewById(R.id.main_frame)).setBackgroundColor(getResources().getColor(R.color.grey_900)); // 背景颜色
            ((Toolbar) findViewById(R.id.main_toolbar)).setBackgroundColor(getResources().getColor(R.color.blue_600)); // 标题栏

            ((TextView) findViewById(R.id.search_loading)).setTextColor(getResources().getColor(R.color.grey_400));
        }
    }

}
