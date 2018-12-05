package cc.seedland.oa.circulate.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.adapter.CommentListAdapter;
import cc.seedland.oa.circulate.base.NoImmerseBaseActivity;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.DiscussListInfo;
import cc.seedland.oa.circulate.modle.bean.DiscussesInfo;
import cc.seedland.oa.circulate.modle.bean.MailInfo;
import cc.seedland.oa.circulate.modle.net.BaseResponse;
import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.MyToolbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class CommentListActivity extends NoImmerseBaseActivity implements ResponseHandler,
        BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView mRvList;
    private CommentListAdapter mAdapter;
    private EditText mEdtComment;
    private int mMailId;
    private int page = 1;
    private boolean mLoadMoreEnd = false;
    private boolean mLoadMoreFail = false;
    private List<DiscussesInfo> mData = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefresh;
    private String mApi;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_comment_list;
    }

    @Override
    public void initView() {
//        initStatusBar();
        initToolbar();
        initList();
        mEdtComment = findView(R.id.edt_comment);
        mSwipeRefresh = findView(R.id.swipe_refresh);
    }

    private void initList() {
        mRvList = findView(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommentListAdapter(R.layout.item_comment_list, mData);
        mRvList.setAdapter(mAdapter);
    }

    private void initToolbar() {
        MyToolbar toolbar = findView(R.id.toolbar);
        toolbar.setOnBackClickListener(this);
        toolbar.setTitle("评论列表");
    }

    private void initStatusBar() {
        FrameLayout flTopBar = findView(R.id.fl_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        flTopBar.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void initListener() {
        mAdapter.setOnLoadMoreListener(this, mRvList);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                HttpService.loadDiscussList(INIT_DATA, mMailId, page, CommentListActivity.this);
            }
        });
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                    HttpService.loadDiscussList(LOAD_MORE, mMailId, page,
                                            CommentListActivity.this);
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
        mEdtComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    String discussContent = mEdtComment.getText().toString();
                    if (TextUtils.isEmpty(discussContent)) {
                        showToast("评论内容为空...");
                        return true;
                    }
                    HttpService.postDiscuss(mApi, mMailId, discussContent, CommentListActivity
                            .this);
                }
                return true;
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mApi = intent.getStringExtra("API");
        MailInfo mailInfo = (MailInfo) intent.getSerializableExtra("DATA");
        mMailId = mailInfo.mailId;
        HttpService.loadDiscussList(INIT_DATA, mMailId, page, this);
    }

    @Override
    public void onClick(View v, int id) {
    }

    @Override
    public void onError(String msg, String code) {
        mSwipeRefresh.setRefreshing(false);
        showToast(msg);
    }

    @Override
    public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
        mSwipeRefresh.setRefreshing(false);
        hideDelayDialog();
        int type = response.getType();
        String dataStr = jsonObject.optString("data");
        if (type == INIT_DATA) {
            mData.clear();
            refreshList(dataStr);
            mRvList.scrollToPosition(mAdapter.getItemCount() - 1);
        } else if (type == LOAD_MORE) {
            refreshList(dataStr);
        } else if (type == mApi.hashCode()) {
            page = 1;
            HttpService.loadDiscussList(INIT_DATA, mMailId, page, this);
            mEdtComment.setText("");
        }
    }

    private void refreshList(String dataStr) {
        DiscussListInfo data = Utils.parseJson(dataStr, DiscussListInfo.class);
        if (data != null) {
            mLoadMoreEnd = data.lastPage;
            if (mLoadMoreEnd) {
                mAdapter.loadMoreEnd();
            }
            List<DiscussesInfo> discussInfo = data.list;
            mData.addAll(discussInfo);
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
