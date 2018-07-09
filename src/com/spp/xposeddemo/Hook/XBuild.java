package com.spp.xposeddemo.Hook;

import java.lang.reflect.Member;
import java.util.jar.Attributes.Name;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;

import com.spp.xposeddemo.Utis.SharedPref;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class XBuild {

	public XBuild(XC_LoadPackage.LoadPackageParam sharePkgParam) {
		AndroidSerial(sharePkgParam);
		BaseBand(sharePkgParam);
		BuildProp(sharePkgParam);
	}

	public void AndroidSerial(XC_LoadPackage.LoadPackageParam loadPkgParam) {
		try {
			Class<?> classBuild = XposedHelpers.findClass("android.os.Build",
					loadPkgParam.classLoader);
			String serial = SharedPref.getXValue("serial");
			if (serial != null && serial.length() > 0)
				XposedHelpers
						.setStaticObjectField(classBuild, "SERIAL", serial); // 串口序列号

			Class<?> classSysProp = Class
					.forName("android.os.SystemProperties");
			String getBaseband = SharedPref.getXValue("getBaseband");
			if (getBaseband != null && getBaseband.length() > 0)
				XposedHelpers.findAndHookMethod(classSysProp, "get",
						String.class, new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);
								String serialno = (String) param.args[0];

								if (serialno.equals("gsm.version.baseband")
										|| serialno.equals("no message")) {
									param.setResult(SharedPref
											.getXValue("getBaseband"));
								}
							}

						});
			if (getBaseband != null && getBaseband.length() > 0)
				XposedHelpers.findAndHookMethod(classSysProp, "get",
						String.class, String.class, new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);

								String serialno = (String) param.args[0];
								if (serialno.equals("gsm.version.baseband")
										|| serialno.equals("no message")) {
									param.setResult(SharedPref
											.getXValue("getBaseband"));
								}
							}

						});
			return;
		} catch (Exception ex) {
			XposedBridge.log(" AndroidSerial 错误: " + ex.getMessage());
		}
	}

	public void BaseBand(XC_LoadPackage.LoadPackageParam loadPkgParam) {
		try {
			String BaseBand = SharedPref.getXValue("BaseBand");
			if (BaseBand != null && BaseBand.length() > 0)
				XposedHelpers.findAndHookMethod("android.os.Build",
						loadPkgParam.classLoader, "getRadioVersion",
						new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// 固件版本
								param.setResult(SharedPref
										.getXValue("BaseBand"));
							}

						});

		} catch (Exception e) {
			XposedBridge.log(" BaseBand 错误: " + e.getMessage());
		}

	}

	public void BuildProp(XC_LoadPackage.LoadPackageParam loadPkgParam) {
		try {
			String board = SharedPref.getXValue("board");
			if (board != null && board.length() > 0)
				XposedHelpers.findField(Build.class, "BOARD").set(null, board);
			String brand = SharedPref.getXValue("brand");
			if (brand != null && brand.length() > 0)
				XposedHelpers.findField(Build.class, "BRAND").set(null, brand);
			String ABI = SharedPref.getXValue("ABI");
			if (ABI != null && ABI.length() > 0)
				XposedHelpers.findField(Build.class, "CPU_ABI").set(null, ABI);
			String ABI2 = SharedPref.getXValue("ABI2");
			if (ABI2 != null && ABI2.length() > 0)
				XposedHelpers.findField(Build.class, "CPU_ABI2")
						.set(null, ABI2);
			String device = SharedPref.getXValue("device");
			if (device != null && device.length() > 0)
				XposedHelpers.findField(Build.class, "DEVICE").set(null,
						SharedPref.getXValue("device"));
			String display = SharedPref.getXValue("display");
			if (display != null && display.length() > 0)
				XposedHelpers.findField(Build.class, "DISPLAY").set(null,
						display);
			String fingerprint = SharedPref.getXValue("fingerprint");
			if (fingerprint != null && fingerprint.length() > 0)
				XposedHelpers.findField(Build.class, "FINGERPRINT").set(null,
						fingerprint);
			String NAME = SharedPref.getXValue("NAME");
			if (NAME != null && NAME.length() > 0)
				XposedHelpers.findField(Build.class, "HARDWARE")
						.set(null, NAME);
			String ID = SharedPref.getXValue("ID");
			if (ID != null && ID.length() > 0)
				XposedHelpers.findField(Build.class, "ID").set(null, ID);
			String Manufacture = SharedPref.getXValue("Manufacture");
			if (Manufacture != null && Manufacture.length() > 0)
				XposedHelpers.findField(Build.class, "MANUFACTURER").set(null,
						Manufacture);
			String model = SharedPref.getXValue("model");
			if (model != null && model.length() > 0)
				XposedHelpers.findField(Build.class, "MODEL").set(null, model);
			String product = SharedPref.getXValue("product");
			if (product != null && product.length() > 0)
				XposedHelpers.findField(Build.class, "PRODUCT").set(null,
						product);
			String booltloader = SharedPref.getXValue("booltloader");
			if (booltloader != null && booltloader.length() > 0)
				XposedHelpers.findField(Build.class, "BOOTLOADER").set(null,
						booltloader); // 主板引导程序
			String host = SharedPref.getXValue("host");
			if (host != null && host.length() > 0)
				XposedHelpers.findField(Build.class, "HOST").set(null,
						SharedPref.getXValue("host")); // 设备主机地址
			String build_tags = SharedPref.getXValue("build_tags");
			if (build_tags != null && build_tags.length() > 0)
				XposedHelpers.findField(Build.class, "TAGS").set(null,
						SharedPref.getXValue("build_tags")); // 描述build的标签
			String shenbei_type = SharedPref.getXValue("shenbei_type");
			if (shenbei_type != null && shenbei_type.length() > 0)
				XposedHelpers.findField(Build.class, "TYPE").set(null,
						shenbei_type); // 设备版本类型
			String incrementalincremental = SharedPref
					.getXValue("incrementalincremental");
			if (incrementalincremental != null
					&& incrementalincremental.length() > 0)
				XposedHelpers.findField(Build.VERSION.class, "INCREMENTAL")
						.set(null, incrementalincremental); // 源码控制版本号
			String AndroidVer = SharedPref.getXValue("AndroidVer");
			if (AndroidVer != null && AndroidVer.length() > 0)
				XposedHelpers.findField(android.os.Build.VERSION.class,
						"RELEASE").set(null, AndroidVer);
			String API = SharedPref.getXValue("API");
			if (API != null && API.length() > 0)
				XposedHelpers.findField(android.os.Build.VERSION.class, "SDK")
						.set(null, SharedPref.getXValue("API"));
			XposedHelpers.findField(android.os.Build.VERSION.class, "CODENAME")
					.set(null, "REL"); // 写死就行 这个值为固定
			long time = SharedPref.getlongXValue("time");
			if (time > 0)
				XposedHelpers.findField(Build.class, "TIME").set(null, time); // 固件时间build

			// XposedHelpers.findField(Build.VERSION.class, "SDK_INT").set(null,
			// pre.getInt("sdkint", 6));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			XposedBridge.log(" BuilProp 错误: " + e.getMessage());
		}

		try {
			String AndroidID = SharedPref.getXValue("AndroidID");
			if (AndroidID != null && AndroidID.length() > 0)
				XposedHelpers.findAndHookMethod(
						"android.provider.Settings.Secure",
						loadPkgParam.classLoader, "getString",
						ContentResolver.class, String.class,
						new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {

								if (param.args[1]
										.equals(Settings.Secure.ANDROID_ID)) {
									param.setResult(SharedPref
											.getXValue("AndroidID"));
								}
							}
						});

		} catch (Exception ex) {
			XposedBridge.log(" Android ID 错误: " + ex.getMessage());
		}

		try {
			Class<?> cls = Class.forName("android.os.SystemProperties");
			if (cls != null) {
				for (Member mem : cls.getDeclaredMethods()) {
					XposedBridge.hookMethod(mem, new XC_MethodHook() {

						@Override
						protected void beforeHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.beforeHookedMethod(param);
							String DESCRIPTION = SharedPref
									.getXValue("DESCRIPTION");
							if (DESCRIPTION != null && DESCRIPTION.length() > 0)
								// 用户的KEY
								if (param.args.length > 0
										&& param.args[0] != null
										&& param.args[0]
												.equals("ro.build.description")) {
									param.setResult(DESCRIPTION);
								}
						}
					});
				}
			}

		} catch (ClassNotFoundException e) {
			XposedBridge.log(" DESCRIPTION 错误: " + e.getMessage());
		}
	}
}
