package cc.seedland.oa.circulate.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.adapter.ObjectListAdapter;
import cc.seedland.oa.circulate.base.CirculateBaseActivity;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.ObjectInfo;
import cc.seedland.oa.circulate.modle.bean.ObjectListInfo;
import cc.seedland.oa.circulate.modle.net.BaseResponse;
import cc.seedland.oa.circulate.modle.net.HttpApis;
import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.MyToolbar;
import cc.seedland.oa.circulate.view.SwipeLayoutManager;

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

    SwipeRefreshLayout swipeRefreshView;

    @Override
    public void initView() {
        initStatusBar();
        initToolbar();
        initList();
        swipeRefreshView = findViewById(R.id.swipeLayout);
        swipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshView.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);


    }

    private void initList() {
        mRvList = findView(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ObjectListAdapter(R.layout.item_object_list, mData);
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
                    HttpService.removeObject(HttpApis.getRemoveObject(),mMailid, objectInfo.userId, ObjectListActivity.this);
                }
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                HttpService.loadObjectList(INIT_DATA, mMailid, page, ObjectListActivity.this);
            }
        });
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                HttpService.loadObjectList(INIT_DATA, mMailid, page, ObjectListActivity.this);
            }
        });
    }

    int page = 1;

    @Override
    public void initData() {
        Intent intent = getIntent();
        mMailid = intent.getLongExtra("MAILID", 0);
        HttpService.loadObjectList(INIT_DATA, mMailid, page, this);
    }

    @Override
    public void onClick(View v, int id) {

    }

    @Override
    public void onError(String msg, String code) {
        showToast(msg);
        swipeRefreshView.setRefreshing(false);
    }

    @Override
    public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
        hideDelayDialog();
        swipeRefreshView.setRefreshing(false);
        int type = response.getType();
        String dataStr = jsonObject.optString("data");
        if (type == INIT_DATA) {
            refreshList(dataStr);
        } else if (type == HttpApis.getRemoveObject().hashCode()) {
            showToast(response.getMsg());
            HttpService.loadObjectList(INIT_DATA, mMailid, page, this);
        }
    }

    private void refreshList(String dataStr) {
        ObjectListInfo data = Utils.parseJson(dataStr, ObjectListInfo.class);
        if (data != null) {
            List<ObjectInfo> list = data.list;
            if (list != null) {
                if (page == 1) {
                    mData.clear();
                    mData.addAll(list);
                } else {
                    mData.addAll(list);
                }
            }
            mLoadMoreEnd = data.lastPage;
            mAdapter.loadMoreComplete();
            mAdapter.notifyDataSetChanged();
            if (mLoadMoreEnd) {
                mAdapter.loadMoreEnd();
            }
        }
    }
}
