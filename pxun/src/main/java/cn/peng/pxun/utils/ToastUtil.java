package cn.peng.pxun.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 可以即时刷新显示数据的吐司
 */
public class ToastUtil {
    public static Toast mToast;

    public static void showToast(Context mContext, String msg) {
        if (mToast == null && mContext != null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();                                                                                                                           
    }
}
