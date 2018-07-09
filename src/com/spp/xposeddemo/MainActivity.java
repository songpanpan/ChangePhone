package com.spp.xposeddemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.TreeMap;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.duowan.mobile.netroid.NetroidError;
import com.spp.xposeddemo.Utis.GetPhoneState;
import com.spp.xposeddemo.Utis.Mnt;
import com.spp.xposeddemo.Utis.PhoneInfoBean;
import com.spp.xposeddemo.Utis.SharedPref;
import com.spp.xposeddemo.net.RequestTask;
import com.spp.xposeddemo.net.RequestTask.RequestCallBack;

public class MainActivity extends Activity {

	public static boolean ISHOCK = true;
	public static String MYPATH = "hockdata.txt";
	TextView info;
	private TelephonyManager phone;
	private WifiManager wifi;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			GetPhoneState.GetNetIp(getApplicationContext());
			handler.sendEmptyMessageDelayed(0, 1000);
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// handler.sendEmptyMessage(0);
		getPhoneInfo(this);

		phone = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		info = (TextView) findViewById(R.id.info);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		// 获取屏幕信息
		Point point = new Point();
		DisplayMetrics dm = getResources().getDisplayMetrics();
		double x = Math.pow(point.x / dm.xdpi, 2);
		double y = Math.pow(point.y / dm.ydpi, 2);
		ConnectivityManager connectMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		String type = "";
		if (connectMgr != null) {
			NetworkInfo netInfo = connectMgr.getActiveNetworkInfo();
			if (netInfo != null) {
				int networkType = netInfo.getSubtype();

				switch (networkType) {
				case TelephonyManager.NETWORK_TYPE_CDMA:
					type = "CDMA";
					break;
				case TelephonyManager.NETWORK_TYPE_EDGE:
					type = "EDGE";
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					type = "EVDO0";
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					type = "EVDOA";
					break;
				case TelephonyManager.NETWORK_TYPE_GPRS:
					type = "GPRS";
					break;
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					type = "HSDPA";
					break;
				case TelephonyManager.NETWORK_TYPE_HSPA:
					type = "HSPA";
					break;
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					type = "HSUPA";
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
					type = "UMTS";
					break;

				default:
					break;
				}
			}

		}

