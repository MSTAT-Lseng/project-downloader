package m20.bgm.downloader.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import org.xutils.common.util.DensityUtil;

public class WidgetUtils {
    // 设置番剧详情控件背景颜色，传参 byte[] imageData图像数据，番剧详情控件 LinearLayout
    public static void setBgmInfoBackground(byte[] imageData, LinearLayout linearLayout) {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length); // 初始化 Bitmap

            // 使用 Palette 获取图像主题色（1.0.6 版本）
            Palette.Builder builder = Palette.from(bitmap);
            builder.generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    int mutedColor = palette.getMutedColor(Color.parseColor("#8C8C8C"));
                    linearLayout.setBackgroundColor(mutedColor); // 设置背景
                }
            });
        } catch (Exception e) {
            // 出现错误则执行
            linearLayout.setBackgroundColor(Color.parseColor("#8C8C8C")); // 设置背景
        }
    }

    // 设置深色背景颜色
    public static void setBgmInfoBackgroundDark(byte[] imageData, LinearLayout linearLayout) {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length); // 初始化 Bitmap

            // 使用 Palette 获取图像主题色（1.0.6 版本）
            Palette.Builder builder = Palette.from(bitmap);
            builder.generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    int mutedColor = palette.getDarkMutedColor(Color.parseColor("#8C8C8C"));
                    linearLayout.setBackgroundColor(mutedColor); // 设置背景
                }
            });
        } catch (Exception e) {
            // 出现错误则执行
            linearLayout.setBackgroundColor(Color.parseColor("#8C8C8C")); // 设置背景
        }
    }
}
