package cc.seedland.oa.circulate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.adapter.SelectedContactsAdapter;
import cc.seedland.oa.circulate.base.CirculateBaseActivity;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.NewObjectInfo;
import cc.seedland.oa.circulate.modle.bean.ObjectInfo;
import cc.seedland.oa.circulate.modle.bean.ObjectListInfo;
import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.modle.net.BaseResponse;
import cc.seedland.oa.circulate.modle.net.HttpApis;

import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.utils.UISkipUtils;

import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.MyToolbar;
import cc.seedland.oa.circulate.view.SwipeLayoutManager;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class SelectedContactsActivity extends CirculateBaseActivity implements ResponseHandler {

    private SelectedContactsAdapter mAdapter;
    private RecyclerView mRvList;
    private List<UserInfo> mSelectedUser = new ArrayList<>();
    private long mMailId;
    private int mRemovePosition;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private MyToolbar toolbar;
    private int page = 1;
    private boolean mLoadMoreEnd = false;
    private boolean mLoadMoreFail = false;
    private List<UserInfo> mSelectedList;
    private int mTotalRecord;
    private String mDeleteApi;
    private int mType;
    private ArrayList<String> mDeleteUserIdList = new ArrayList<>();

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
        mSwipeRefreshLayout = findView(R.id.swipe_refresh);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectedContactsAdapter(R.layout.item_selected_contacts, null);
        mRvList.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                HttpService.loadObjectList(INIT_DATA, mMailId, page, SelectedContactsActivity.this);
            }
        });
    }

    private void initToolbar() {
        toolbar = findView(R.id.toolbar);
        toolbar.setBackClickListener(this);
        toolbar.setTitle("已选联系人");
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
                        showDelayDialog();
                        HttpService.removeObject(mDeleteApi,mMailId, userInfo.userId, SelectedContactsActivity.this);
                    } else {
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
        mType = intent.getIntExtra("type", 1);
        if (mType == 1) {//来自传阅详情
            mDeleteApi = HttpApis.getRemoveObject();
        }else if (mType == 2) {//来自待发传阅
            mDeleteApi = HttpApis.deleteObjectWait();
        }
        mSelectedList = (List<UserInfo>) intent.getSerializableExtra("SELECTED_USER");
        if (mSelectedList != null && mSelectedList.size() > 0) {
            toolbar.setRightText("确认");
            toolbar.setOnRightTextClickListener(this);
            mSelectedUser.addAll(mSelectedList);
        }
        showDelayDialog();
        HttpService.loadObjectList(INIT_DATA, mMailId, page, this);
        mAdapter.setNewData(mSelectedUser);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                HttpService.loadObjectList(INIT_DATA, mMailId, page, SelectedContactsActivity.this);
            }
        });
    }


    private void refreshList(String dataStr) {
        ObjectListInfo data = Utils.parseJson(dataStr, ObjectListInfo.class);
        if (data != null) {
            mTotalRecord = data.totalRecord;
            List<ObjectInfo> list = data.list;
            if (list != null) {
                if (page == 1) {
                    mSelectedUser.clear();
                    updateList(list);
                } else {
                    updateList(list);
                }
            }
            mLoadMoreEnd = data.lastPage;
            mAdapter.loadMoreComplete();
            if (mSelectedList != null) {
//                mSelectedUser.addAll(mSelectedList);
//                mTotalRecord += mSelectedList.size();
            }
            mAdapter.notifyDataSetChanged();
            if (mLoadMoreEnd) {
                mAdapter.loadMoreEnd();
            }
        }
    }

    private void updateList(List<ObjectInfo> list) {
        if (mSelectedUser != null && mSelectedUser.size() > 0) {
            List<String> idList = new ArrayList<>();
            for (UserInfo userInfo : mSelectedUser) {
                idList.add(userInfo.userId);
            }
            for (ObjectInfo objectInfo : list) {
                if (!idList.contains(objectInfo.getUserId())) {
                    packUserInfo(objectInfo);
                }
            }
        } else {
            for (ObjectInfo objectInfo : list) {
                packUserInfo(objectInfo);
            }
        }
        mTotalRecord = mSelectedUser.size();
        mAdapter.notifyDataSetChanged();
    }

    private void packUserInfo(ObjectInfo objectInfo) {
        UserInfo userInfo = new UserInfo();
        userInfo.departmentName = objectInfo.departmentName;
        userInfo.lastName = objectInfo.lastName;
        userInfo.loginId = objectInfo.loginId;
        userInfo.subcompanyName = objectInfo.subcompanyName;
        userInfo.userId = objectInfo.getUserId();
        userInfo.workCode = objectInfo.workCode;
        mSelectedUser.add(userInfo);
    }

    @Override
    public void onClick(View v, int id) {
        Intent intent = new Intent();
        if (id == R.id.tv_continue) {
            intent.putParcelableArrayListExtra("DATA", (ArrayList<? extends Parcelable>) mSelectedUser);
            intent.putStringArrayListExtra("DELETE_LIST",  mDeleteUserIdList);
            intent.putExtra("COUNT", mTotalRecord);
            setResult(UISkipUtils.FROM_EDIT, intent);
            finish();
        } else if (id == R.id.tv_right) {
            intent.putExtra("DATA", (Serializable) mSelectedList);
            setResult(UISkipUtils.FROM_SELECTED, intent);
            finish();
        } else if (id == R.id.ll_back) {
//            intent.putExtra("DATA", (Serializable) mSelectedList);
            intent.putParcelableArrayListExtra("DATA", (ArrayList<? extends Parcelable>) mSelectedUser);
            intent.putStringArrayListExtra("DELETE_LIST",mDeleteUserIdList);
            intent.putExtra("COUNT", mTotalRecord);
            setResult(UISkipUtils.FROM_EDIT, intent);
            finish();
        }
    }

    @Override
    public void onError(String msg, String code) {
        hideDelayDialog();
        showToast(msg);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
        hideDelayDialog();
        int type = response.getType();
        if (type == mDeleteApi.hashCode()) {
            showToast(response.getMsg());
            if (response.getCode().equals("200")) {
                String userId = mSelectedUser.get(mRemovePosition).getUserId();
                mDeleteUserIdList.add(userId);
                mSelectedUser.remove(mRemovePosition);
                mTotalRecord--;
                mAdapter.notifyDataSetChanged();
            }
        } else if (type == INIT_DATA) {
            mSwipeRefreshLayout.setRefreshing(false);
            String dataStr = jsonObject.optString("data");
            refreshList(dataStr);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
//        intent.putExtra("DATA", (Serializable) mSelectedUser);
        intent.putParcelableArrayListExtra("DATA", (ArrayList<? extends Parcelable>) mSelectedUser);
        intent.putStringArrayListExtra("DELETE_LIST",  mDeleteUserIdList);
        intent.putExtra("COUNT", mTotalRecord);
        setResult(UISkipUtils.FROM_EDIT, intent);
        finish();
        super.onBackPressed();
    }
}
