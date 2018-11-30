package com.ecology.view.seedland.circulate.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.adapter.ObjectListAdapter;
import com.ecology.view.seedland.circulate.base.CirculateBaseActivity;
import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.modle.bean.ObjectInfo;
import com.ecology.view.seedland.circulate.modle.bean.ObjectListInfo;
import com.ecology.view.seedland.circulate.modle.net.BaseResponse;
import com.ecology.view.seedland.circulate.modle.net.HttpApis;
import com.ecology.view.seedland.circulate.modle.net.HttpService;
import com.ecology.view.seedland.circulate.modle.net.ResponseHandler;
import com.ecology.view.seedland.circulate.utils.Utils;
import com.ecology.view.seedland.circulate.view.MyToolbar;
import com.ecology.view.seedland.circulate.view.SwipeLayoutManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class ObjectListActivity extends CirculateBaseActivity implements ResponseHandler {

    private RecyclerView mRvList;
    private ObjectListAdapter mAdapter;
    private List<ObjectInfo> mData = new ArrayList<>();
    private boolean mLoadMoreEnd = false;
    private boolean mLoadMoreFail = false;
    private long mMailid;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_object_list;
    }

    @Override
    public void initView() {
        initStatusBar();
        initToolbar();
        initList();
    }

    private void initList() {
        mRvList = findView(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ObjectListAdapter(R.layout.item_object_list,mData);
        mRvList.setAdapter(mAdapter);
    }

    private void initToolbar() {
        MyToolbar toolbar = findView(R.id.toolbar);
        toolbar.setOnBackClickListener(this);
        toolbar.setTitle("传阅对象");
    }

    private void initStatusBar() {
        FrameLayout flTopBar = findView(R.id.fl_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        flTopBar.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void initListener() {
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    SwipeLayoutManager.getInstance().closeSwipeLayout();
                }
            }
        });

        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                List<ObjectInfo> data = adapter.getData();
                ObjectInfo objectInfo = data.get(position);
                if (id == R.id.tv_delete) {
                    showDelayDialog();
                    HttpService.removeObject(mMailid,objectInfo.userId,ObjectListActivity.this);
                }
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mMailid = intent.getLongExtra("MAILID", 0);
        HttpService.loadObjectList(INIT_DATA, mMailid,this);
    }

    @Override
    public void onClick(View v, int id) {

    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }

    @Override
    public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
        hideDelayDialog();
        int type = response.getType();
        String dataStr = jsonObject.optString("data");
        if (type == INIT_DATA) {
            mData.clear();
            refreshList(dataStr);
        }else if (type == HttpApis.REMOVE_OBJECT.hashCode()) {
            showToast(response.getMsg());
            HttpService.loadObjectList(INIT_DATA, mMailid,this);
        }
    }

    private void refreshList(String dataStr) {
        ObjectListInfo data = Utils.parseJson(dataStr, ObjectListInfo.class);
        if (data != null) {
            mLoadMoreEnd = data.lastPage;
            if (mLoadMoreEnd) {
                mAdapter.loadMoreEnd();
            }
            List<ObjectInfo> list = data.list;
            mData.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }
}
