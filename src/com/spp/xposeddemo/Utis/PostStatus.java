package com.spp.xposeddemo.Utis;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class PostStatus {
	public interface StatusBack {
		public void Success(String result);

		public void Fail(String result);
	}

	/**
	 * 
	 * @param code
	 * @param appchannel
	 * @param other
	 * @param back
	 */
	public void PostPayStatus(final String url, final StatusBack back) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpGet get = new HttpGet(url);
				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
				client.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 10000);

				try {
					HttpResponse response = client.execute(get);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						String strResult = EntityUtils.toString(response
								.getEntity());
						back.Success(strResult);
					} else {
						back.Fail("fail");
					}
					back.Success(response.toString());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					back.Fail("error");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					back.Fail("error");
				}
			}
		}).start();
	}
}
