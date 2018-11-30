package com.ecology.view.seedland.circulate.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ecology.view.seedland.circulate.modle.bean.MailCountInfo;
import com.ecology.view.seedland.circulate.modle.net.BaseResponse;
import com.ecology.view.seedland.circulate.modle.net.HttpService;
import com.ecology.view.seedland.circulate.modle.net.ResponseHandler;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HeCh on 2017/3/13 0013.
 */
public class Utils {

    /**
     * 查找一个布局里的所有的按钮并设置点击事件
     *
     * @param rootView
     * @param listener
     */
    public static void findButtonAndSetOnClickListener(View rootView,
                                                       OnClickListener listener) {
        if (rootView instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) rootView;
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                // 如果是按钮设置点击事件
                if (child instanceof Button || child instanceof ImageButton) {
                    child.setOnClickListener(listener); // 设置点击事件
                }
                if (child instanceof ViewGroup) {
                    findButtonAndSetOnClickListener(child, listener);
                }
            }
        }
    }

    /**
     * 格式化时间显示，如果是当天的短信，则显示时分，否则显示日期
     *
     * @param date
     * @return
     */
    public static String formatDate(long date) {
        if (DateUtils.isToday(date)) {
            return DateFormat.format("yyyy-MM-dd hh:mm", date).toString();
        } else {
            return DateFormat.format("yyyy-MM-dd hh:mm", date).toString();
        }
    }

    public static String format(double countPrice) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        return format.format(countPrice);
    }


    /**
     * 匹配ip地址的正则表达式
     */
    private static final String IP_REGEXP =
            "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9" +
                    "])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\" +
                    ".(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\." +
                    "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])";

    /**
     * 替换一个url中的ip地址 <br/>
     * 例如：<br/>
     * url为： http://127.0.0.1:8080/jinquan/1.jpg <br/>
     * 调用 replaceIp(url, "192.168.1.1")后，<br/>
     * 则得到新的url为： http://192.168.1.1:8080/jinquan/1.jpg
     *
     * @param url
     * @param ip
     * @return
     */
    public static String replaceIp(String url, String ip) {
        // 匹配ip的正则表达式
        return url.replaceAll(IP_REGEXP, ip);
    }

    /*public static String replaceIp(String url) {
        // 匹配ip的正则表达式
        return url.replaceAll(IP_REGEXP, Const.HOST_IP);
    }*/

    /**
     * 是否是一个合法的ip地址
     *
     * @param ip 要检测的字符串
     * @return
     */
    public static boolean checkIp(String ip) {
        Pattern pattern = Pattern.compile(IP_REGEXP);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }


    /**
     * @param str
     * @return
     */
    public static boolean checkPhoneNum(String str) {
        String namePattern = "^1[0-9]\\d{9}$";
        boolean ret = false;
        if (str == null) {
            ret = false;
            return ret;
        }
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches()) {
            ret = false;
        } else {
            ret = true;
        }
        return ret;
    }


    //生成MD5
    public static String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象
            byte[] messageByte = message.getBytes("UTF-8");
            byte[] md5Byte = md.digest(messageByte);              // 获得MD5字节数组,16*8=128位
            md5 = bytesToHex(md5Byte);                            // 转换为16进制字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    // 二进制转十六进制
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }


    /**
     * 将Bitmap转换成文件
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm, String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }

    /**
     * file转byteArray
     *
     * @param filePath
     * @return
     */
    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    /**
     * 时间转换为时间戳
     *
     * @param timeStr 时间 例如: 2016-03-09
     * @param format  时间对应格式  例如: yyyy-MM-dd
     * @return
     */
    public static long getTimeStamp(String timeStr, String format) {
        if (TextUtils.isEmpty(timeStr)) {
            return 0;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
            long timeStamp = date.getTime();
            return timeStamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm分ss秒", Locale.getDefault());
        return dateFormat.format(d);
    }

    public static String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("######0.00");
        String format = df.format(price);
        return format;
    }




    /**
     * 对给定的字符串进行base64解码操作
     */
    public static String decodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.decode(inputData,Base64.DEFAULT));
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }

        return null;
    }


    public static <T> T parseJson(String json,Class<T> tClass) {
        T info = JSONObject.parseObject(json, tClass);
        return info;
    }

    public static <T>List<T> parseJsonArray(String json, Class<T> tClass) {
        List<T> ts = JSONArray.parseArray(json, tClass);
        return ts;
    }


    public static void displayImage(Context context, String url, int loadingImg, int errorImg,
                                    ImageView targetView) {
        Glide.with(context).load(url).placeholder(loadingImg).error(errorImg).into(targetView);
    }

    public static void displayDrawable(Context context, String url, int loadingImg, int errorImg,
                                       final ImageView targetView) {
        Glide.with(context).load(url).placeholder(loadingImg).error(errorImg).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                targetView.setImageDrawable(resource);
            }
        });
    }

    public static void getUnReadCount(final OnUnReadCountListener onUnReadCountListener) {
        HttpService.loadMailCount(new ResponseHandler() {
            @Override
            public void onError(String msg) {
                onUnReadCountListener.unReadCount(false,msg);
            }

            @Override
            public void onSuccess(String json, org.json.JSONObject jsonObject, BaseResponse response) {
                String data = jsonObject.optString("data");
                MailCountInfo mailCountInfo = parseJson(data, MailCountInfo.class);
                onUnReadCountListener.unReadCount(true,mailCountInfo.getUnreadCount());
            }
        });
    }

    public interface OnUnReadCountListener{
        void unReadCount(boolean status,String unreadCount);
    }

    /**
     * 判断字符串是不是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是中文
     * @param str
     * @return
     */
    public static boolean isChinese(String str){
//        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
//        Matcher m = p.matcher(str);
//        if(m.matches()){
//            return true;
//        }else {
//            return false;
//        }

        if (str == null)
            return false;
        for (char c : str.toCharArray()) {
            if (!isChinese(c))
                return false;// 有一个中文字符就返回
        }
        return true;
    }

    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

}











