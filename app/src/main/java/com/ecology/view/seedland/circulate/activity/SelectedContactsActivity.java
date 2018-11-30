package com.ecology.view.seedland.circulate.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.adapter.SelectedContactsAdapter;
import com.ecology.view.seedland.circulate.base.CirculateBaseActivity;
import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.modle.bean.UserInfo;
import com.ecology.view.seedland.circulate.modle.net.BaseResponse;
import com.ecology.view.seedland.circulate.modle.net.HttpApis;
import com.ecology.view.seedland.circulate.modle.net.HttpService;
import com.ecology.view.seedland.circulate.modle.net.ResponseHandler;
import com.ecology.view.seedland.circulate.utils.UISkipUtils;
import com.ecology.view.seedland.circulate.view.MyToolbar;
import com.ecology.view.seedland.circulate.view.SwipeLayoutManager;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class SelectedContactsActivity extends CirculateBaseActivity implements ResponseHandler {

    private SelectedContactsAdapter mAdapter;
    private RecyclerView mRvList;
    private List<UserInfo> mSelectedUser;
    private long mMailId;
    private int mRemovePosition;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_selected_contacts;
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
        mAdapter = new SelectedContactsAdapter(R.layout.item_selected_contacts, null);
        mRvList.setAdapter(mAdapter);
    }

    private void initToolbar() {
        MyToolbar toolbar = findView(R.id.toolbar);
        toolbar.setBackClickListener(this);
        toolbar.setTitle("已选联系人");
        toolbar.setRightText("确认");
        toolbar.setOnRightTextClickListener(this);
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
                if (id == R.id.tv_delete) {
                    if (mMailId != -1) {
                        mRemovePosition = position;
                        UserInfo userInfo = mSelectedUser.get(position);
                        HttpService.removeObject(mMailId, userInfo.userId,SelectedContactsActivity.this);
                    }else {
                        mSelectedUser.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mMailId = intent.getLongExtra("MAIL_ID", -1);
        mSelectedUser = (List<UserInfo>) intent.getSerializableExtra("SELECTED_USER");
        mAdapter.setNewData(mSelectedUser);
    }

    @Override
    public void onClick(View v, int id) {
        Intent intent = new Intent();
        switch (id) {
            case R.id.tv_continue:
                intent.putExtra("DATA", (Serializable) mSelectedUser);
                setResult(UISkipUtils.FROM_EDIT,intent);
                finish();
                break;
            case R.id.tv_right:
                intent.putExtra("DATA", (Serializable) mSelectedUser);
                setResult(UISkipUtils.FROM_SELECTED,intent);
                finish();
                break;
            case R.id.ll_back:
                intent.putExtra("DATA", (Serializable) mSelectedUser);
                setResult(UISkipUtils.FROM_EDIT,intent);
                finish();
                break;
        }
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }

    @Override
    public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
        int type = response.getType();
        if (type == HttpApis.REMOVE_OBJECT.hashCode()) {
            showToast(response.getMsg());
            mSelectedUser.remove(mRemovePosition);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("DATA", (Serializable) mSelectedUser);
        setResult(UISkipUtils.FROM_EDIT,intent);
        finish();
        super.onBackPressed();
    }
}
