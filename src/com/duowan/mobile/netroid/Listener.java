package com.duowan.mobile.netroid;

/**
 * Callback interface for delivering request status or response result.
 * Note : all method are calls over UI thread.
 * @param <T> Parsed type of this response
 */
public abstract class Listener<T> {
	 // 在请求开始执行时回调，可做一些例如弹出ProgressDialog等等的UI操作来通知用户进行等待。
    // 由于请求是异步线程操作，在队列过长的情况下无法保证这个方法在addRequest()后立即被调用。
    public void onPreExecute() {}

    // 无论请求的执行情况是成功还是失败，这个方法都会在执行完成时回调。
    // 如果在onPreExecute()中做了一些UI提醒，建议在这个方法中取消。
    public void onFinish() {}

    // 在请求成功时回调
    public abstract void onSuccess(T response);

    // 在请求失败时回调
    public void onError(NetroidError error) {}

    // 在请求真正被取消时回调。注：Request.cancel() 方法只是做取消的标记，
    // 请求不会马上被终止，这个方法是在请求真正由于取消而终止时的回调通知。
    public void onCancel() {}

    // 如果请求选择使用缓存，但缓存不存在或过期时回调。这个方法相对于onUsedCache(), 意味着Netroid将会执行http操作。
    // 场景解释：在缓存可用的情况下，onPreExecute()跟onFinish()的回调间隔时间非常短，如果在onPreExecute()
    // 内弹出ProgressDialog来通知用户等待，可能会出现Dialog一闪而过的问题，因此提供这个回调方法作为替代。
    // 这个方法的回调同时意味着请求将会由于执行http操作而有相对长时间的等待（视网络情况而定）。
    public void onNetworking() {}

    // 如果请求设置了使用Cache，Netroid在获取到可用缓存时会回调这个方法。
    // 这个方法的回调意味着请求很快会使用缓存数据返回，不再执行实际的Http操作。
    public void onUsedCache() {}

    // 这个方法用于在请求超时需要重试前回调，可对重试次数进行统计。
    // 提示：可通过请求facebook等等被墙的网站来测试其可用性。
    public void onRetry() {}

    // 这个回调方法提供给文件下载功能使用，用于计算下载进度
    public void onProgressChange(long fileSize, long downloadedSize) {}
}
