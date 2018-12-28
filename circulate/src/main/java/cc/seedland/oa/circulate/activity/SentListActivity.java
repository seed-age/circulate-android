package cc.seedland.oa.circulate.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.adapter.SentAdapter;
import cc.seedland.oa.circulate.base.CirculateBaseActivity;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.MailInfo;
import cc.seedland.oa.circulate.modle.bean.SentCYListInfo;
import cc.seedland.oa.circulate.modle.net.BaseResponse;
import cc.seedland.oa.circulate.modle.net.HttpApis;
import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.utils.LogUtil;
import cc.seedland.oa.circulate.utils.UISkipUtils;
import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.BottomDialog;
import cc.seedland.oa.circulate.view.MyToolbar;
import cc.seedland.oa.circulate.view.SimpleItemDecoration;
import cc.seedland.oa.circulate.view.SwipeLayoutManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/1/6 0006.
 */

public class SentListActivity extends CirculateBaseActivity implements ResponseHandler,
        BaseQuickAdapter.RequestLoadMoreListener {

    private SentAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<MailInfo> mData = new ArrayList<>();
    private MyToolbar mToolbar;
    private TabLayout mTabLayout;
    private LinearLayout mLlTabBar;
    private int status;
    private int page = 1;
    private int orderBy = -1;
    private boolean mLoadMoreEnd = false;
    private boolean mLoadMoreFail = false;
    private BottomDialog mSortDialog;
    private int mDeleteMailID;
    private int mFocusMailID;
    private LinearLayout mLlNoMail;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_chuanyue_list;
    }

    @Override
    public void initView() {
        initStatusBar();
        initToolbar();
        initList();
        mLlTabBar = findView(R.id.ll_tab_bar);
        //初始化tabLayout
        mTabLayout = findView(R.id.tabLayout);
        mLlNoMail = findView(R.id.ll_no_mail);
    }

    private void initList() {
        mRecyclerView = findView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SentAdapter(R.layout.item_received_list, mData);
        SimpleItemDecoration itemDecoration = new SimpleItemDecoration(this, new
                SimpleItemDecoration.ObtainTextCallback() {
                    @Override
                    public String getText(int position) {
                        if (mData != null && mData.size() > 0 && position < mData.size())
                            return mData.get(position).getTime();
                        else
                            return null;
                    }
                });
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initTabLayout(LinkedHashMap<String, Integer> tabs) {
        mTabLayout.removeAllTabs();
        Set<String> keys = tabs.keySet();
        for (String key : keys) {
            Integer status = tabs.get(key);
            mTabLayout.addTab(mTabLayout.newTab().setText(key).setTag(status));
        }
    }

    private void initToolbar() {
        mToolbar = findView(R.id.toolbar);
        mToolbar.setOnBackClickListener(this);
        mToolbar.setRightFirstIvImg(R.drawable.icon_edit);
        mToolbar.setRightSecondIvImg(R.drawable.icon_search);
        mToolbar.setRightFirstIvOnClickListener(this);
        mToolbar.setRightSecondIvOnClickListener(this);
    }

    private void initStatusBar() {
        LinearLayout llTopBar = findView(R.id.ll_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        llTopBar.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void initListener() {
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    SwipeLayoutManager.getInstance().closeSwipeLayout();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    if (manager instanceof LinearLayoutManager) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                        int lastPosition = layoutManager.findLastVisibleItemPosition();
                        if (lastPosition == mAdapter.getItemCount() - 1)
                            if (!mLoadMoreFail) {
                                if (!mLoadMoreEnd) {
                                    page++;
//                                    loadListData(LOAD_MORE, page);
                                    HttpService.loadCYListData(HttpApis.getSentList(), LOAD_MORE,
                                            status, page, orderBy, SentListActivity.this);
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

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<MailInfo> data = adapter.getData();
                MailInfo mailInfo = data.get(position);
                UISkipUtils.skipToCYDetailActivity(SentListActivity.this, mailInfo.mailId,1);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<MailInfo> data = adapter.getData();
                MailInfo mailInfo = data.get(position);
                int id = view.getId();
                if(id == R.id.tv_focus) {
                    mFocusMailID = mailInfo.mailId;
                    showDelayDialog();
                    List<String> focusMailId = new ArrayList<>();
                    focusMailId.add(String.valueOf(mFocusMailID));
                    HttpService.focusReceivedMail(focusMailId,!mailInfo.attention,1,SentListActivity.this);
                }else if(id == R.id.tv_delete) {
                    mDeleteMailID = mailInfo.mailId;
                    showDelayDialog();
                    List<String> deleteMailId = new ArrayList<>();
                    deleteMailId.add(String.valueOf(mDeleteMailID));
                    HttpService.deleteMail(deleteMailId, SentListActivity.this);
                }
            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                status = (int) tab.getTag();
                page = 1;
                LogUtil.e("status = " + status);
//                loadListData(INIT_DATA, page);
                showDelayDialog();
                HttpService.loadCYListData(HttpApis.getSentList(), INIT_DATA, status, page, orderBy,
                        SentListActivity.this);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //关注
    private void focusItem() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        int type = intent.getIntExtra("TYPE", 0);
        String title = intent.getStringExtra("TITLE");
        mToolbar.setTitle(title);
        status = type;
        LinkedHashMap<String, Integer> tabs = new LinkedHashMap<>();
//        tabs.put("所有传阅", 0);
        tabs.put("全部", 0);
        tabs.put("传阅中", 1);
        tabs.put("已完成", 3);
        initTabLayout(tabs);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if ((int)tab.getTag() == status) {
                tab.select();
                break;
            }
        }
        //设置列表默认参数
//        loadListData(INIT_DATA, page);
        showDelayDialog();
        HttpService.loadCYListData(HttpApis.getSentList(), INIT_DATA, status, page, orderBy, this);
    }

    @Override
    public void onClick(View v, int id) {
        if(id == R.id.fl_right_first_ic) {//编辑
            UISkipUtils.skipToEditSentCYListActivity(this, status);
        }else if(id == R.id.fl_right_second_ic) {
            UISkipUtils.skipToSearchCYActivity(this, HttpApis.getSearchSendList(),1);
        }else if(id == R.id.fl_sort) {
            showSortDialog();
        }else if(id == R.id.tv_sort_des) {
            mSortDialog.dismiss();
            page = 1;
            orderBy = 2;
//                loadListData(INIT_DATA, page);
            showDelayDialog();
            HttpService.loadCYListData(HttpApis.getSentList(), INIT_DATA, status, page, orderBy,
                    this);
        }else if(id == R.id.tv_sort_asc) {
            mSortDialog.dismiss();
            page = 1;
            orderBy = 1;
//                loadListData(INIT_DATA, page);
            showDelayDialog();
            HttpService.loadCYListData(HttpApis.getSentList(), INIT_DATA, status, page, orderBy,
                    this);
        }else if(id == R.id.tv_cancel) {
            mSortDialog.dismiss();
        }
    }

    private void showSortDialog() {
        if (mSortDialog == null)
            mSortDialog = new BottomDialog(this, R.layout.dialog_time_sort);
        mSortDialog.show();
        TextView tvSortDes = mSortDialog.findViewById(R.id.tv_sort_des);
        TextView tvSortAsc = mSortDialog.findViewById(R.id.tv_sort_asc);
        TextView tvCancel = mSortDialog.findViewById(R.id.tv_cancel);
        tvSortDes.setOnClickListener(this);
        tvSortAsc.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UISkipUtils.TO_EDIT && resultCode == UISkipUtils.FROM_EDIT) {
            page = 1;
            HttpService.loadCYListData(HttpApis.getSentList(), INIT_DATA, status, page, orderBy,
                    this);
        }
    }

    @Override
    public void onError(String msg, String code) {
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
        } else if (type == LOAD_MORE) {
            refreshList(dataStr);
        } else if (type == HttpApis.getDeleteMail().hashCode()) {
            showToast(jsonObject.optString("msg"));
            List<MailInfo> data = mAdapter.getData();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).mailId == mDeleteMailID) {
                    data.remove(i);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (type == HttpApis.getReceivedFocusMail().hashCode()){
            showToast(jsonObject.optString("msg"));
            List<MailInfo> data = mAdapter.getData();
            for (int i = 0; i < data.size(); i++) {
                MailInfo mailInfo = data.get(i);
                if (mailInfo.mailId == mFocusMailID) {
                    mailInfo.attention = !mailInfo.attention;
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
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
            if (mData.size() == 0) {
                mLlNoMail.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }else {
                mLlNoMail.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
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
