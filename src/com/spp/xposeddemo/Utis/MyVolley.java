/**
 * Copyright 2013 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spp.xposeddemo.Utis;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.Network;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.cache.BitmapImageCache;
import com.duowan.mobile.netroid.cache.DiskCache;
import com.duowan.mobile.netroid.image.NetworkImageView;
import com.duowan.mobile.netroid.request.StringRequest;
import com.duowan.mobile.netroid.stack.HurlStack;
import com.duowan.mobile.netroid.toolbox.BasicNetwork;
import com.duowan.mobile.netroid.toolbox.FileDownloader;
import com.duowan.mobile.netroid.toolbox.ImageLoader;
import com.duowan.mobile.netroid.toolbox.ImageLoader.ImageContainer;
import com.duowan.mobile.netroid.toolbox.ImageLoader.ImageListener;

/**
 * Helper class that is used to provide references to initialized
 * RequestQueue(s) and ImageLoader(s)
 * 
 * @author Ognyan Bankov
 * 
 */
public class MyVolley {
	// private static RequestQueue mRequestQueue;
	// private static ImageLoader mImageLoader;
	//
	//
	// private MyVolley() {
	// // no instances
	// }
	//
	//
	// static void init(Context context) {
	// mRequestQueue = Volley.newRequestQueue(context);
	//
	// int memClass = ((ActivityManager)
	// context.getSystemService(Context.ACTIVITY_SERVICE))
	// .getMemoryClass();
	// // Use 1/8th of the available memory for this memory cache.
	// int cacheSize = 1024 * 1024 * memClass / 8;
	// mImageLoader = new ImageLoader(mRequestQueue, new
	// BitmapLruCache(cacheSize));
	// }
	//
	//
	// public static RequestQueue getRequestQueue() {
	// if (mRequestQueue != null) {
	// return mRequestQueue;
	// } else {
	// throw new IllegalStateException("RequestQueue not initialized");
	// }
	// }
	//
	//
	// /**
	// * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	// which effectively means
	// * that no memory caching is used. This is useful for images that you know
	// that will be show
	// * only once.
	// *
	// * @return
	// */
	// public static ImageLoader getImageLoader() {
	// if (mImageLoader != null) {
	// return mImageLoader;
	// } else {
	// throw new IllegalStateException("ImageLoader not initialized");
	// }
	// }

	// Netroid入口，私有该实例，提供方法对外服务。
	private static RequestQueue mRequestQueue;

	// 图片加载管理器，私有该实例，提供方法对外服务。
	private static ImageLoader mImageLoader;

	// 文件下载管理器，私有该实例，提供方法对外服务。
	private static FileDownloader mFileDownloader;

	private static Context ctx;

	private MyVolley() {
	};

	public static String getSysSavefilePath(Context context, String url) {
		String cachePath = "";
		String uniqueName = "/appdata3";
		// if (Environment.MEDIA_MOUNTED.equals(Environment
		// .getExternalStorageState())) {
		// String sdcardFile = System.getenv("EXTERNAL_STORAGE");
		// // cachePath = Environment.getExternalStorageDirectory().getPath()
		// // + uniqueName;
		// cachePath = sdcardFile + uniqueName;
		// } else {
		// String sdcardFile = System.getenv("EXTERNAL_STORAGE");
		// // cachePath = ctx.getFilesDir().getPath() + uniqueName;
		// cachePath = sdcardFile + uniqueName;
		// }
		cachePath = Environment.getExternalStorageDirectory().getPath()
				+ uniqueName;
		File dir = new File(cachePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (url.equals(""))
			return cachePath;
		String fileName = "";
		try {
			fileName = url.substring(url.lastIndexOf('/'));

		} catch (Exception e) {
			// e.printStackTrace();
		}
		return cachePath + fileName;
	}

	public static void init(Context ctx) {
		if (mRequestQueue != null) {
			// throw new IllegalStateException("initialized");
			// mRequestQueue.stop();
			return;
		}

		MyVolley.ctx = ctx;
		// 创建Netroid主类，指定硬盘缓存方案
		Network network = new BasicNetwork(
				new HurlStack(Const.USER_AGENT, null), HTTP.UTF_8);
		mRequestQueue = new RequestQueue(network, 4, new DiskCache(new File(
				ctx.getCacheDir(), Const.HTTP_DISK_CACHE_DIR_NAME),
				Const.HTTP_DISK_CACHE_SIZE));
		mFileDownloader = new FileDownloader(mRequestQueue, 1);
		mRequestQueue.start();
	}

	// 示例做法：执行自定义请求以获得书籍列表
	@SuppressLint("InlinedApi")
	public static void getNetString(String url, Listener<String> listener) {
		StringRequest sr = new StringRequest(url, listener);
		sr.setCacheExpireTime(TimeUnit.DAYS, 1);
		mRequestQueue.add(sr);
	}

	// 加载单张图片
	public static void displayImage(String url, ImageView imageView,
			ImageListener listener) {
		if (url != null && !url.equals(""))
			mImageLoader.get(url, listener, 0, 0);
	}

	// 批量加载图片
	public static void displayImage(String url, NetworkImageView imageView) {
		imageView.setImageUrl(url, mImageLoader);
	}

	// 执行文件下载请求
	public static FileDownloader.DownloadController addFileDownload(
			String storeFilePath, String url, Listener<Void> listener) {
		return mFileDownloader.add(storeFilePath, url, listener);
	}

	// 执行文件下载请求
	public static FileDownloader.DownloadController addFileDownload(String url,
			Listener<Void> listener) {

		return mFileDownloader.add(getSavefilePath(ctx, url), url, listener);
	}

	public static String getSavefilePath(Context context) {

		return getSavefilePath(context, "");
	}

	public static String getSavefilePath(Context context, String url) {
		String cachePath = "";
		String uniqueName = "/appdata3";
		// if (Environment.MEDIA_MOUNTED.equals(Environment
		// .getExternalStorageState())) {
		cachePath = Environment.getExternalStorageDirectory().getPath()
				+ uniqueName;
		// } else {
		// cachePath = ctx.getFilesDir().getPath() + uniqueName;
		// }
		File dir = new File(cachePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (url.equals(""))
			return cachePath;
		String fileName = "";
		try {
			fileName = url.substring(url.lastIndexOf('/'));

		} catch (Exception e) {
			// e.printStackTrace();
		}
		return cachePath + fileName;
	}

	public class Const {
		// http parameters
		public static final int HTTP_MEMORY_CACHE_SIZE = 2 * 1024 * 1024; // 2MB
		public static final int HTTP_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
		public static final String HTTP_DISK_CACHE_DIR_NAME = "netroid";
		public static final String USER_AGENT = "netroid.cn";
	}
}