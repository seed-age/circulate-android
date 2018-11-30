package com.ecology.view.seedland.circulate.global;

import android.app.Application;

import com.zzhoujay.richtext.RichText;

/**
 * Created by Administrator on 2018/1/5 0005.
 */

public class MyApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Global.init(getApplicationContext());
    }
}
