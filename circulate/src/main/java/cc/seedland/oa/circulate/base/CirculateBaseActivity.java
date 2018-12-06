package cc.seedland.oa.circulate.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.view.DelayDialog;


/**
 * Created by HeCh on 2017/3/13 0013.
 */

public abstract class CirculateBaseActivity extends AppCompatActivity implements IUIOperation {
    protected Bundle savedInstanceState;
    private DelayDialog mDelayDialog;

    protected final int INIT_DATA = 0x001;
    protected final int LOAD_MORE = 0x002;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明底部导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(getLayoutRes());
        this.savedInstanceState = savedInstanceState;
        //拿到系统跟布局对象，可查找到activity所有子控件
        View root = findViewById(android.R.id.content);
        // 查找activity布局中所有的Button（ImageButton），并设置点击事件
//        Utils.findButtonAndSetOnClickListener(root, this);
        mDelayDialog = new DelayDialog(this);
        initView();
        initData();
        initListener();
    }


    public void showDelayDialog() {
        if (!mDelayDialog.isShowing())
            mDelayDialog.show();
    }


    public void hideDelayDialog() {
        if (mDelayDialog != null && mDelayDialog.isShowing())
            mDelayDialog.dismiss();
    }

    /**
     * 查找空间的方法，可省略强转
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T> T findView(int id) {
        T view = (T) findViewById(id);
        return view;
    }

    @Override
    public void onClick(View view) {
        onClick(view, view.getId());
    }

    public void showToast(String text) {
        Global.showToast(text);
    }

    private ProgressDialog mPDialog;

    /**
     * 显示加载提示框(不能在子线程调用)
     */
    public void showProgressDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPDialog = new ProgressDialog(CirculateBaseActivity.this);
                mPDialog.setMessage(message);
                // 点击外部时不销毁
                mPDialog.setCanceledOnTouchOutside(false);

                // activity如果正在销毁或已销毁，不能show Dialog，否则出错。
                if (!isFinishing())
                    mPDialog.show();
            }
        });
    }

    /**
     * 销毁加载提示框
     */
    public void dismissProgressDialog() {
        if (mPDialog != null) {
            mPDialog.dismiss();
            mPDialog = null;
        }
    }

    public void showMyDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("确定", null);
        alertDialog.show();
    }

}
