package m20.bgm.downloader.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class IntentUtils {
    // 浏览器打开网页，传参网页链接，Activity
    public static void openUrl(String url, Activity activity) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }
}
