package m20.bgm.downloader.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WaitingWatchUtils {

    // 插入稍后再看，传参收藏的关键词、Context
    public void insertKeyword(String name, Context context) {    }

    // 读取稍后再看，传参 Context
    public Cursor queryKeyword(Context context) {    }

    // 删除稍后再看，传参关键词，Context
    public void removeKeyword(String keyword, Context context) {    }

    // 生成稍后再看格式
    public String createWWCFormat(double id, String si, String name, String link) {    }

    // 获取格式解析后数据，传参原始数据，请求类型
    public static String getWWCFormatResult(String data, String reuType) {  return "null";   }

}
