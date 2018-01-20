package cn.peng.pxun.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 获得屏幕相关的工具类
 */
public class ScreenUtil {
	private static boolean isFullScreen = false;
	private static DisplayMetrics dm = null;

	/**
	 * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获得设备屏幕密度
	 * @param activity
	 * @return
	 */
	public static float getDisplayMetrics(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	/**
	 * 获取当前屏幕亮度
	 * @param activity
	 * @return
	 */
	public static int getScreenBrightness(Activity activity) {
		int value = 0;
		ContentResolver cr = activity.getContentResolver();
		try {
			value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Settings.SettingNotFoundException e) {

		}
		return value;
	}

	/**
	 * 设置屏幕亮度
	 */
	public static void setScreenBrightness(Activity activity, float f) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = f;
		if (lp.screenBrightness > 1.0f)
			lp.screenBrightness = 1.0f;
		else if (lp.screenBrightness < 0.01f)
			lp.screenBrightness = 0.01f;
		activity.getWindow().setAttributes(lp);
	}

	/**
	 * 获取屏幕宽度
	 * @param activity
	 * @return
	 */
	public static int getScreenWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		int width = dm.widthPixels;
		return width;
	}

	/**
	 * 获取屏幕高度
	 * @param activity
	 * @return
	 */
	public static int getScreenHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		//虚拟按键的高度
		int result = 0;
		int rid = activity.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
		if (rid != 0) {
			int resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
			result = activity.getResources().getDimensionPixelSize(resourceId);
		}

		int height = dm.heightPixels + result;
		return height;
	}

	/**
	 * 获得状态栏的高度
	 * @param activity
	 * @return
	 */
	public static int getStatusHeight(Activity activity) {
		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
			statusHeight = activity.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	/**
	 * 获取当前屏幕截图，包含状态栏
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;
	}

	/**
	 * 获取当前屏幕截图，不包含状态栏
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
		view.destroyDrawingCache();
		return bp;
	}


	public static DisplayMetrics displayMetrics(Context context) {
		if (null != dm) {
			return dm;
		}
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	public static void toggleFullScreen(Activity activity) {
		Window window = activity.getWindow();
		int flagFullscreen = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (isFullScreen) {
			window.clearFlags(flagFullscreen);
			isFullScreen = false;
		} else {
			window.setFlags(flagFullscreen, flagFullscreen);
			isFullScreen = true;
		}
	}

	/**
	 * 保持屏幕常亮
	 */
	public static void keepBright(Activity activity) {
		//需在setContentView前调用
		int keepScreenOn = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		activity.getWindow().setFlags(keepScreenOn, keepScreenOn);
	}

	public static int widthPixels(Context context) {
		return displayMetrics(context).widthPixels;
	}

	public static int heightPixels(Context context) {
		return displayMetrics(context).heightPixels;
	}

	public static float density(Context context) {
		return displayMetrics(context).density;
	}

	public static int densityDpi(Context context) {
		return displayMetrics(context).densityDpi;
	}

	public static boolean isFullScreen() {
		return isFullScreen;
	}

}
