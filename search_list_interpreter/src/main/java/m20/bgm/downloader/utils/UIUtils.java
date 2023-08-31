package m20.bgm.downloader.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import m20.bgm.downloader.settings.MainSettingsActivity;
import m20.bgm.downloader.utils.packages.SystemBarTintManager;

public class UIUtils {
    /**
     * 设置状态栏为透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(Activity activity, int color) {
        //Android6.0（API 23）以上，系统方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            setTranslucentStatus(activity);
            //设置状态栏颜色
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);
        }
    }

    // 获取水波纹效果 drawable，传参 Context
    public static Drawable getSelectableItemBackground(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        int[] attribute = new int[]{android.R.attr.selectableItemBackground};
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
        Drawable drawable = typedArray.getDrawable(0);
        typedArray.recycle();
        return drawable;
    }

    // 获取系统状态栏高度
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result =Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    // 获取系统是否是深色模式
    public static boolean isDarkMode(Context context) {
        boolean isNightMode = false;
        // 读取相关配置信息

        SharedPreferences sharedPreferences= context.getSharedPreferences(MainSettingsActivity.settings_sharedPreferences, context.MODE_PRIVATE);
        final int[] darkmode = {sharedPreferences.getInt(MainSettingsActivity.settings_darkmode, 0)}; // 深色模式

        if (darkmode[0] == 0) {
            // 跟随系统
            int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    // Night mode is not active, we're using the light theme
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    // Night mode is active, we're using dark theme
                    isNightMode = true;
                    break;
            }
        } else if (darkmode[0] == 1) {
            // 始终深色
            isNightMode = true;
        } else {
            // 始终浅色
            isNightMode = false;
        }

        return isNightMode;
    }
}
