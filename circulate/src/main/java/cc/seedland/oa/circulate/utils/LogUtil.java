package cc.seedland.oa.circulate.utils;

import android.util.Log;

/**
 * 日志打印
 */
public class LogUtil {
	
	private static final boolean mDebug = true;

	private static final String TAG = "circulate";

	/** 在控制台打印信息 */
	public static void i(String msg) {
		i(TAG, msg);
	}

	public static void d(String msg) {
		d(TAG, msg);
	}

	public static void w(String msg) {
		w(TAG, msg);
	}

	public static void v(String msg) {
		v(TAG, msg);
	}

	public static void e(String msg) {
		e(TAG, msg);
	}

	public static void i(String tag, String msg) {
		String mess = getLogPrefix() + msg;
		if (mDebug)
			Log.i(tag, mess);
	}

	public static void d(String tag, String msg) {
		String mess = getLogPrefix() + msg;
		if (mDebug)
			Log.d(tag, mess);
	}

	public static void w(String tag, String msg) {
		String mess = getLogPrefix() + msg;
		if (mDebug)
			Log.w(tag, mess);
	}

	public static void v(String tag, String msg) {
		String mess = getLogPrefix() + msg;
		if (mDebug)
			Log.v(tag, mess);
	}

	public static void e(String tag, String msg) {
		String mess = getLogPrefix() + msg;
		if (mDebug)
			Log.e(tag, mess);
	}

	/** 为了方便查看，在打印的日志信息内容前添加一个序号前缀 */
	private static String getLogPrefix() {
		logId++;
		if (logId >= 1000)
			logId = 1;
		return "(" + logId + "). ";
	}

	private static int logId = 0;

	/**
	 * 截断输出日志
	 * @param msg
	 */
	public static void eCut(String tag, String msg) {
		if(mDebug) {
			if (tag == null || tag.length() == 0
					|| msg == null || msg.length() == 0)
				return;

			int segmentSize = 3 * 1024;
			long length = msg.length();
			if (length <= segmentSize ) {// 长度小于等于限制直接打印
				Log.e(tag, msg);
			}else {
				while (msg.length() > segmentSize ) {// 循环分段打印日志
					String logContent = msg.substring(0, segmentSize );
					msg = msg.replace(logContent, "");
					Log.e(tag, logContent);
				}
				Log.e(tag, msg);// 打印剩余日志
			}
		}

	}
	/**
	 * 截断输出日志
	 * @param msg
	 */
	public static void eCut(String msg) {
		if(mDebug) {
			if (TAG == null || TAG.length() == 0
					|| msg == null || msg.length() == 0)
				return;

			int segmentSize = 3 * 1024;
			long length = msg.length();
			if (length <= segmentSize ) {// 长度小于等于限制直接打印
				Log.e(TAG, msg);
			}else {
				while (msg.length() > segmentSize ) {// 循环分段打印日志
					String logContent = msg.substring(0, segmentSize );
					msg = msg.replace(logContent, "");
					Log.e(TAG, logContent);
				}
				Log.e(TAG, msg);// 打印剩余日志
			}
		}

	}
}
