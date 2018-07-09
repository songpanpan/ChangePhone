package com.spp.xposeddemo.net;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.duowan.mobile.netroid.AuthFailureError;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.Request;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.request.StringRequest;
import com.spp.xposeddemo.Netroid;
import com.spp.xposeddemo.Utis.PhoneInfoParser;

public class RequestTask {
	public static int GET = Request.Method.GET;
	public static int POST = Request.Method.POST;
	public static final int COMMAND_ID_GET_PH_INFO = 1;

	public static String URL_GET_PHONE_INFO = "你的服务器连接，获取的json数据可查看json.txt";

	// =====EXIT
	private Context context;

	public RequestTask(Context context) {
		this.context = context;
	}

	public void request(Object... params) {
		/*
		 * final int type,final String url, final HashMap<String, String>
		 * params0,final RequestCallBack requestCallBack
		 */
		int index = 0;
		int httpType = (Integer) params[index++];
		String url = (String) params[index++];
		@SuppressWarnings("unchecked")
		TreeMap<String, String> tempmayMap = (TreeMap<String, String>) params[index++];
		if (tempmayMap == null) {
			tempmayMap = new TreeMap<String, String>();
		}
		final TreeMap<String, String> args = tempmayMap;
		final RequestCallBack requestCallBack = (RequestCallBack) params[index++];
		final int commandid = (Integer) params[index++];
		final StringBuffer requestUrl = new StringBuffer();

		RequestQueue queue = Netroid.newRequestQueue(context);
		try {
			// args.put(HTTP_KEY_VERSION, Tools.VER);
			// args.put(HTTP_KEY_VERSION_P, Tools.VER_P);
			// args.put(HTTP_KEY_IMEI, Tools.getIMEI(context));
			// args.put(HTTP_KEY_APPKEY, Tools.getAppkey(context));
			// args.put(HTTP_KEY_MID, Tools.getMID(context));
			Iterator<String> iterator = args.keySet().iterator();
			String key;
			while (iterator.hasNext()) {
				key = iterator.next();
				key = key.trim();
				// requestUrl.append(key + "="+ URLEncoder.encode(args.get(key),
				// HTTP.UTF_8)+ "&");
				requestUrl.append(key + "=" + args.get(key) + "&");
			}
			if (requestUrl.length() != 0) {
				requestUrl.deleteCharAt(requestUrl.length() - 1);
			}
			if (httpType == GET) {
				url = url + "?" + requestUrl.toString().trim();
			}
			StringRequest myReq = new StringRequest(httpType, url,
					new Listener<String>() {

						@Override
						public void onSuccess(String response) {
							// 2014-7-6 下午5:53:53
							if (response == null) {
								if (requestCallBack != null) {
									requestCallBack.error(new NetroidError(
											"data is null"));
									return;
								}
							}
							Object object = null;
							try {
								switch (commandid) {
								case COMMAND_ID_GET_PH_INFO:
									if (response.startsWith("200|"))
										response = response.substring(4);
									object = new PhoneInfoParser().parse(
											context, response);
									break;
								}
							} catch (JSONException e) {
								// e.printStackTrace();
							}
							if (requestCallBack != null)
								requestCallBack.requestData(object);
						}

						@Override
						public void onError(NetroidError error) {
							// 2014-7-6 下午5:54:46
							if (requestCallBack != null)
								requestCallBack.error(error);
						}
					}) {
				@Override
				public Map<String, String> getParams() throws AuthFailureError {
					// 2014-6-20 上午11:45:05
					Log.e("spptag", "args:" + args);
					return args;
				}

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					// 2014-7-4 下午3:11:52
					Map<String, String> map = HttpHeader.getHeaders(context);
					return map;
				}
			};
			// myReq.setForceUpdate(true);
			/*
			 * 获取config接口缓存10s 其他还是5分钟
			 * 
			 * 2014-11-05 上报的接口不能缓存 其他接口缓存五分钟
			 */
			int SECONDS = 1;
			myReq.setCacheExpireTime(TimeUnit.SECONDS, SECONDS);
			queue.add(myReq);

			/*
			 * 加入统计
			 */
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	public interface RequestCallBack {
		public void requestData(Object data);

		public void error(NetroidError error);
	}
}
