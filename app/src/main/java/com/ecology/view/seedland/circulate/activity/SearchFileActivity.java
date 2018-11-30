package com.ecology.view.seedland.circulate.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.adapter.AttachListAdapter;
import com.ecology.view.seedland.circulate.base.CirculateBaseActivity;
import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.modle.bean.FileInfo;
import com.ecology.view.seedland.circulate.modle.bean.SearchFileListInfo;
import com.ecology.view.seedland.circulate.modle.net.BaseResponse;
import com.ecology.view.seedland.circulate.modle.net.HttpService;
import com.ecology.view.seedland.circulate.modle.net.ResponseHandler;
import com.ecology.view.seedland.circulate.utils.UISkipUtils;
import com.ecology.view.seedland.circulate.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hch on 2018/2/2.
 */

public class SearchFileActivity extends CirculateBaseActivity implements BaseQuickAdapter
        .RequestLoadMoreListener, ResponseHandler {
    private EditText mEdtKeyword;
    private LinearLayout mLlNull;
    private RecyclerView mRvList;
    private AttachListAdapter mAdapter;
    private List<FileInfo> mData = new ArrayList<>();
    private int page = 0;
    private boolean mLoadMoreEnd = false;
    private boolean mLoadMoreFail = false;
    private String mKeyword;
    private String mPath;
    private String mPathType;
    private int contentSize = 0;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_search_file;
    }

    @Override
    public void initView() {
        initStatusBar();
        initList();
        mEdtKeyword = findView(R.id.edt_keyword);
        mLlNull = findView(R.id.ll_null);
    }

    private void initStatusBar() {
        LinearLayout llTopBar = findView(R.id.ll_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        llTopBar.setPadding(0, statusBarHeight + Global.dp2px(10), 0, Global.dp2px(10));
    }

    private void initList() {
        mRvList = findView(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AttachListAdapter(mData);
        mRvList.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mAdapter.setOnLoadMoreListener(this, mRvList);
        mEdtKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mKeyword = mEdtKeyword.getText().toString();
                    showDelayDialog();
                    HttpService.searchFile(INIT_DATA, page, mKeyword,mPath,mPathType, SearchFileActivity.this);
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
                                    HttpService.searchFile(LOAD_MORE, page, mKeyword,mPath,mPathType,SearchFileActivity.this);
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
                List<FileInfo> data = adapter.getData();
                FileInfo fileInfo = data.get(position);
                int itemType = fileInfo.getItemType();
                if (itemType == FileInfo.TYPE_DIR) {
                    UISkipUtils.skipToFileListActivity(SearchFileActivity.this, fileInfo.path, fileInfo.pathType,
                            fileInfo.path);
                } else if (itemType == FileInfo.TYPE_FILE) {
                    fileInfo.isSelected = !fileInfo.isSelected;
                    if (fileInfo.isSelected) {
                        Global.sSelectedFiles.add(fileInfo);
                    } else {
                        Global.sSelectedFiles.remove(fileInfo);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mPath = intent.getStringExtra("PATH");
        mPathType = intent.getStringExtra("PATH_TYPE");
    }

    @Override
    public void onClick(View v, int id) {
        switch (id) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.tv_confirm:
                setResult(UISkipUtils.FROM_SEARCH);
                finish();
                break;
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
        SearchFileListInfo data = Utils.parseJson(dataStr, SearchFileListInfo.class);
        if (data != null) {
//            int total_size = data.total_size;
//            int content_size = data.content_size;
//            contentSize += content_size;
//            if (contentSize >= total_size) {
//                mLoadMoreEnd = true;
//                mAdapter.loadMoreEnd();
//            }
            int end = data.totalPage;
            if (page + 1 == end) {
                mLoadMoreEnd = true;
                mAdapter.loadMoreEnd();
            }
            List<FileInfo> list = data.content;
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
        }else {
            mLoadMoreEnd = true;
            mAdapter.loadMoreEnd();
        }
    }
}
