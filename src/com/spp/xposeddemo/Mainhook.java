package com.spp.xposeddemo;

import java.util.Arrays;

import android.util.Log;

import com.spp.xposeddemo.Hook.Cpuinfo;
import com.spp.xposeddemo.Hook.OpenGL;
import com.spp.xposeddemo.Hook.Phone;
import com.spp.xposeddemo.Hook.Resolution;
import com.spp.xposeddemo.Hook.XBuild;
import com.spp.xposeddemo.Utis.RootCloak;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class Mainhook implements IXposedHookLoadPackage {
	public static String[] MY_TEST_PACKAGES = { "com.example.androidtests",
			"com.android.browser", "com.android.browserone",
			"com.just.library.agentweb","com.anjian.gaiji" };

	@Override
	public void handleLoadPackage(
			XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
		Log.e("spptag", "=============运行中=============="
				+ loadPackageParam.packageName);
		XposedBridge.log("=============运行中=============="
				+ loadPackageParam.packageName);
		// if (loadPackageParam.packageName.equals("com.example.androidtest")
		// && MainActivity.ISHOCK) {
		if (Arrays.asList(MY_TEST_PACKAGES).contains(
				loadPackageParam.packageName)) {
			// new Hook().HookTest(loadPackageParam); // 动态生效 不用重启
			new RootCloak().handleLoadPackage(loadPackageParam);
			new XBuild(loadPackageParam); // build
			new Phone(loadPackageParam); // TelephonyManager
			new Resolution().Display(loadPackageParam); // 屏幕
			new OpenGL().OpenGLTest(loadPackageParam); // 显卡
			new Cpuinfo(loadPackageParam); // CPU*/

			/*
			 * GPS位置 只对百度高德生效 有需要的朋友可以添加 要更改位置应用的包名 不要作用于全局 某些机型可能不太好使 请自行适配 不难的
			 */
			// if (loadPackageParam.packageName.equals("com.baidu.BaiduMap")
			// || loadPackageParam.packageName
			// .equals("com.autonavi.minimap")) {
			//
			// GPShook.HookAndChange(loadPackageParam.classLoader,
			// SharedPref.getfloatXValue("lat"),
			// SharedPref.getfloatXValue("log"));
			//
			// }
		}
	}
}
