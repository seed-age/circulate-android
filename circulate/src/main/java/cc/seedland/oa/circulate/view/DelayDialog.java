package cc.seedland.oa.circulate.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.global.Global;

/**
 * Created by Administrator on 2017/12/2 0002.
 */

public class DelayDialog extends AlertDialog {
    public DelayDialog(@NonNull Context context) {
        super(context);
    }

    protected DelayDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DelayDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_deley_load);
        setCanceledOnTouchOutside(false);//按对话框以外的地方不起作用。按返回键还起作用
        //        setCancelable(false);//按对话框以外的地方不起作用。按返回键也不起作用
        Window window = getWindow();
        //去掉dialog默认的padding
//        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = Global.dp2px(200);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp);
    }
}
