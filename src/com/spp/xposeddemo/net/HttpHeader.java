package com.spp.xposeddemo.net;

import java.util.Map;
import java.util.TreeMap;

import com.spp.xposeddemo.Utis.GetPhoneState;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class HttpHeader {

	private static TreeMap<String, String> httpHeaders = null;
	private static boolean isFirst;
	public static String ip = "";

	public static Map<String, String> getHeaders(Context con) {
		if (httpHeaders == null) {
			httpHeaders = new TreeMap<String, String>();
		}
		if (!isFirst) {

			isFirst = true;
			httpHeaders.put("Osversion", "" + GetPhoneState.getSysRelease());// 操作系统版本
			httpHeaders.put("SysLanguage", "" + GetPhoneState.getSysLanguage());// 系统语言
			httpHeaders.put("Brand", "" + GetPhoneState.getBrand());// 手机品牌
			httpHeaders.put("Model", "" + GetPhoneState.getModel());// 手机型号
			httpHeaders.put("IMSI", "" + GetPhoneState.readSimSerialNum(con));// sim卡序列号imsi
			httpHeaders.put("ICCID", "" + GetPhoneState.readSimICCID(con));// iccid
			// httpHeaders.put("Mac",
			// ""+GetPhoneState.getLocalMacAddress(con));// mac
			httpHeaders.put("Carrier", "" + GetPhoneState.getCarrier(con));// 运营商
			httpHeaders.put("NetworkType",
					"" + GetPhoneState.getAccessNetworkType(con));// 联网类型
			httpHeaders.put("hasIccCard", "" + GetPhoneState.hasIccCard(con));// ICC卡是否存在
			// httpHeaders.put("PhoneNumber",
			// ""+GetPhoneState.getNativePhoneNumber());// I手机号 可能为空
			WindowManager m = (WindowManager) con
					.getSystemService(Context.WINDOW_SERVICE);
			Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
			httpHeaders.put("Screenw", String.valueOf(d.getWidth()));
			httpHeaders.put("Screenh", String.valueOf(d.getHeight()));

			// Set<String> key = httpHeaders.keySet();
			// for (Iterator it = key.iterator(); it.hasNext();) {
			// String s = (String) it.next();
			// System.out.println("@@ "+s+"="+httpHeaders.get(s));
			// }

		}
		// UserEntityBean userBean = UserEntityBean.getInstance();
		// if (userBean.isValid())
		// {
		// MyLogPrinter.debugError("userBean.getUserid() = "+
		// userBean.getUserid());
		// // httpHeaders.put("Usersession", userBean.getUserSession());//
		// session
		// httpHeaders.put("uid", userBean.getUserid());
		// }
		return httpHeaders;
	}

}
