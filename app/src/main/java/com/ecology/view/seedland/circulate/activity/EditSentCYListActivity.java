package com.ecology.view.seedland.circulate.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.adapter.EditSentCYListAdapter;
import com.ecology.view.seedland.circulate.base.CirculateBaseActivity;
import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.modle.bean.MailInfo;
import com.ecology.view.seedland.circulate.modle.bean.SentCYListInfo;
import com.ecology.view.seedland.circulate.modle.net.BaseResponse;
import com.ecology.view.seedland.circulate.modle.net.HttpApis;
import com.ecology.view.seedland.circulate.modle.net.HttpService;
import com.ecology.view.seedland.circulate.modle.net.ResponseHandler;
import com.ecology.view.seedland.circulate.utils.UISkipUtils;
import com.ecology.view.seedland.circulate.utils.Utils;
import com.ecology.view.seedland.circulate.view.MyToolbar;
import com.ecology.view.seedland.circulate.view.SimpleItemDecoration;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/6 0006.
 */

public class EditSentCYListActivity extends CirculateBaseActivity implements ResponseHandler,
        BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView mRecyclerView;
    private EditSentCYListAdapter mAdapter;
    private int page = 1;
    private boolean mLoadMoreEnd = false;
    private boolean mLoadMoreFail = false;
    private List<MailInfo> mData = new ArrayList<>();
    private boolean isSelectAll = false;
    private int mStatus;
    private LinearLayout mLlRemove;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_edit_cylist;
    }

    @Override
    public void initView() {
        initStatusBar();
        initToolbar();
        initList();
        ImageView ivRemove = findView(R.id.iv_remove);
        TextView tvRemove = findView(R.id.tv_remove);
        ivRemove.setImageResource(R.drawable.icon_trash);
        tvRemove.setText("删除");
        mLlRemove = findView(R.id.ll_remove);
    }

    private void initList() {
        mRecyclerView = findView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SimpleItemDecoration itemDecoration = new SimpleItemDecoration(this, new
                SimpleItemDecoration
                        .ObtainTextCallback() {
                    @Override
                    public String getText(int position) {
                        if (mData != null && mData.size() > 0 && position < mData.size())
                            return mData.get(position).getTime();
                        else
                            return null;
                    }
                });
        mRecyclerView.addItemDecoration(itemDecoration);
        mAdapter = new EditSentCYListAdapter(R.layout.item_edit_cy, mData);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolbar() {
        MyToolbar toolbar = findView(R.id.toolbar);
        toolbar.setOnBackClickListenerWithResult(this, UISkipUtils.FROM_EDIT,null);
        toolbar.setTitle("编辑");
        toolbar.setRightText("全选");
        toolbar.setOnRightTextClickListener(this);
        toolbar.showTextBack();
    }

    private void initStatusBar() {
        FrameLayout flTopBar = findView(R.id.fl_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        flTopBar.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void initListener() {
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<MailInfo> data = adapter.getData();
                int id = view.getId();
                switch (id) {
                    case R.id.fl_selector:
                        MailInfo listBean = data.get(position);
                        listBean.isSelected = !listBean.isSelected;
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    if (manager instanceof LinearLayoutManager) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                        int lastPosition = layoutManager.findLastVisibleItemPosition();
                        if (lastPosition == mAdapter.getItemCount() - 1)
                            if (!mLoadMoreFail) {
                                if (!mLoadMoreEnd) {
                                    page++;
                                    HttpService.loadCYListData(HttpApis.SENT_LIST,LOAD_MORE,mStatus,page,-1,EditSentCYListActivity.this);
                                } else {
                                    mAdapter.loadMoreEnd();
                                }
                            } else {
                                mLoadMoreFail = false;
                                mAdapter.loadMoreFail();
                            }
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mStatus = intent.getIntExtra("STATUS", 5);
        showDelayDialog();
        HttpService.loadCYListData(HttpApis.SENT_LIST,INIT_DATA, mStatus,page,-1,this);
        if (mStatus == 3) {
            mLlRemove.setVisibility(View.GONE);
        }else {
            mLlRemove.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v, int id) {
        switch (id) {
            case R.id.tv_right:
                List<MailInfo> data = mAdapter.getData();
                isSelectAll = !isSelectAll;
                for (MailInfo datum : data) {
                    datum.isSelected = isSelectAll;
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_focus:
                focusAll();
                break;
            case R.id.ll_remove:
                removeAll();
                break;
        }
    }

    private void removeAll() {
        List<MailInfo> data = mAdapter.getData();
        List<String> removeMailId = new ArrayList<>();
        for (MailInfo mailInfo : data) {
            if (mailInfo.isSelected) {
                removeMailId.add(String.valueOf(mailInfo.mailId));
            }
        }
        if (removeMailId.size() == 0) {
            showToast("请选择你所要删除的传阅!");
            return;
        }
        HttpService.deleteMail(removeMailId, this);
    }

    private void focusAll() {
        List<MailInfo> data = mAdapter.getData();
        List<String> focusMailId = new ArrayList<>();
        for (MailInfo mailInfo : data) {
            if (mailInfo.isSelected) {
                focusMailId.add(String.valueOf(mailInfo.mailId));
            }
        }
        HttpService.focusReceivedMail(focusMailId,true,1,this);
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
        }else if (type == LOAD_MORE) {
            refreshList(dataStr);
        }else if (type == HttpApis.DELETE_MAIL.hashCode()) {
            showToast(jsonObject.optString("msg"));
//            page = 1;
//            HttpService.loadCYListData(HttpApis.SENT_LIST,INIT_DATA, mStatus,page,-1,this);
            setResult(UISkipUtils.FROM_EDIT);
            page = 1;
            HttpService.loadCYListData(HttpApis.SENT_LIST,INIT_DATA, mStatus,page,-1,this);
//            finish();
        } else if (type == HttpApis.RECEIVED_FOCUS_MAIL.hashCode()){
            showToast(jsonObject.optString("msg"));
            page = 1;
            HttpService.loadCYListData(HttpApis.SENT_LIST,INIT_DATA, mStatus,page,-1,this);
        }
    }


    private void refreshList(String dataStr) {
        SentCYListInfo data = Utils.parseJson(dataStr, SentCYListInfo.class);
        if (data != null) {
            mLoadMoreEnd = data.lastPage;
            if (mLoadMoreEnd) {
                mAdapter.loadMoreEnd();
            }
            List<MailInfo> listInfo = data.list;
            mData.addAll(listInfo);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (!mLoadMoreFail) {
            if (!mLoadMoreEnd) {

            } else {
                mAdapter.loadMoreEnd();
            }
        } else {
            mLoadMoreFail = false;
            mAdapter.loadMoreFail();
        }
    }
}
