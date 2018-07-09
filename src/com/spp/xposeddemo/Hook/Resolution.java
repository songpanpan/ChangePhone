package com.spp.xposeddemo.Hook;

import android.util.DisplayMetrics;
import android.view.Display;

import com.spp.xposeddemo.Utis.SharedPref;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XCallback;


public class Resolution {
	/*
	 * 屏幕相关
	 */
	public void Display(XC_LoadPackage.LoadPackageParam loadPkgParam) {
		try {
			final int dpi = tryParseInt(SharedPref.getXValue("DPI"));
			if (dpi > 0)
				XposedHelpers.findAndHookMethod("android.view.Display",
						loadPkgParam.classLoader, "getMetrics",
						DisplayMetrics.class, new XC_MethodHook(
								XCallback.PRIORITY_LOWEST) {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);

								DisplayMetrics metrics = (DisplayMetrics) param.args[0];
								metrics.densityDpi = dpi;

							}

						});
		} catch (Exception e) {
			XposedBridge.log("Fake DPI ERROR: " + e.getMessage());
		}
		int xdpi = SharedPref.getintXValue("DPI");
		if (xdpi > 0)
			try {
				XposedHelpers.findAndHookMethod("android.view.Display",
						loadPkgParam.classLoader, "getRealMetrics",
						DisplayMetrics.class, new XC_MethodHook(
								XCallback.PRIORITY_LOWEST) {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);
								final int dpi = SharedPref.getintXValue("DPI");
								DisplayMetrics metrics = (DisplayMetrics) param.args[0];
								metrics.densityDpi = dpi;

							}

						});
			} catch (Exception e) {

			}

		float density = SharedPref.getfloatXValue("density");
		if (density > 0)
			try {
				XposedHelpers.findAndHookMethod("android.view.Display",
						loadPkgParam.classLoader, "getMetrics",
						DisplayMetrics.class, new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);
								final float sdensity = SharedPref
										.getfloatXValue("density");
								DisplayMetrics metrics = (DisplayMetrics) param.args[0];
								metrics.density = sdensity;

							}

						});
			} catch (Exception e) {

			}
		float mxdpi = SharedPref.getfloatXValue("xdpi");
		if (mxdpi > 0)
			try {
				XposedHelpers.findAndHookMethod("android.view.Display",
						loadPkgParam.classLoader, "getMetrics",
						DisplayMetrics.class, new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);
								final float sxdpi = SharedPref
										.getfloatXValue("xdpi");
								DisplayMetrics metrics = (DisplayMetrics) param.args[0];
								metrics.xdpi = sxdpi;

							}

						});
			} catch (Exception e) {
				XposedBridge.log("Fake Real DPI ERROR: " + e.getMessage());
			}

		float ydpi = SharedPref.getfloatXValue("ydpi");
		if (ydpi > 0)
			try {
				XposedHelpers.findAndHookMethod("android.view.Display",
						loadPkgParam.classLoader, "getMetrics",
						DisplayMetrics.class, new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);
								final float sydpi = SharedPref
										.getfloatXValue("ydpi");
								DisplayMetrics metrics = (DisplayMetrics) param.args[0];
								metrics.ydpi = sydpi;

							}

						});
			} catch (Exception e) {

			}
		float scaledDensity = SharedPref.getfloatXValue("scaledDensity");
		if (scaledDensity > 0)
			try {
				XposedHelpers.findAndHookMethod("android.view.Display",
						loadPkgParam.classLoader, "getMetrics",
						DisplayMetrics.class, new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);
								final float scdensity = SharedPref
										.getfloatXValue("scaledDensity");
								DisplayMetrics metrics = (DisplayMetrics) param.args[0];
								metrics.scaledDensity = scdensity;

							}

						});

			} catch (Exception e) {

			}

		int width = SharedPref.getintXValue("width");
		if (width > 0)
			// 已废弃的修改屏幕信息
			XposedHelpers.findAndHookMethod("android.view.Display",
					loadPkgParam.classLoader, "getWidth", new XC_MethodHook() {
						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);

							param.setResult(SharedPref.getintXValue("width"));
						}
					});
		int mheight = SharedPref.getintXValue("height");
		if (mheight > 0)
			XposedHelpers.findAndHookMethod("android.view.Display",
					loadPkgParam.classLoader, "getHeight", new XC_MethodHook() {
						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);

							param.setResult(SharedPref.getintXValue("height"));
						}
					});

		// 宽
		XposedHelpers.findAndHookMethod(Display.class, "getMetrics",
				DisplayMetrics.class, new XC_MethodHook(
						XCallback.PRIORITY_LOWEST) {
					protected void afterHookedMethod(MethodHookParam param)
							throws Throwable {
						final int zhenwidth = SharedPref.getintXValue("width");
						DisplayMetrics metrics = (DisplayMetrics) param.args[0];
						metrics.widthPixels = zhenwidth;

					}
				});
		int height = SharedPref.getintXValue("height");
		if (height > 0)
			// 高
			XposedHelpers.findAndHookMethod(Display.class, "getMetrics",
					DisplayMetrics.class, new XC_MethodHook(
							XCallback.PRIORITY_LOWEST) {
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							final int zhenheight = SharedPref
									.getintXValue("height");
							DisplayMetrics metrics = (DisplayMetrics) param.args[0];
							metrics.heightPixels = zhenheight;

						}
					});

	}

	private static int tryParseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 320;
		}
	}

	private static float tryParsefloat(String s) {
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return (float) 480.0;
		}
	}

}
