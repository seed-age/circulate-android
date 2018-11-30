package com.ecology.view.seedland.circulate.modle.net;

import org.json.JSONObject;

/**
 * Created by hch on 2018/1/13.
 */

public interface ResponseHandler {
    void onError(String msg);

    void onSuccess(String json, JSONObject jsonObject,BaseResponse response);
}
