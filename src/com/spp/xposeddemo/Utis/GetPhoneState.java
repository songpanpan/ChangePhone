package com.spp.xposeddemo.Utis;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.duowan.mobile.netroid.Listener;
import com.spp.xposeddemo.Utis.PostStatus.StatusBack;

/**
 * �?��手机状�?
 * 
 * @author Administrator
 * 
 */
public class GetPhoneState {
	private static ConnectivityManager connManager = null;
	private static TelephonyManager telephonyManager = null;
	public static DisplayMetrics dm;

	/**
	 * �?��SDCard是否可用
	 * 
	 * @return
	 */
	public static boolean isSDCardAvailable() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �?��网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkAvailable(Context appContext) {
		Context context = appContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 获取当前操作系统的语�?
	 * 
	 * @return String 系统语言
	 */
	public static String getSysLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/**
	 * 获取手机品牌
	 * 
	 * @return String 手机型号
	 */
	public static String getBrand() {
		return android.os.Build.BRAND;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return String 手机型号
	 */
	public static String getModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取操作系统的版本号
	 * 
	 * @return String 系统版本�?
	 */
	public static String getSysRelease() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 读取手机串号imei
	 * 
	 * @param con
	 *            上下�?
	 * @return String 手机串号
	 */
	public static String readTelephoneSerialNum(Context con) {
		TelephonyManager telephonyManager = (TelephonyManager) con
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		if (TextUtils.isEmpty(imei)) {
			imei = getLocalMacAddress(con);
		}
		return imei;
	}

	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String mac = info.getMacAddress();
		if (TextUtils.isEmpty(mac)) {
			mac = "00:00:00:00:00:00";
		}
		return mac;
	}

	/**
	 * 读取sim卡序列号imsi
	 */
	@SuppressLint("NewApi")
	public static String hasIccCard(Context con) {
		if (con == null) {
			return "";
		}
		if (telephonyManager == null) {
			telephonyManager = (TelephonyManager) con
					.getSystemService(Context.TELEPHONY_SERVICE);
		}
		if (telephonyManager.hasIccCard())
			return "true";
		return "false";
	}

	/**
	 * 读取sim卡序列号imsi
	 */
	public static String readSimSerialNum(Context con) {
		if (con == null) {
			return "";
		}
		if (telephonyManager == null) {
			telephonyManager = (TelephonyManager) con
					.getSystemService(Context.TELEPHONY_SERVICE);
		}
		if (telephonyManager.getSubscriberId() == null)
			return "";
		return telephonyManager.getSubscriberId();
	}

	/**
	 * 读取sim卡序列号
	 */
	public static String readSimICCID(Context con) {
		if (con == null) {
			return "";
		}
		if (telephonyManager == null) {
			telephonyManager = (TelephonyManager) con
					.getSystemService(Context.TELEPHONY_SERVICE);
		}
		if (telephonyManager.getSimSerialNumber() == null)
			return "";
		return telephonyManager.getSimSerialNumber();
	}

	/**
	 * Role:获取当前设置的电话号码 <BR>
	 * Date:2012-3-12 <BR>
	 * 
	 * @author CODYY)peijiangping
	 */

	public static String getNativePhoneNumber() {
		String NativePhoneNumber = null;
		NativePhoneNumber = telephonyManager.getLine1Number();
		if (TextUtils.isEmpty(NativePhoneNumber))
			return "0";
		return NativePhoneNumber;
	}

	public static void GetNetIp(final Context context) {
		String url = "http://2017.ip138.com/ic.asp";
		PostStatus post = new PostStatus();
		post.PostPayStatus(url, new StatusBack() {

			@Override
			public void Success(String result) {
				// TODO Auto-generated method stub
//				Log.e("spptag", "result:" + result);
				String line = null;
				// 从反馈的结果中提取出IP地址
				if (result.length() < 10 | result.startsWith("org"))
					return;
				int start = result.indexOf("[");
				int end = result.indexOf("]", start + 1);
				line = result.substring(start + 1, end);
				Log.e("spptag", "IP:" + line);
			}

			@Override
			public void Fail(String result) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * 获取运营商信�?
	 * 
	 * @param con
	 *            上下�?
	 * @return String 运营商信�?
	 */
	public static String getCarrier(Context con) {
		TelephonyManager telManager = (TelephonyManager) con
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telManager.getSubscriberId();
		if (imsi != null && !"".equals(imsi)) {
			if (imsi.startsWith("46000") || imsi.startsWith("46002")
					|| imsi.startsWith("46007")) {// 因为移动网络编号46000下的IMSI已经用完，所以虚拟了�?��46002编号�?34/159号段使用了此编号
				return "yidong";
			} else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
				return "liantong";
			} else if (imsi.startsWith("46003") || imsi.startsWith("46005")
					|| imsi.startsWith("46011")) {
				return "dianxin";
			}
		}
		return "";
	}

	/**
	 * 获取网络类型
	 * 
	 * @param context
	 *            上下�?
	 * @return String 返回网络类型
	 */
	public static String getAccessNetworkType(Context context) {
		int type = 0;
		try {
			if (connManager != null) {
				type = connManager.getActiveNetworkInfo().getType();
			} else {
				connManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				type = connManager.getActiveNetworkInfo().getType();
			}
			if (type == ConnectivityManager.TYPE_WIFI) {
				return "wifi";
			} else if (type == ConnectivityManager.TYPE_MOBILE) {
				return "3G/GPRS";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回当前程序版本�?
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}
}
