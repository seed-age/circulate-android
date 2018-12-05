package cc.seedland.oa.circulate.modle.net;

import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class NetManager {
    public static final int DEFAULT_MILLISECONDS = 60000;       //默认的超时时间
    private static NetManager mNetManager;
    private final OkHttpClient.Builder mHttpClientBuilder;
    private final OkHttpClient client;
    private HttpParams params = new HttpParams();

    private NetManager() {
        mHttpClientBuilder = new OkHttpClient.Builder();
        mHttpClientBuilder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        mHttpClientBuilder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        mHttpClientBuilder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        mHttpClientBuilder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        client = mHttpClientBuilder.build();
    }

    public static NetManager getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，sPayUtils不等于null时，直接返回对象，提高运行效率）
        if (mNetManager == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (NetManager.class) {
                //未初始化，则初始instance变量
                if (mNetManager == null) {
                    mNetManager = new NetManager();
                }
            }
        }
        return mNetManager;
    }

    public void getRequest(final BaseRequest req) {
        Request request = new Request.Builder()
                .get()
                .url(req.getUrl())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Global.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        req.getHandler().onError(e.getMessage(), "500");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
//                final RESPONSE result = req.parseJson(json);
                Global.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        parseJson(req, json);
                    }
                });
            }
        });
    }


    public void postRequest(final BaseRequest req) {
        RequestBody requestBody = HttpUtils.generateMultipartRequestBody(params);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(req.getUrl())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Global.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        req.getHandler().onError(e.getMessage(), "500");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
//                final RESPONSE result = req.parseJson(json);
                Global.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        parseJson(req, json);
                    }
                });
            }
        });
    }

    private void parseJson(BaseRequest req, String json) {
        LogUtil.e("Api = " + req.getUrl());
        LogUtil.e("type = " + req.getType());
        LogUtil.eCut("json = " + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (!jsonObject.isNull("success")) {//json满足{"success": true,"code":"","msg": "",
                // "data": {}}的数据格式
                BaseResponse res = new BaseResponse();
                res.setSuccess(jsonObject.optBoolean("success"));
                res.setCode(jsonObject.optString("code"));
                res.setMsg(jsonObject.optString("msg"));
                int type = req.getType();
                if (type != 0)
                    res.setType(type);
                if (res.isSuccess()) {
                    req.getHandler().onSuccess(json, jsonObject, res);
                } else {
                    req.getHandler().onError(res.getMsg(),res.getCode());
//                    Global.showToast(res.getMsg());
                }
            } else { //json不满足{"success": true,"code":"","msg": "","data": {}}的数据格式
                req.getHandler().onSuccess(json, jsonObject, null);
            }
        } catch (JSONException e) {
            LogUtil.e(e.getMessage());
            req.getHandler().onError(e.getMessage(), "500");
        }
    }

    public void params(HttpParams params) {
        this.params = params;
    }

    /**
     * 取消所有请求
     */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
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
        HttpUtils.checkNotNull(client,"please call init OkHttpClient");
        return client;
    }
}
