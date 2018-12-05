package cc.seedland.oa.circulate.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import cc.seedland.oa.circulate.R;

/**
 * @author Created by Administrator.
 * @time 2018/11/27 0027 14:02
 * Description:
 */

public class DownLoadDialog extends Dialog {
    public DownLoadDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_progress);
        Window window = getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;

        attributes.gravity = Gravity.CENTER;
        window.setAttributes(attributes);
        setCanceledOnTouchOutside(false);
    }
}
