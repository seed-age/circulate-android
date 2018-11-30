package com.ecology.view.seedland.circulate.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.ecology.view.seedland.circulate.R;


/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class BottomDialog extends Dialog {

    private int mLayoutRes;

    public BottomDialog(Context context, int layoutRes) {
        this(context, R.style.MyDialogStyleBottom,layoutRes);

    }

    public BottomDialog(Context context, int themeResId, int layoutRes) {
        super(context, themeResId);
        mLayoutRes = layoutRes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        setContentView(mLayoutRes);
        //去掉dialog默认的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //设置dialog的位置在底部
        lp.gravity = Gravity.BOTTOM;

        window.setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }

}
