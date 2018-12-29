package cc.seedland.oa.circulate.activity;

import android.content.Intent;
import android.graphics.Color;
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
import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.modle.net.BaseResponse;
import cc.seedland.oa.circulate.modle.net.HttpApis;

import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.utils.UISkipUtils;

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
//        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
//        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.BLUE, Color.BLUE);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                getData();
//            }
//        });
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
                        HttpService.removeObject(mMailId, userInfo.userId, SelectedContactsActivity.this);
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
        mSelectedUser = (List<UserInfo>) intent.getSerializableExtra("SELECTED_USER");
        if(mSelectedUser!=null&&mSelectedUser.size()>0){
            toolbar.setRightText("确认");
            toolbar.setOnRightTextClickListener(this);
        }
        mAdapter.setNewData(mSelectedUser);
//        getData();
    /*    mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getData();
            }
        });*/
    }

    /**
     * 530
     */
//    int page = 1;
//
//    private void getData() {
//        HttpParams params = new HttpParams();
//        params.put("userId", Global.sKnife.getCurrentUserId());
//        params.put("mailId", mMailId);
//        params.put("page", page);
//        params.put("pageRows", 15);
//        HttpService.loadObjectList(params, new ResponseHandler() {
//            @Override
//            public void onError(String msg, String code) {
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
//                NewObjectInfo data = Utils.parseJson(json, NewObjectInfo.class);
//                if (data != null) {
//                    int currentPage = data.getData().getCurrentPage();
//                    int totalPage = data.getData().getTotalPage();
//                    List<NewObjectInfo.DataBean.ListBean> list = data.getData().getList();
//                    if (list != null && !list.isEmpty()) {
//                        if (page == 1) {
//                            mSelectedUser.clear();
//                            for (NewObjectInfo.DataBean.ListBean bean : list) {
//                                UserInfo userInfo = new UserInfo();
//                                userInfo.departmentName = bean.getDepartmentName();
//                                userInfo.lastName = bean.getLastName();
//                                userInfo.loginId = bean.getLoginId();
//                                userInfo.subcompanyName = bean.getSubcompanyName();
//                                userInfo.userId = bean.getUserId() + "";
//                                userInfo.workCode = bean.getWorkCode();
//                                mSelectedUser.add(userInfo);
//                            }
//                        } else {
//                            for (NewObjectInfo.DataBean.ListBean bean : list) {
//                                UserInfo userInfo = new UserInfo();
//                                userInfo.departmentName = bean.getDepartmentName();
//                                userInfo.lastName = bean.getLastName();
//                                userInfo.loginId = bean.getLoginId();
//                                userInfo.subcompanyName = bean.getSubcompanyName();
//                                userInfo.userId = bean.getUserId() + "";
//                                userInfo.workCode = bean.getWorkCode();
//                                mSelectedUser.add(userInfo);
//                            }
//                        }
//                        mAdapter.notifyDataSetChanged();
//
//                    } else {
//                        if (page == 1) {
//                        }
//                    }
//                }
//                mAdapter.loadMoreComplete();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
//    }

    @Override
    public void onClick(View v, int id) {
        Intent intent = new Intent();
        if (id == R.id.tv_continue) {
            intent.putExtra("DATA", (Serializable) mSelectedUser);
            setResult(UISkipUtils.FROM_EDIT, intent);
            finish();
        } else if (id == R.id.tv_right) {
            intent.putExtra("DATA", (Serializable) mSelectedUser);
            setResult(UISkipUtils.FROM_SELECTED, intent);
            finish();
        } else if (id == R.id.ll_back) {
            intent.putExtra("DATA", (Serializable) mSelectedUser);
            setResult(UISkipUtils.FROM_EDIT, intent);
            finish();
        }
    }

    @Override
    public void onError(String msg, String code) {
        showToast(msg);
    }

    @Override
    public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
        int type = response.getType();
        if (type == HttpApis.getRemoveObject().hashCode()) {
            showToast(response.getMsg());
            mSelectedUser.remove(mRemovePosition);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("DATA", (Serializable) mSelectedUser);
        setResult(UISkipUtils.FROM_EDIT, intent);
        finish();
        super.onBackPressed();
    }
}
