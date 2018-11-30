package com.ecology.view.seedland.circulate.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.utils.Utils;
import com.ecology.view.seedland.circulate.view.DelayDialog;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public abstract class CirculateBaseFragment extends Fragment implements IUIOperation {
    /** 管理Fragment的Activity */
    protected CirculateBaseActivity mActivity;
    private DelayDialog mDelayDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (CirculateBaseActivity) getActivity();
        mDelayDialog = new DelayDialog(mActivity);
    }
    
    /** Fragment显示的布局 */
    public View mRoot;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = inflater.inflate(getLayoutRes(), container,false);
            // 查找布局中的所有的button(ImageButton),并设置点击事件
            Utils.findButtonAndSetOnClickListener(mRoot, this);

            initView();
            initListener();
            initData();
        } else {
            // 解除mRoot与其父控件的关系
            unbindWidthParent(mRoot);
        }
        return mRoot;
    }

    /**
     * 解除父子控件关系
     *
     * @param view
     */
    public void unbindWidthParent(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
    }

    /** 查找子控件，可以省略强转 */
    public <T> T findView(int id) {
        @SuppressWarnings("unchecked")
        T view = (T) mRoot.findViewById(id);
        return view;
    }

    @Override
    public void onClick(View v) {
        onClick(v, v.getId());
    }

    public void showToast(String text) {
        Global.showToast(text);
    }


    public void showDelayDialog() {
        if (!mDelayDialog.isShowing())
            mDelayDialog.show();
    }


    public void hideDelayDialog() {
        if (mDelayDialog.isShowing())
            mDelayDialog.dismiss();
    }

/*
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }*/
}
