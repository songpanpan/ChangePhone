package com.spp.xposeddemo.Utis;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class PhoneInfoParser {
	public Object parse(Context application, String data) throws JSONException {
		PhoneInfoBean bean = new PhoneInfoBean();
		Log.e("spptag", "---------data---------" + data);
		try {
			JSONObject jsonObject = new JSONObject(data);
			bean.ARCH = jsonObject.optString("ARCH");
			bean.BRAND = jsonObject.optString("BRAND");
			bean.Build_VERSION_SDK_INT = jsonObject
					.optString("Build_VERSION_SDK_INT");
			bean.DEVICE = jsonObject.getString("DEVICE");
			bean.DISPLAY = jsonObject.getString("DISPLAY");
			bean.FINGERPRINT = jsonObject.getString("FINGERPRINT");
			bean.HARDWARE = jsonObject.getString("HARDWARE");
			bean.HOST = jsonObject.getString("HOST");
			bean.PHONEID = jsonObject.getString("HOST");
			bean.MANUFACTURER = jsonObject.getString("MANUFACTURER");
			bean.MODEL = jsonObject.getString("MODEL");
			bean.PRODUCT = jsonObject.getString("PRODUCT");
			bean.RELEASE = jsonObject.getString("MYRELEASE");
			bean.SDK = jsonObject.getString("SDK");
			bean.SERIAL = jsonObject.getString("SERIAL");
			bean.TAGS = jsonObject.getString("TAGS");
			bean.TYPE = jsonObject.getString("TYPE");
			bean.USER = jsonObject.getString("USER");
			bean.build_board = jsonObject.getString("build_board");
			bean.build_bootloader = jsonObject.getString("build_bootloader");
			bean.build_cpuabi = jsonObject.getString("build_cpuabi");
			bean.build_cpuabi2 = jsonObject.getString("build_cpuabi2");
			bean.build_time = jsonObject.getString("build_time");
			bean.connect_mode = jsonObject.getString("connect_mode");
			bean.density = jsonObject.getString("density");
			bean.densityDpi = jsonObject.getString("densityDpi");
			bean.get = jsonObject.getString("get");
			bean.getAddress = jsonObject.getString("getAddress");
			bean.getBSSID = jsonObject.getString("getBSSID");
			bean.getDeviceId = jsonObject.getString("getDeviceId");
			bean.getIpAddress = jsonObject.getString("getIpAddress");
			bean.getMacAddress = jsonObject.getString("getMacAddress");
			bean.getMetrics = jsonObject.getString("getMetrics");

			bean.getNetworkType = jsonObject.getString("getNetworkType");
			bean.getPhoneType = jsonObject.getString("getPhoneType");
			bean.getRadioVersion = jsonObject.getString("getRadioVersion");
			bean.getSSID = jsonObject.getString("getSSID");
			bean.getSimCountryIso = jsonObject.getString("getSimCountryIso");
			bean.getNetworkCountryIso = "cn";
			// 如果不是中国卡，按照比例变换成移动联通，电信。46000：CMCC 46001：中国联通 46002：CMCC
			// 46003：中国电信 46007：CMCC 46011：中国电信
			if (!bean.getSimCountryIso.equals("cn")) {
				bean.getSimCountryIso = "cn";
				Random random = new Random();
				int index = random.nextInt(100) + 1;
				if (index < 44) {
					bean.getNetworkOperator = "46000";
					bean.getNetworkOperatorName = "中国移动";
					bean.getSimOperator = "46000";
					bean.getSimOperatorName = "中国移动";
				} else if (index < 59) {
					bean.getNetworkOperator = "46001";
					bean.getNetworkOperatorName = "中国联通";
					bean.getSimOperator = "46001";
					bean.getSimOperatorName = "中国联通";
				} else if (index < 83) {
					bean.getNetworkOperator = "46002";
					bean.getNetworkOperatorName = "CMCC";
					bean.getSimOperator = "46002";
					bean.getSimOperatorName = "CMCC";
				} else if (index < 91) {
					bean.getNetworkOperator = "46003";
					bean.getNetworkOperatorName = "中国电信";
					bean.getSimOperator = "46003";
					bean.getSimOperatorName = "中国电信";
				} else if (index < 97) {
					bean.getNetworkOperator = "46007";
					bean.getNetworkOperatorName = "CMCC";
					bean.getSimOperator = "46007";
					bean.getSimOperatorName = "CMCC";
				} else {
					bean.getNetworkOperator = "46011";
					bean.getNetworkOperatorName = "中国电信";
					bean.getSimOperator = "46011";
					bean.getSimOperatorName = "中国电信";
				}
			} else {
				bean.getSimOperator = jsonObject.getString("getSimOperator");
				bean.getSimOperatorName = jsonObject
						.getString("getSimOperatorName");

				bean.getNetworkOperator = jsonObject
						.getString("getNetworkOperator");
				bean.getNetworkOperatorName = jsonObject
						.getString("getNetworkOperatorName");
			}

			bean.getSimSerialNumber = jsonObject
					.getString("getSimSerialNumber");
			bean.getSimState = jsonObject.getString("getSimState");
			bean.getString = jsonObject.getString("getString");
			bean.getSubtypeName = jsonObject.getString("getSubtypeName");
			bean.phone_deviceversion = jsonObject
					.getString("phone_deviceversion");
			bean.scaledDensity = jsonObject.getString("scaledDensity");
			bean.setCpuName = jsonObject.getString("setCpuName");
			bean.ID = jsonObject.getString("ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

}
