package com.ecology.view.seedland.circulate.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.adapter.DBankAdapter;
import com.ecology.view.seedland.circulate.base.CirculateBaseActivity;
import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.modle.bean.DBankDirInfo;
import com.ecology.view.seedland.circulate.utils.UISkipUtils;
import com.ecology.view.seedland.circulate.view.MyToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/30 0030.
 */

public class DBankActivity extends CirculateBaseActivity {

    private DBankAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_dbank;
    }

    @Override
    public void initView() {
        initToolbar();
        initStatusBar();
        mRecyclerView = findView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DBankAdapter(R.layout.item_dbank_dir,null);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initStatusBar() {
        FrameLayout flTopBar = findView(R.id.fl_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        flTopBar.setPadding(0, statusBarHeight, 0, 0);
    }


    private void initToolbar() {
        MyToolbar toolbar = findView(R.id.toolbar);
//        toolbar.setOnBackClickListenerWithResult(this,UISkipUtils.FROM_EDIT,null);
        toolbar.setOnBackClickListener(this);
        toolbar.setTitle("实地网盘");
    }

    @Override
    public void initListener() {
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<DBankDirInfo> data = adapter.getData();
                DBankDirInfo dBankDirInfo = data.get(position);
                UISkipUtils.skipToFileListActivity(DBankActivity.this,dBankDirInfo.name,dBankDirInfo.type,"");
            }
        });
    }

    @Override
    public void initData() {
        Global.sSelectedFiles = new ArrayList<>();
        Global.sFileListInstance = new ArrayList<>();
        List<DBankDirInfo> data = new ArrayList<>();
        data.add(new DBankDirInfo("企业空间","ent"));
        data.add(new DBankDirInfo("个人空间","self"));
        mAdapter.setNewData(data);
    }

    @Override
    public void onClick(View v, int id) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UISkipUtils.TO_EDIT) {
            finish();
        }
    }
}
