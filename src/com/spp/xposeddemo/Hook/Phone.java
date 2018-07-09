package com.spp.xposeddemo.Hook;

import android.os.Build;

import com.spp.xposeddemo.Utis.SharedPref;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class Phone {
	public Phone(XC_LoadPackage.LoadPackageParam sharePkgParam) {
		getType(sharePkgParam);
		Bluetooth(sharePkgParam);
		Wifi(sharePkgParam);
		Telephony(sharePkgParam);
	}

	// 联网方式
	public void getType(XC_LoadPackage.LoadPackageParam loadPackageParam) {
		int getType = SharedPref.getintXValue("getType");
		if (getType > 0)
			XposedHelpers.findAndHookMethod("android.net.NetworkInfo",
					loadPackageParam.classLoader, "getType",
					new XC_MethodHook() {
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							super.afterHookedMethod(param);
							param.setResult(SharedPref.getintXValue("getType"));

						};

					});

	}

	// ------- MAC 蓝牙-----------------------------------------------------------
	public void Bluetooth(XC_LoadPackage.LoadPackageParam loadPkgParam) {

		String LYMAC = SharedPref.getXValue("LYMAC");
		if (LYMAC == null | LYMAC.length() == 0)
			return;
		try {

			// 双层 MAC
			XposedHelpers.findAndHookMethod(
					"android.bluetooth.BluetoothAdapter",
					loadPkgParam.classLoader, "getAddress",
					new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);
							param.setResult(SharedPref.getXValue("LYMAC"));
						}

					});
			// 双层MAC
			XposedHelpers.findAndHookMethod(
					"android.bluetooth.BluetoothDevice",
					loadPkgParam.classLoader, "getAddress",
					new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							// super.afterHookedMethod(param);
							param.setResult(SharedPref.getXValue("LYMAC"));
						}

					});
		} catch (Exception e) {
			XposedBridge.log("phone MAC HOOK 失败 " + e.getMessage());
		}
	}

	// -----------------------------------------------------------------------------

	// WIF MAC
	public void Wifi(XC_LoadPackage.LoadPackageParam loadPkgParam) {
		try {
			String WifiMAC = SharedPref.getXValue("WifiMAC");
			if (WifiMAC != null && WifiMAC.length() > 0)
				XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo",
						loadPkgParam.classLoader, "getMacAddress",
						new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);
								param.setResult(SharedPref.getXValue("WifiMAC"));
							}

						});

			String getIP = SharedPref.getXValue("getIP");
			if (getIP != null && getIP.length() > 0)
				// 内网IP
				XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo",
						loadPkgParam.classLoader, "getIpAddress",
						new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);
								param.setResult(SharedPref.getXValue("getIP"));
								// param.setResult(tryParseInt(SharedPref.getXValue("getIP")));

							}

						});
			String WifiName = SharedPref.getXValue("WifiName");
			if (WifiName != null && WifiName.length() > 0)
				XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo",
						loadPkgParam.classLoader, "getSSID",
						new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);
								param.setResult(SharedPref
										.getXValue("WifiName"));
							}

						});

		} catch (Exception e) {

		}

		// ------------------------基站信息

		// 基站的信号强度
		String BSSID = SharedPref.getXValue("BSSID");
		if (BSSID != null && BSSID.length() > 0)
			XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo",
					loadPkgParam.classLoader, "getBSSID", new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);
							param.setResult(SharedPref.getXValue("BSSID"));
						}

					});

	}

	public void Telephony(XC_LoadPackage.LoadPackageParam loadPkgParam) {

		String TelePhone = "android.telephony.TelephonyManager";
		String IMEI = SharedPref.getXValue("IMEI");
		if (IMEI != null && IMEI.length() > 0) {
			try {
				XposedHelpers.findAndHookMethod(
						"android.telephony.TelephonyManager",
						loadPkgParam.classLoader, "getDeviceId",
						XC_MethodReplacement.returnConstant(IMEI));
				XposedHelpers.findAndHookMethod(
						"com.android.internal.telephony.PhoneSubInfo",
						loadPkgParam.classLoader, "getDeviceId",
						XC_MethodReplacement.returnConstant(IMEI));

				if (Build.VERSION.SDK_INT < 22) {
					XposedHelpers.findAndHookMethod(
							"com.android.internal.telephony.gsm.GSMPhone",
							loadPkgParam.classLoader, "getDeviceId",
							XC_MethodReplacement.returnConstant(IMEI));
					XposedHelpers.findAndHookMethod(
							"com.android.internal.telephony.PhoneProxy",
							loadPkgParam.classLoader, "getDeviceId",
							XC_MethodReplacement.returnConstant(IMEI));
				}
			} catch (Exception ex) {
				XposedBridge.log(" IMEI 错误: " + ex.getMessage());
			}
		}
		String deviceversion = SharedPref.getXValue("deviceversion");
		if (deviceversion != null && deviceversion.length() > 0)
			HookTelephony(TelePhone, loadPkgParam, "getDeviceSoftwareVersion",
					deviceversion);// 返系统版本
		String IMSI = SharedPref.getXValue("IMSI");
		if (IMSI != null && IMSI.length() > 0)
			HookTelephony(TelePhone, loadPkgParam, "getSubscriberId", IMSI);
		String PhoneNumber = SharedPref.getXValue("PhoneNumber");
		if (PhoneNumber != null && PhoneNumber.length() > 0)
			HookTelephony(TelePhone, loadPkgParam, "getLine1Number",
					PhoneNumber);
		String SimSerial = SharedPref.getXValue("SimSerial");
		if (SimSerial != null && SimSerial.length() > 0)
			HookTelephony(TelePhone, loadPkgParam, "getSimSerialNumber",
					SimSerial);
		String networktor = SharedPref.getXValue("networktor");
		if (networktor != null && networktor.length() > 0)
			HookTelephony(TelePhone, loadPkgParam, "getNetworkOperator",
					networktor); // 网络运营商类型
		String Carrier = SharedPref.getXValue("Carrier");
		if (Carrier != null && Carrier.length() > 0)
			HookTelephony(TelePhone, loadPkgParam, "getNetworkOperatorName",
					Carrier); // 网络类型名
		String CarrierCode = SharedPref.getXValue("CarrierCode");
		if (CarrierCode != null && CarrierCode.length() > 0)
			HookTelephony(TelePhone, loadPkgParam, "getSimOperator",
					CarrierCode); // 运营商
		String simopename = SharedPref.getXValue("simopename");
		if (simopename != null && simopename.length() > 0)
			HookTelephony(TelePhone, loadPkgParam, "getSimOperatorName",
					simopename); // 运营商名字
		String gjISO = SharedPref.getXValue("gjISO");
		if (gjISO != null && gjISO.length() > 0)
			HookTelephony(TelePhone, loadPkgParam, "getNetworkCountryIso",
					gjISO); // 国家iso代码
		String CountryCode = SharedPref.getXValue("CountryCode");
		if (CountryCode != null && CountryCode.length() > 0)
			HookTelephony(TelePhone, loadPkgParam, "getSimCountryIso",
					CountryCode); // 手机卡国家
		int networkType = SharedPref.getintXValue("networkType");
		if (networkType > 0)
			XposedHelpers.findAndHookMethod(
					"android.telephony.TelephonyManager",
					loadPkgParam.classLoader, "getNetworkType",
					new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);
							// 网络类型
							param.setResult(SharedPref
									.getintXValue("networkType"));

						}

					});
		int phonetype = SharedPref.getintXValue("phonetype");
		if (phonetype > 0)
			XposedHelpers.findAndHookMethod(
					"android.telephony.TelephonyManager",
					loadPkgParam.classLoader, "getPhoneType",
					new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);

							param.setResult(SharedPref
									.getintXValue("phonetype"));
						}
					});
		int SimState = SharedPref.getintXValue("SimState");
		if (SimState > 0)
			XposedHelpers.findAndHookMethod(
					"android.telephony.TelephonyManager",
					loadPkgParam.classLoader, "getSimState",
					new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);

							param.setResult(SharedPref.getintXValue("SimState"));
						}
					});

	}

	private void HookTelephony(String hookClass,
			XC_LoadPackage.LoadPackageParam loadPkgParam, String funcName,
			final String value) {
		try {
			XposedHelpers.findAndHookMethod(hookClass,
					loadPkgParam.classLoader, funcName, new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);
							param.setResult(value);
						}

					});

		} catch (Exception e) {

		}
	}

}