package cc.seedland.oa.circulate.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

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
import cc.seedland.oa.circulate.utils.UISkipUtils;
import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.MyToolbar;
import cc.seedland.oa.circulate.view.SimpleItemDecoration;
import cc.seedland.oa.circulate.view.SwipeLayoutManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/6 0006.
 */

public class RemovedListActivity extends CirculateBaseActivity implements ResponseHandler,
        BaseQuickAdapter.RequestLoadMoreListener {

    private SentAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<MailInfo> mData = new ArrayList<>();
    private MyToolbar mToolbar;
    private LinearLayout mLlTabBar;
    private int page = 1;
    private boolean mLoadMoreEnd = false;
    private boolean mLoadMoreFail = false;
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
        mLlTabBar.setVisibility(View.GONE);
        mLlNoMail = findView(R.id.ll_no_mail);
    }

    private void initList() {
        mRecyclerView = findView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SentAdapter(R.layout.item_received_list, mData,true);
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

    private void initToolbar() {
        mToolbar = findView(R.id.toolbar);
        mToolbar.setTitle("已删除");
        mToolbar.setOnBackClickListener(this);
        mToolbar.setRightFirstIvImg(R.drawable.icon_search);
        mToolbar.setRightFirstIvOnClickListener(this);
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
                                    HttpService.loadRemovedList(LOAD_MORE,page, RemovedListActivity.this);
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
                UISkipUtils.skipToCYDetailActivity(RemovedListActivity.this, mailInfo.mailId,2);
            }
        });
    }

    @Override
    public void initData() {
        HttpService.loadRemovedList(INIT_DATA,page, this);
    }

    @Override
    public void onClick(View v, int id) {
        if(id == R.id.fl_right_first_ic) {
            UISkipUtils.skipToSearchCYActivity(this, HttpApis.getSearchRemovedList(),2);
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
        switch (type) {
            case INIT_DATA:
                mData.clear();
                refreshList(dataStr);
                break;
            case LOAD_MORE:
                refreshList(dataStr);
                break;
        }
    }

    private void refreshList(String dataStr) {
        SentCYListInfo data = Utils.parseJson(dataStr, SentCYListInfo.class);
        if (data != null) {
            mLoadMoreEnd = data.lastPage;
            if (mLoadMoreEnd) {
                mAdapter.loadMoreEnd();
            }
            List<MailInfo> list = data.list;
            mData.addAll(list);
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
