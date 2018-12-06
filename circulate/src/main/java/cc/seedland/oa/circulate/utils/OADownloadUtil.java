package cc.seedland.oa.circulate.utils;

import android.text.TextUtils;

import cc.seedland.oa.circulate.modle.net.HttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OADownloadUtil {
    private static OADownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static OADownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new OADownloadUtil();
        }
        return downloadUtil;
    }

    private OADownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param url 下载连接
     * @param saveDir 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String saveDir,final String fileName, final OnDownloadListener listener) {
//        String itemSum = getValueByName(url, "itemSum");
//        long sum = -1;
//        if (!TextUtils.isEmpty(itemSum)) {
//            try {
//                sum = Long.valueOf(itemSum);
//            }catch (ClassCastException e) {
//                e.printStackTrace();
//            }
//        }
        Request request = new Request.Builder().tag(url).url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                File saveFolder = new File(saveDir);
                if (!saveFolder.exists()) {
                    saveFolder.mkdirs();
                }
                try {
                    is = response.body().byteStream();
                    long total = -1;
                    String sumStr = response.request().url().queryParameter("itemSum");
                    try {
                        total = Long.valueOf(sumStr);
                    }catch (ClassCastException e) {
                        e.printStackTrace();
                    }
//                    long total = response.body().contentLength();
//                    int total = response.body().bytes().length;
                    LogUtil.e("total = " + total);
                    File file = new File(saveDir, fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    long startTime = System.currentTimeMillis(); // 开始下载时获取开始时间
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
//                        LogUtil.e("sum = " + sum);
                        int progress =  (int) (sum * 1.0f / total * 100);
                        LogUtil.e("progress = " + progress);
                        long curTime = System.currentTimeMillis();
                        int usedTime = (int) ((curTime-startTime)/1000);

                        if(usedTime==0)usedTime = 1;
                        float downloadSpeed = (sum/usedTime)/2048; // 下载速度
                        // 下载中
                        listener.onDownloading(progress,downloadSpeed);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    LogUtil.e(e.getMessage());
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }


    /***
     * 获取url 指定name的value;
     * @param url
     * @param name
     * @return
     */
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }

    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public OkHttpClient getOkHttpClient() {
        HttpUtils.checkNotNull(okHttpClient,"please call init OkHttpClient");
        return okHttpClient;
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         * @param speed
         */
        void onDownloading(int progress, float speed);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
