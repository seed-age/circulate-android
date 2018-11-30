package com.ecology.view.seedland.circulate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.base.CirculateBaseActivity;
import com.ecology.view.seedland.circulate.fragment.CommonGroupFragment;
import com.ecology.view.seedland.circulate.fragment.OrganizationFragment;
import com.ecology.view.seedland.circulate.fragment.PrivateGroupFragment;
import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.modle.bean.ContactsMultiInfo;
import com.ecology.view.seedland.circulate.modle.bean.UserInfo;
import com.ecology.view.seedland.circulate.utils.LogUtil;
import com.ecology.view.seedland.circulate.utils.UISkipUtils;
import com.ecology.view.seedland.circulate.view.MyToolbar;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hch on 2018/2/2.
 */

public class OrganizationActivity extends CirculateBaseActivity {

    private FrameLayout mFlContainer;
    private FragmentTransaction mTransaction;
    private OrganizationFragment mOrganizationFragment;
    private List<UserInfo> mSelectedData;
    private int mType; //0:组织架构 1:公用组 2:私人组
    private CommonGroupFragment mCommonGroupFragment;
    private PrivateGroupFragment mPrivateGroupFragment;
    private MyToolbar mToolbar;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_circulate_organization;
    }

    @Override
    public void initView() {
        initStatusBar();
        initToolbar();
        mFlContainer = findView(R.id.fl_container);
    }

    private void initToolbar() {
        mToolbar = findView(R.id.toolbar);
        mToolbar.setBackClickListener(this);
        mToolbar.setTitle("组织");
        mToolbar.setRightText("确认");
        mToolbar.setOnRightTextClickListener(this);
    }

    private void initStatusBar() {
        FrameLayout flTopBar = findView(R.id.fl_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        flTopBar.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mSelectedData = (List<UserInfo>) intent.getSerializableExtra("DATA");
        mType = intent.getIntExtra("TYPE", 0);
        if (mType == 0) {
            mToolbar.setTitle("组织");
        }else if (mType == 1){
            mToolbar.setTitle("公用组");
        }else if (mType == 2){
            mToolbar.setTitle("私人组");
        }
        createFragment();
    }

    private void createFragment() {
        FragmentManager manager = getSupportFragmentManager();
        mTransaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("SELECTED_DATA", (Serializable) mSelectedData);
        if (mType == 0) {
            mOrganizationFragment = new OrganizationFragment();
            mOrganizationFragment.setArguments(bundle);
            mTransaction.replace(R.id.fl_container, mOrganizationFragment).commit();
        } else if (mType == 1) {
            mCommonGroupFragment = new CommonGroupFragment();
            mCommonGroupFragment.setArguments(bundle);
            mTransaction.replace(R.id.fl_container, mCommonGroupFragment).commit();
        }else if (mType == 2) {
            mPrivateGroupFragment = new PrivateGroupFragment();
            mPrivateGroupFragment.setArguments(bundle);
            mTransaction.replace(R.id.fl_container, mPrivateGroupFragment).commit();
        }
    }

    @Override
    public void onClick(View v, int id) {
        Intent intent = new Intent();
        List<UserInfo> selectedNode = null;
        if (mType == 0) {
            selectedNode = mOrganizationFragment.getSelectedNode();
        } else if (mType == 1) {
            selectedNode = mCommonGroupFragment.getSelectedNode();
        } else if (mType == 2) {
            selectedNode = mPrivateGroupFragment.getSelectedNode();
        }
        switch (id) {
            case R.id.tv_right:
                intent.putExtra("DATA", (Serializable) selectedNode);
                setResult(UISkipUtils.FROM_SELECTED, intent);
                finish();
                break;
            case R.id.ll_back:
                if (selectedNode.size() != 0)
                    intent.putExtra("DATA", (Serializable) selectedNode);
                else
                    intent.putExtra("DATA", (Serializable) mSelectedData);
                setResult(UISkipUtils.FROM_EDIT, intent);
                finish();
                break;
            case R.id.ll_search:
                UISkipUtils.skipToSearchPeopleActivity(this, selectedNode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UISkipUtils.TO_EDIT) {
            if (resultCode == UISkipUtils.FROM_EDIT) {
                List<UserInfo> selected = (List<UserInfo>) data.getSerializableExtra("DATA");
                if (mType == 0) {
                    mOrganizationFragment.setSelectedNode(selected);
                }else if (mType == 1) {
                    mCommonGroupFragment.setSelectedNode(selected);
                }else if (mType == 2) {
                    mPrivateGroupFragment.setSelectedNode(selected);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        List<UserInfo> selectedNode = null;
        if (mType == 0) {
            selectedNode = mOrganizationFragment.getSelectedNode();
        }else if (mType == 1) {
            selectedNode = mCommonGroupFragment.getSelectedNode();
        }else if (mType == 2) {
            selectedNode = mPrivateGroupFragment.getSelectedNode();
        }
        intent.putExtra("DATA", (Serializable) selectedNode);
        setResult(UISkipUtils.FROM_EDIT, intent);
        finish();
    }
}
