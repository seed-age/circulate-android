package com.ecology.view.seedland.circulate.modle.net;

/**
 * Created by hch on 2018/1/13.
 */

public class BaseRequest<R extends BaseRequest> {
    private int type;
    private String url;
    private ResponseHandler mHandler;
    private HttpParams params;
    private static BaseRequest mRBaseRequest;


    public BaseRequest() {

    }

    public static BaseRequest getInstance() {
//        // 对象实例化时与否判断（不使用同步代码块，sPayUtils不等于null时，直接返回对象，提高运行效率）
//        if (mRBaseRequest == null) {
//            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
//            synchronized (NetManager.class) {
//                //未初始化，则初始instance变量
//                if (mRBaseRequest == null) {
                    mRBaseRequest = new BaseRequest<>();
//                }
//            }
//        }
        return mRBaseRequest;
    }

    public int getType() {
        return type;
    }

    public R setType(int type) {
        this.type = type;
        return (R) this;
    }

    public String getUrl() {
        return url;
    }

    public R setUrl(String url) {
        this.url = url;
        return (R) this;
    }

    public ResponseHandler getHandler() {
        return mHandler;
    }

    public void setHandler(ResponseHandler handler) {
        this.mHandler = handler;
    }

//    protected RESPONSE parseJson(String json){
//        return null;
//    }

    @SuppressWarnings("unchecked")
    public R params(HttpParams params) {
        this.params = params;
        return (R) this;
    }

    public void postExecute(ResponseHandler handler) {
        mHandler = handler;
        if (params != null)
            NetManager.getInstance().params(params);
        NetManager.getInstance().postRequest(this);
    }

    public void getExecute(ResponseHandler handler) {
        mHandler = handler;
        if (params != null)
            NetManager.getInstance().params(params);
        NetManager.getInstance().getRequest(this);
    }
}