		info.setText("imei: "
				+ getIMEI(this)
				+ "\nIMSI: "
				+ GetPhoneState.readSimSerialNum(this)
				+ "\nnumber: "
				+ phone.getLine1Number()
				+ "\nARCH: "
				+ Build.CPU_ABI
				+ "\nBRAND: "
				+ Build.BRAND
				+ "\nBuild_VERSION_SDK_INT: "
				+ Build.VERSION.SDK_INT
				+ "\nDEVICE: "
				+ Build.DEVICE
				+ "\nDISPLAY: "
				+ Build.DISPLAY
				+ "\nFINGERPRINT: "
				+ Build.FINGERPRINT
				+ "\nHARDWARE: "
				+ Build.HARDWARE
				+ "\nHOST: "
				+ Build.HARDWARE
				+ "\nHOST: "
				+ Build.HOST
				+ "\nMANUFACTURER: "
				+ Build.MANUFACTURER
				+ "\nMODEL: "
				+ android.os.Build.MODEL
				+ "\nPRODUCT: "
				+ Build.PRODUCT
				+ "\nRELEASE: "
				+ Build.VERSION.RELEASE
				+ "\nSDK: "
				+ Build.VERSION.SDK
				+ "\nSERIAL: "
				+ Build.SERIAL
				+ "\nTAGS: "
				+ Build.TAGS
				+ "\nTYPE: "
				+ Build.TYPE
				+ "\nUSER: "
				+ Build.USER
				+ "\nbuild_board: "
				+ Build.BOARD
				+ "\nbuild_bootloader"
				+ Build.BOOTLOADER
				+ "\nbuild_cpuabi: "
				+ Build.CPU_ABI
				+ "\nbuild_cpuabi2: "
				+ Build.CPU_ABI2
				+ "\nbuild_time: "
				+ Build.TIME
				+ "\nconnect_mode: "
				+ type
				+ "\ndensity: "
				+ dm.density
				+ "\ndensityDpi: "
				+ dm.densityDpi
				+ "\nget: "
				+ Build.VERSION.INCREMENTAL
				+ "\ngetAddress: "
				+ BluetoothAdapter.getDefaultAdapter().getAddress()
				+ "\ngetBSSID: "
				+ wifi.getConnectionInfo().getBSSID()
				+ "\ngetDeviceId: "
				+ phone.getDeviceId()
				+ "\ngetIpAddress: "
				+ getLocalHostIp()
				+ "\ngetMacAddress: "
				+ wifi.getConnectionInfo().getMacAddress()
				+ "\ngetMetrics: "
				+ display.getWidth()
				+ "x"
				+ display.getHeight()
				+ "\ngetNetworkCountryIso: "
				+ phone.getNetworkCountryIso()
				+ "\ngetNetworkOperator: "
				+ phone.getNetworkOperator()
				+ "\ngetNetworkOperatorName: "
				+ phone.getNetworkOperatorName()
				+ "\ngetNetworkType: "
				+ phone.getNetworkType()
				+ "\ngetPhoneType: "
				+ phone.getPhoneType()
				+ "\ngetRadioVersion: "
				+ Build.getRadioVersion()
				+ "\ngetSSID: "
				+ wifi.getConnectionInfo().getBSSID()
				+ "\ngetSimCountryIso: "
				+ phone.getSimCountryIso()
				+ "\ngetSimOperator: "
				+ phone.getSimOperator()
				+ "\ngetSimOperatorName: "
				+ phone.getSimOperatorName()
				+ "\ngetSimSerialNumber: "
				+ phone.getSimSerialNumber()
				+ "\ngetSimState: "
				+ phone.getSimState()
				+ "\ngetString: "
				+ Settings.Secure.getString(getContentResolver(),
						Settings.Secure.ANDROID_ID) + "\ngetSubtypeName: "
				+ phone.getSubscriberId() + "\nphone_bluename: "
				+ BluetoothAdapter.getDefaultAdapter().getName()
				+ "\nphone_deviceversion: " + phone.getDeviceSoftwareVersion()
				+ "\nscaledDensity: " + dm.density + "\nsetCpuName: "
				+ getCpuName());
		/*
		 * String SdCardPath = getSdCardPath(); if (SdCardPath == null |
		 * SdCardPath.equals("")) { Toast.makeText(this, "读取sd卡根目录失败",
		 * Toast.LENGTH_LONG).show(); ISHOCK = false; return; } String dataPath
		 * = SdCardPath + "/hock/" + MYPATH; String folderPath = SdCardPath +
		 * "/hock/";
		 * 
		 * Log.e("spptag", "===========dataPath===========" + dataPath); File
		 * file = new File(dataPath); File folder = new File(folderPath); if
		 * (!folder.exists()) folder.mkdirs(); Log.e("spptag",
		 * "===========realdataPath===========" + file.getAbsolutePath());
		 * Log.e("spptag", "===========file.exists()===========" +
		 * file.exists()); if (!file.exists()) { Toast.makeText(this,
		 * "数据目录下文件不存在", Toast.LENGTH_LONG).show(); ISHOCK = false; try {
		 * file.createNewFile(); InputStream is =
		 * getAssets().open("myjsonone.txt"); // 获得AssetManger int size =
		 * is.available();// 取得数据流的数据大小 byte[] buffer = new byte[size];
		 * is.read(buffer); is.close(); String txt = new String(buffer,
		 * "utf-8"); Log.e("spptag", "json:" + txt); setSdCardValue(txt,
		 * dataPath); Log.e("spptag", "===========文件不存在，创建写入assert===========");
		 * } catch (Exception e) { // TODO: handle exception } return; }
		 * 
		 * String data = getSdCardValue(dataPath); Log.e("spptag",
		 * "-------读取data------" + data); if (data.startsWith("ï»¿")) { data =
		 * data.substring(3); } if (data != null && data.length() > 0) {
		 * PhoneInfoBean bean; try { Log.e("spptag", "parse:" + data); bean =
		 * (PhoneInfoBean) new PhoneInfoParser().parse(this, data); Save(bean);
		 * CPU(); } catch (Exception e) { // TODO: handle exception
		 * e.printStackTrace(); } }
		 */
		// try {
		// InputStream is = getAssets().open("myjsonone.txt"); // 获得AssetManger
		// // 对象,
		// // 调用其open
		// // 方法取得
		// // 对应的inputStream对象
		// int size = is.available();// 取得数据流的数据大小
		// byte[] buffer = new byte[size];
		// is.read(buffer);
		// is.close();
		// String txt = new String(buffer, "utf-8");
		// Log.e("spptag", "json:" + txt);
		// PhoneInfoBean bean;
		// try {
		// Log.e("spptag", "parse:" + txt);
		// bean = (PhoneInfoBean) new PhoneInfoParser().parse(this, txt);
		// Save(bean);
		// CPU();
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }

	}

	/**
	 * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
	 * 
	 * @return
	 */
	public static boolean isSdCardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡根目录路径
	 * 
	 * @return
	 */
	public static String getSdCardPath() {
		boolean exist = isSdCardExist();
		String sdpath = "";
		if (exist) {
			sdpath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		} else {
			sdpath = "不适用";
		}
		return sdpath;

	}

	/**
	 * 读取目录下txt文件内容
	 * 
	 * @param filename
	 * @return
	 */
	public static String getSdCardValue(String filename) {
		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(filename);
			FileInputStream is = new FileInputStream(file);
			int c;
			while ((c = is.read()) != -1) {
				sb.append((char) c);
			}
			is.close();
			System.out.println("读取成功：" + sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 写入数据
	 * 
	 * @param value
	 * @param filename
	 * @return
	 */
	public static String setSdCardValue(String value, String filename) {
		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(filename);
			FileOutputStream fw = new FileOutputStream(file);
			fw.write(value.getBytes());
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 判断路径下文件是否存在
	 * 
	 * @param strFile
	 * @return
	 */
	public boolean fileIsExsits(String strFile) {
		try {
			File file = new File(strFile);
			if (file.exists()) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return false;
	}

	private void Save(PhoneInfoBean bean) {
		SharedPref mySP = new SharedPref(getApplicationContext());
		/*
		 * build 系列
		 */

		mySP.setSharedPref("serial", bean.SERIAL); // 串口序列号，对应SERIAL
		mySP.setSharedPref("getBaseband", bean.get); // get
														// 参数，对应get，？？？？？？？？？？
		mySP.setSharedPref("BaseBand", bean.getRadioVersion); // 固件版本，getRadioVersion
		mySP.setSharedPref("board", bean.build_board); // 主板，build_board
		mySP.setSharedPref("brand", bean.BRAND); // 设备品牌，BRAND
		mySP.setSharedPref("ABI", bean.build_cpuabi); // 设备指令集名称 1，build_cpuabi
		mySP.setSharedPref("ABI2", bean.build_cpuabi2); // 设备指令集名称
														// 2，build_cpuabi2
		mySP.setSharedPref("device", bean.DEVICE); // 设备驱动名称，DEVICE
		mySP.setSharedPref("display", bean.DISPLAY); // 设备显示的版本包 固件版本，DISPLAY
		// 指纹 设备的唯一标识。由设备的多个信息拼接合成。FINGERPRINT
		mySP.setSharedPref("fingerprint", bean.FINGERPRINT);
		mySP.setSharedPref("NAME", bean.HARDWARE); // 设备硬件名称，HARDWARE
		mySP.setSharedPref("ID", bean.ID); // 设备版本号，ID
		mySP.setSharedPref("Manufacture", bean.MANUFACTURER); // 设备制造商，MANUFACTURER
		mySP.setSharedPref("model", bean.MODEL); // 手机的型号 设备名称，MODEL
		mySP.setSharedPref("product", bean.PRODUCT); // 设备驱动名称，PRODUCT
		mySP.setSharedPref("booltloader", bean.build_bootloader); // 设备引导程序版本号，build_bootloader
		mySP.setSharedPref("host", bean.HOST); // 设备主机地址，HOST
		mySP.setSharedPref("build_tags", bean.TAGS); // 设备标签，TAGS
		mySP.setSharedPref("shenbei_type", bean.TYPE); // 设备版本类型，TYPE
		mySP.setSharedPref("incrementalincremental", bean.get); // 源码控制版本号，get
		mySP.setSharedPref("AndroidVer", bean.RELEASE); // 系统版本，RELEASE
		mySP.setSharedPref("API", bean.SDK); // 系统的API级别，SDK
		if (bean.build_time != null && bean.build_time.length() > 0)
			mySP.setLongSharedPref("time", Long.parseLong(bean.build_time));// 固件时间，build_time2
		mySP.setSharedPref("AndroidID", bean.getString); // android id，getString
		// mySP.setSharedPref("DESCRIPTION",
		// "jfltexx-user 4.3 JSS15J I9505XXUEML1 release-keys"); // 用户的KEY？？？？？？
		/*
		 * TelephonyManager相关
		 */
		mySP.setSharedPref("IMEI", bean.getDeviceId); // 序列号IMEI，getDeviceId
		mySP.setSharedPref("LYMAC", bean.getAddress);// 蓝牙 MAC，getAddress
		mySP.setSharedPref("WifiMAC", bean.getMacAddress); // WIF
															// mac地址，getMacAddress
		mySP.setSharedPref("WifiName", bean.getSSID); // 无线路由器名，getSSID
		mySP.setSharedPref("BSSID", bean.getBSSID); // 无线路由器地址，getBSSID
		mySP.setSharedPref("IMSI", bean.getSubtypeName);// getSubtypeName
		// mySP.setSharedPref("PhoneNumber", bean.getLine1Number); //
		// 手机号码,getLine1Number
		mySP.setSharedPref("SimSerial", bean.getSimSerialNumber); // 手机卡序列号,getSimSerialNumber
		mySP.setSharedPref("networktor", bean.getNetworkOperator); // 网络运营商类型,getNetworkOperator
		mySP.setSharedPref("Carrier", bean.getNetworkOperatorName);// 网络类型名,getNetworkOperatorName
		mySP.setSharedPref("CarrierCode", bean.getSimOperator); // 运营商,getSimOperator
		mySP.setSharedPref("simopename", bean.getSimOperatorName);// 运营商名字,getSimOperatorName
		mySP.setSharedPref("gjISO", bean.getNetworkCountryIso);// 国家iso代码,getNetworkCountryIso
		mySP.setSharedPref("CountryCode", bean.getSimCountryIso);// 手机卡国家,getSimCountryIso
		mySP.setSharedPref("deviceversion", bean.phone_deviceversion); // 返回系统版本,phone_deviceversion
		if (bean.getPhoneType != null && bean.getPhoneType.length() > 0)
			mySP.setintSharedPref("getType",
					Integer.parseInt(bean.getPhoneType)); // 联网方式
															// 1为WIFI
		if (bean.getNetworkType != null && bean.getNetworkType.length() > 0) // 2为流量,getPhoneType
			mySP.setintSharedPref("networkType",
					Integer.parseInt(bean.getNetworkType));// 网络类型,getNetworkType
		if (bean.getPhoneType != null && bean.getPhoneType.length() > 0)
			mySP.setintSharedPref("phonetype",
					Integer.parseInt(bean.getPhoneType)); // 手机类型,getPhoneType
		mySP.setintSharedPref("SimState", Integer.parseInt(bean.getSimState)); // 手机卡状态,getSimState
		mySP.setintSharedPref("width", 720); // 宽,getMetrics x xxy
		mySP.setintSharedPref("height", 1280); // 高,getMetrics y
		mySP.setSharedPref("getIP", bean.getIpAddress); // 内网ip(wifl可用),getIpAddress
		/*
		 * 屏幕相关
		 */
		if (bean.densityDpi != null && bean.densityDpi.length() > 0)
			mySP.setintSharedPref("DPI", Integer.parseInt(bean.densityDpi)); // dpi,densityDpi
		if (bean.density != null && bean.density.length() > 0)
			mySP.setfloatharedPref("density", Float.parseFloat(bean.density)); // density,density
		mySP.setfloatharedPref("xdpi", (float) 200.123);// xdpi
		mySP.setfloatharedPref("ydpi", (float) 211.123);// ydpi
		if (bean.density != null && bean.density.length() > 0)
			mySP.setfloatharedPref("scaledDensity",
					Float.parseFloat(bean.density)); // 字体缩放比例，scaledDensity

		/*
		 * 显卡信息
		 */

		mySP.setSharedPref("GLRenderer", "Adreno (TM) 111"); // GPU，GL_RENDERER
																// k
		mySP.setSharedPref("GLVendor", "UFU");// GPU厂商，GL_VENDOR

		/*
		 * 位置信息
		 * 
		 * 30.2425140000,120.1404220000 杭州
		 */

		mySP.setfloatharedPref("lat", (float) 30.2425140000); // 纬度
		mySP.setfloatharedPref("log", (float) 120.1404220000); // 经度

		Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();

	}

	// private void Save() {
	// SharedPref mySP = new SharedPref(getApplicationContext());
	// /*
	// * build 系列
	// */
	//
	// mySP.setSharedPref("serial", "aee5060e"); // 串口序列号，对应SERIAL
	// mySP.setSharedPref("getBaseband", "SCL23KDU1BNG3"); // get
	// // 参数，对应get，？？？？？？？？？？
	// mySP.setSharedPref("BaseBand", "REL"); // 固件版本，getRadioVersion
	// mySP.setSharedPref("board", "msm8916"); // 主板，build_board
	// mySP.setSharedPref("brand", "Huawei"); // 设备品牌，BRAND
	// mySP.setSharedPref("ABI", "armeabi-v7a"); // 设备指令集名称 1，build_cpuabi
	// mySP.setSharedPref("ABI2", "armeabi"); // 设备指令集名称 2，build_cpuabi2
	// mySP.setSharedPref("device", "hwG750-T01"); // 设备驱动名称，DEVICE
	// mySP.setSharedPref("display", "R7c_11_151207"); // 设备显示的版本包 固件版本，DISPLAY
	// // 指纹 设备的唯一标识。由设备的多个信息拼接合成。FINGERPRINT
	// mySP.setSharedPref(
	// "fingerprint",
	// "Huawei/G750-T01/hwG750-T01:4.2.2/HuaweiG750-T01/C00B152:user/ota-rel-keys,release-keys");
	// mySP.setSharedPref("NAME", "mt6592"); // 设备硬件名称，HARDWARE
	// mySP.setSharedPref("ID", "KTU84P"); // 设备版本号，ID
	// mySP.setSharedPref("Manufacture", "HUAWEI"); // 设备制造商，MANUFACTURER
	// mySP.setSharedPref("model", "HUAWEI G750-T01"); // 手机的型号 设备名称，MODEL
	// mySP.setSharedPref("product", "hwG750-T01"); // 设备驱动名称，PRODUCT
	// mySP.setSharedPref("booltloader", "unknown"); //
	// 设备引导程序版本号，build_bootloader
	// mySP.setSharedPref("host", "ubuntu-121-114"); // 设备主机地址，HOST
	// mySP.setSharedPref("build_tags", "release-keys"); // 设备标签，TAGS
	// mySP.setSharedPref("shenbei_type", "user"); // 设备版本类型，TYPE
	// mySP.setSharedPref("incrementalincremental", "eng.root.20151207"); //
	// 源码控制版本号，get
	// mySP.setSharedPref("AndroidVer", "5.1"); // 系统版本，RELEASE
	// mySP.setSharedPref("API", "19"); // 系统的API级别，SDK
	//
	// mySP.setintSharedPref("time", 123456789);// 固件时间，build_time
	// mySP.setSharedPref("AndroidID", "fc4ad25f66d554a8"); // android
	// id，getString
	// mySP.setSharedPref("DESCRIPTION",
	// "jfltexx-user 4.3 JSS15J I9505XXUEML1 release-keys"); // 用户的KEY？？？？？？
	//
	// /*
	// * TelephonyManager相关
	// */
	// mySP.setSharedPref("IMEI", "506066104722640"); // 序列号IMEI，getDeviceId
	// mySP.setSharedPref("LYMAC", "BC:1A:EA:D9:8D:98");// 蓝牙 MAC，getAddress
	// mySP.setSharedPref("WifiMAC", "a8:a6:68:a3:d9:ef"); // WIF
	// // mac地址，getMacAddress
	// mySP.setSharedPref("WifiName", "免费WIFI"); // 无线路由器名，getSSID
	// mySP.setSharedPref("BSSID", "ce:ea:8c:1a:5c:b2"); // 无线路由器地址，getBSSID
	// mySP.setSharedPref("IMSI", "460017932859596");//getSubtypeName
	// mySP.setSharedPref("PhoneNumber", "13117511178"); // 手机号码,getLine1Number
	// mySP.setSharedPref("SimSerial", "89860179328595969501"); //
	// 手机卡序列号,getSimSerialNumber
	// mySP.setSharedPref("networktor", "46001"); // 网络运营商类型,getNetworkOperator
	// mySP.setSharedPref("Carrier", "中国联通");// 网络类型名,getNetworkOperatorName
	// mySP.setSharedPref("CarrierCode", "46001"); // 运营商,getSimOperator
	// mySP.setSharedPref("simopename", "中国联通");// 运营商名字,getSimOperatorName
	// mySP.setSharedPref("gjISO", "cn");// 国家iso代码,getNetworkCountryIso
	// mySP.setSharedPref("CountryCode", "cn");// 手机卡国家,getSimCountryIso
	// mySP.setSharedPref("deviceversion", "100"); // 返回系统版本,phone_deviceversion
	//
	// mySP.setintSharedPref("getType", 1); // 联网方式 1为WIFI 2为流量,getPhoneType
	// mySP.setintSharedPref("networkType", 6);// 网络类型,getNetworkType
	// mySP.setintSharedPref("phonetype", 5); // 手机类型,getPhoneType
	// mySP.setintSharedPref("SimState", 10); // 手机卡状态,getSimState
	// mySP.setintSharedPref("width", 720); // 宽,getMetrics x xxy
	// mySP.setintSharedPref("height", 1280); // 高,getMetrics y
	// mySP.setintSharedPref("getIP", -123456789); // 内网ip(wifl可用),getIpAddress
	// /*
	// * 屏幕相关
	// */
	//
	// mySP.setintSharedPref("DPI", 320); // dpi,densityDpi
	// mySP.setfloatharedPref("density", (float) 2.0); // density,density
	// mySP.setfloatharedPref("xdpi", (float) 200.123);//xdpi
	// mySP.setfloatharedPref("ydpi", (float) 211.123);//ydpi
	// mySP.setfloatharedPref("scaledDensity", (float) 2.0); //
	// 字体缩放比例，scaledDensity
	//
	// /*
	// * 显卡信息
	// */
	//
	// mySP.setSharedPref("GLRenderer", "Adreno (TM) 111"); // GPU，GL_RENDERER
	// mySP.setSharedPref("GLVendor", "UFU");// GPU厂商，GL_VENDOR
	//
	// /*
	// * 位置信息
	// *
	// * 30.2425140000,120.1404220000 杭州
	// */
	//
	// mySP.setfloatharedPref("lat", (float) 30.2425140000); // 纬度
	// mySP.setfloatharedPref("log", (float) 120.1404220000); // 经度
	//
	// Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
	//
	// }

	/*
	 * 创建 cpuinfo 文件 等待HOOK 重定向
	 */

	private void CPU() {

		String filePath = "/sdcard/Test/";
		String fileName = "cpuinfo";

		String hardware = "GT1000";

		// 生成文件夹之后，再生成文件，不然会出错
		Mnt.makeFilePath(filePath, fileName);

		String strFilePath = filePath + fileName;
		// 每次写入时，都换行写
		String strContent = "Processor	: ARMv7 Processor rev 0 (v7l)" + "\r\n";
		String strContent2 = "processor	: 0" + "\r\n";
		String strContent3 = "BogoMIPS	: 38.40";
		String strContent4 = "" + "\r\n";
		String strContent5 = "" + "\r\n";
		String strContent6 = "processor	: 1" + "\r\n";
		String strContent7 = "BogoMIPS	: 38.40" + "\r\n";
		String strContent8 = "" + "\r\n";
		String strContent9 = "Features	: swp half thumb fastmult vfp edsp neon vfpv3 tls vfpv4 idiva idivt"
				+ "\r\n";
		String strContent10 = "CPU implementer	: 0x51" + "\r\n";
		String strContent11 = "CPU architecture: 7" + "\r\n";
		String strContent12 = "CPU variant	: 0x2" + "\r\n";
		String strContent13 = "CPU part	: 0x06f" + "\r\n";
		String strContent14 = "CPU revision	: 0" + "\r\n";
		String strContent15 = "" + "\r\n";
		String strContent16 = "Hardware	: " + hardware + "\r\n";
		String strContent17 = "Revision	: 000d" + "\r\n";
		String strContent18 = "Serial		: 0000088900004e4b" + "\r\n";
		try {
			File file = new File(strFilePath);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			// 要先将已有文件删除、避免干扰。
			if (file.exists()) {
				file.delete();
			}

			RandomAccessFile raf = new RandomAccessFile(file, "rwd");
			raf.seek(file.length());
			raf.write(strContent.getBytes());
			raf.write(strContent2.getBytes());
			raf.write(strContent3.getBytes());
			raf.write(strContent4.getBytes());
			raf.write(strContent5.getBytes());
			raf.write(strContent6.getBytes());
			raf.write(strContent7.getBytes());
			raf.write(strContent8.getBytes());
			raf.write(strContent9.getBytes());
			raf.write(strContent10.getBytes());
			raf.write(strContent11.getBytes());
			raf.write(strContent12.getBytes());
			raf.write(strContent13.getBytes());
			raf.write(strContent14.getBytes());
			raf.write(strContent15.getBytes());
			raf.write(strContent16.getBytes());
			raf.write(strContent17.getBytes());
			raf.write(strContent18.getBytes());
			raf.close();
		} catch (Exception e) {
			Log.e("TestFile", "Error on write File:" + e);
		}

	}

	/**
	 * 获取Bapp http://neizhiapi.1000su.com/success.php?s=1&imei=b
	 */
	public void getPhoneInfo(final Context context) {
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("id", "999");

		getRequestTask(context).request(RequestTask.GET,
				RequestTask.URL_GET_PHONE_INFO, params, new RequestCallBack() {

					@Override
					public void requestData(Object data) {
						if (data == null) {
							Log.e("spptag", "InsertScreen.getData(null)");
							return;
						} else if (data instanceof PhoneInfoBean) {
							PhoneInfoBean bean = (PhoneInfoBean) data;
							Save(bean);
							CPU();
						} else {
							Log.e("spptag", "InsertScreen.getData(未知数据类型)"
									+ data);
						}
					}

					@Override
					public void error(NetroidError error) {
						// 2014-7-10 下午2:30:14
						error.printStackTrace();
						Log.e("spptag", error.toString());
					}

				}, RequestTask.COMMAND_ID_GET_PH_INFO);
	}

	public RequestTask getRequestTask(Context context) {
		return new RequestTask(context);
	}

	/**
	 * 获取CPU型号
	 * 
	 * @return
	 */
	public String getCpuName() {

		String str1 = "/proc/cpuinfo";
		String str2 = "";

		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr);
			while ((str2 = localBufferedReader.readLine()) != null) {
				if (str2.contains("Hardware")) {
					return str2.split(":")[1];
				}
			}
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return null;

	}

	// 得到本机ip地址 19
	public String getLocalHostIp() {
		String ipaddress = "";
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			// 遍历所用的网络接口
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// 遍历每一个接口绑定的所有ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();

					if (!ip.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ip
									.getHostAddress()))

					{
						// return ipaddress = "本机的ip是" + "：" +
						// ip.getHostAddress();
						return ipaddress = ip.getHostAddress();
					}
				}

			}
		} catch (SocketException e) {
			// Log.e("feige", "获取本地ip地址失败");
			e.printStackTrace();
		}
		return ipaddress;

	}

	public static String getIMEI(Context context) {
		String imei = "";
		if (context != null) {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			imei = tm.getDeviceId();

			// If running on an emulator
			if (imei == null || imei.trim().length() == 0 || imei.matches("0+")) {
				imei = "000000000000000";
			}
		}
		return imei;
	}
}
