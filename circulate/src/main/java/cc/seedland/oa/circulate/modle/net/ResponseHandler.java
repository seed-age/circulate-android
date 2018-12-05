package cc.seedland.oa.circulate.modle.net;

import org.json.JSONObject;

/**
 * Created by hch on 2018/1/13.
 */

public interface ResponseHandler {
    void onError(String msg, String code);

    void onSuccess(String json, JSONObject jsonObject,BaseResponse response);
}
