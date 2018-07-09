package com.spp.xposeddemo;

import java.io.File;

import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.duowan.mobile.netroid.Network;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.cache.DiskCache;
import com.duowan.mobile.netroid.stack.HttpClientStack;
import com.duowan.mobile.netroid.stack.HttpStack;
import com.duowan.mobile.netroid.stack.HurlStack;
import com.duowan.mobile.netroid.toolbox.BasicNetwork;

public class Netroid {
	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it.
	 * 
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context) {
		int poolSize = RequestQueue.DEFAULT_NETWORK_THREAD_POOL_SIZE;

		HttpStack stack;
		String userAgent = "netroid/0";
		try {
			String packageName = context.getPackageName();
			PackageInfo info = context.getPackageManager().getPackageInfo(
					packageName, 0);
			userAgent = packageName + "/" + info.versionCode;
		} catch (NameNotFoundException e) {
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			stack = new HurlStack(userAgent, null);
		} else {
			// Prior to Gingerbread, HttpUrlConnection was unreliable.
			// See:
			// http://android-developers.blogspot.com/2011/09/androids-http-clients.html
			stack = new HttpClientStack(userAgent);
		}

		Network network = new BasicNetwork(stack, HTTP.UTF_8);
		File diskCacheDir = new File(context.getCacheDir(), "netroid");
		int diskCacheSize = 50 * 1024 * 1024; // 50MB
		DiskCache diskCache = new DiskCache(diskCacheDir, diskCacheSize);
		RequestQueue queue = new RequestQueue(network, poolSize, diskCache);
		queue.start();

		return queue;
	}
}
