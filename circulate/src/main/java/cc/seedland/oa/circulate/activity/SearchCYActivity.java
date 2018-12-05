package cc.seedland.oa.circulate.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.adapter.ReceiveAdapter;
import cc.seedland.oa.circulate.base.CirculateBaseActivity;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.MailInfo;
import cc.seedland.oa.circulate.modle.bean.SentCYListInfo;
import cc.seedland.oa.circulate.modle.net.BaseResponse;
import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.utils.UISkipUtils;
import cc.seedland.oa.circulate.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class SearchCYActivity extends CirculateBaseActivity implements ResponseHandler, BaseQuickAdapter
        .RequestLoadMoreListener {

    private EditText mEdtKeyword;
    private RecyclerView mRvList;
    private ReceiveAdapter mAdapter;
    private List<MailInfo> mData = new ArrayList<>();
    private int page = 1;
    private boolean mLoadMoreEnd = false;
    private boolean mLoadMoreFail = false;
    private String mKeyword;
    private LinearLayout mLlNull;
    private String mApi="";
    private int mType;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        initStatusBar();
        initList();
        mEdtKeyword = findView(R.id.edt_keyword);
        mLlNull = findView(R.id.ll_null);
        TextView tvNull = findView(R.id.tv_null);
        ImageView ivNullIcon = findView(R.id.iv_null_icon);
        ivNullIcon.setImageResource(R.drawable.icon_null_cy);
        tvNull.setText("没有搜索到符合条件的传阅");
    }

    private void initList() {
        mRvList = findView(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ReceiveAdapter(R.layout.item_received_list, mData,true);
        mRvList.setAdapter(mAdapter);
    }

    private void initStatusBar() {
        LinearLayout llTopBar = findView(R.id.ll_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        llTopBar.setPadding(0, statusBarHeight + Global.dp2px(10), 0, Global.dp2px(10));
    }

    @Override
    public void initListener() {
        mAdapter.setOnLoadMoreListener(this, mRvList);
        mEdtKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mKeyword = mEdtKeyword.getText().toString();
                    HttpService.searchCYList(mApi,INIT_DATA, page, mKeyword, SearchCYActivity.this);
                    InputMethodManager im = (InputMethodManager) getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
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
//                                    loadListData(LOAD_MORE, page);
                                    HttpService.searchCYList(mApi,LOAD_MORE, page, mKeyword, SearchCYActivity.this);
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

        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<MailInfo> data = adapter.getData();
                MailInfo mailInfo = data.get(position);
                if (mType == 4) {
                    UISkipUtils.skipToCreateMailActivity(SearchCYActivity.this, mailInfo);
                }else {
                    UISkipUtils.skipToCYDetailActivity(SearchCYActivity.this, mailInfo.mailId,mType);
                }
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mApi = intent.getStringExtra("API");
        mType = intent.getIntExtra("TYPE", 0);
    }

    @Override
    public void onClick(View v, int id) {
        if(id == R.id.tv_cancel) {
            finish();
        }
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
            if (list == null || list.size() == 0) {
                mLlNull.setVisibility(View.VISIBLE);
                mRvList.setVisibility(View.GONE);
                return;
            }else {
                mLlNull.setVisibility(View.GONE);
                mRvList.setVisibility(View.VISIBLE);
            }
            mData.addAll(list);
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
