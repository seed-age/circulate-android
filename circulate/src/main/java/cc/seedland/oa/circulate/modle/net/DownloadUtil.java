package cc.seedland.oa.circulate.modle.net;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/26 0026.
 */

public class DownloadUtil {
    public static final int DOWNLOAD_FAIL=0;
    public static final int DOWNLOAD_PROGRESS=1;
    public static final int DOWNLOAD_SUCCESS=2;
    public static final int DOWNLOAD_SPEED=3;
    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;
    public static DownloadUtil getInstance() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     *
     */
    public void download(final String url, final String saveDir, final String fileName, final OnDownloadListener listener){
        this.listener=listener;
        Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= Message.obtain();
                message.what=DOWNLOAD_FAIL;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is=null;
                byte[] buf=new byte[2048];
                int len=0;
                FileOutputStream fos=null;
                //储存下载文件的目录
                String savePath=isExistDir(saveDir);
                try{
                    is=response.body().byteStream();
                    long total=response.body().contentLength();
                    File file=new File(savePath,fileName);
                    fos=new FileOutputStream(file);
                    long sum=0;
                    long startTime = System.currentTimeMillis(); // 开始下载时获取开始时间
                    while((len = is.read(buf))!=-1){
                        fos.write(buf,0,len);
                        sum+=len;
                        int progress=(int)(sum*1.0f/total*100);

                        long curTime = System.currentTimeMillis();
                        int usedTime = (int) ((curTime-startTime)/1000);

                        if(usedTime==0)usedTime = 1;
                        float downloadSpeed = (sum/usedTime)/1024; // 下载速度
                        //下载中
                        Message message=Message.obtain();
                        message.what=DOWNLOAD_PROGRESS;
                        message.arg1=progress;
                        message.obj = downloadSpeed;
                        mHandler.sendMessage(message);

                    }
                    fos.flush();
                    //下载完成
                    Message message=Message.obtain();
                    message.what=DOWNLOAD_SUCCESS;
                    message.obj=file.getAbsolutePath();
                    mHandler.sendMessage(message);
                }catch (Exception e){
                    Message message=Message.obtain();
                    message.what=DOWNLOAD_FAIL;
                    mHandler.sendMessage(message);
                }finally{
                    try{
                        if(is!=null)
                            is.close();
                    }catch (IOException e){

                    }
                    try {
                        if(fos!=null){
                            fos.close();
                        }
                    }catch (IOException e){

                    }
                }
            }
        });
    }

    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/")+1);
    }


    private String isExistDir(String saveDir) throws IOException {
        File downloadFile=new File(saveDir);
        if(!downloadFile.mkdirs()){
            downloadFile.createNewFile();
        }
        String savePath=downloadFile.getAbsolutePath();
        return savePath;
    }




    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DOWNLOAD_PROGRESS:
                    listener.onDownloading(msg.arg1,(float)msg.obj);
                    break;
                case DOWNLOAD_FAIL:
                    listener.onDownloadFailed();
                    break;
                case DOWNLOAD_SUCCESS:
                    listener.onDownloadSuccess((String) msg.obj);
                    break;
                case DOWNLOAD_SPEED:

                    break;
            }
        }
    };


    OnDownloadListener listener;
    public interface OnDownloadListener{
        /**
         * 下载成功
         */
        void onDownloadSuccess(String path);
        /**
         * 下载进度
         * @param progress
         * @param speed
         */
        void onDownloading(int progress, float speed);
        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
