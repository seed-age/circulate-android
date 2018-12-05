package com.ecology.view.seedland;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;

import cc.seedland.oa.circulate.activity.ChuanYueActivity;
import cc.seedland.oa.demo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.circulate_txv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ARouter.getInstance().build("/Function/Circulate")
//                        .navigation();
                Intent i = new Intent(MainActivity.this, ChuanYueActivity.class);
                startActivity(i);
            }
        });
    }
}
