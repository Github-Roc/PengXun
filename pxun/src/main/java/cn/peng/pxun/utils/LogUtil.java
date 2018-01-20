package cn.peng.pxun.utils;

import android.util.Log;

import cn.peng.pxun.modle.AppConfig;

import static android.R.attr.tag;

/**
 * Created by th on 2016/12/12.
 */
public class LogUtil {
    private static String debugTag = AppConfig.DEBUG_TAG;   // LogCat的标记
    private static final int LOG_LEVEL_NONE = 0;     //不输出任和log
    private static final int LOG_LEVEL_DEBUG = 1;    //调试 蓝色
    private static final int LOG_LEVEL_INFO = 2;     //提现 绿色
    private static final int LOG_LEVEL_WARN = 3;     //警告 橙色
    private static final int LOG_LEVEL_ERROR = 4;    //错误 红色
    private static final int LOG_LEVEL_ALL = 5;      //输出所有等级

    /**
     * 允许输出的log日志等级
     * 当出正式版时,把mLogLevel的值改为 LOG_LEVEL_NONE,
     * 就不会输出任何的Log日志了.
     */
    private static int mLogLevel = LOG_LEVEL_ALL;

    /**
     * 获取Log等级
     * @return
     */
    public static int getLogLevel() {
        return mLogLevel;
    }

    /**
     * 设置Log等级
     * @param level
     */
    public static void setLogLevel(int level) {
        LogUtil.mLogLevel = level;
    }

    /**
     * 以级别为 v 的形式输出LOG ，verbose啰嗦的意思
     */
    public static void v(String msg) {
        if (getLogLevel() >= LOG_LEVEL_ALL) {
            Log.v(debugTag, msg);
        }
    }

    /**
     * 以级别为 v 的形式输出LOG ，verbose啰嗦的意思
     */
    public static void v(String tag, String msg) {
        if (getLogLevel() >= LOG_LEVEL_ALL) {
            Log.v(tag, msg);
        }
    }

    /**
     * 以级别为 d 的形式输出LOG,输出debug调试信息
     */
    public static void d(String msg) {
        if (getLogLevel() >= LOG_LEVEL_DEBUG) {
            Log.d(debugTag, msg);
        }
    }

    /**
     * 以级别为 d 的形式输出LOG,输出debug调试信息
     */
    public static void d(String tag, String msg) {
        if (getLogLevel() >= LOG_LEVEL_DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * 以级别为 i 的形式输出LOG,一般提示性的消息information
     */
    public static void i(String msg) {
        if (getLogLevel() >= LOG_LEVEL_INFO) {
            Log.i(debugTag, msg);
        }
    }

    /**
     * 以级别为 i 的形式输出LOG,一般提示性的消息information
     */
    public static void i(String tag, String msg) {
        if (getLogLevel() >= LOG_LEVEL_INFO) {
            Log.i(tag, msg);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG,显示warning警告，一般是需要我们注意优化Android代码
     */
    public static void w(String msg) {
        if (getLogLevel() >= LOG_LEVEL_WARN) {
            Log.w(debugTag, msg);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG,显示warning警告，一般是需要我们注意优化Android代码
     */
    public static void w(String tag, String msg) {
        if (getLogLevel() >= LOG_LEVEL_WARN) {
            Log.w(tag, msg);
        }
    }

    /**
     * 以级别为 e 的形式输出LOG,error级别的Log信息,查看错误源的关键
     */
    public static void e(String msg) {
        if (getLogLevel() >= LOG_LEVEL_ERROR) {
            Log.e(debugTag, msg);
        }
    }

    /**
     * 以级别为 e 的形式输出LOG,error级别的Log信息,查看错误源的关键
     */
    public static void e(String tag, String msg) {
        if (getLogLevel() >= LOG_LEVEL_ERROR) {
            Log.e(tag, msg);
        }
    }
}


