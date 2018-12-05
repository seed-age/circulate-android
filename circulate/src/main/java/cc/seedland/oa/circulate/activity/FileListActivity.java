package cc.seedland.oa.circulate.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.adapter.AttachListAdapter;
import cc.seedland.oa.circulate.base.CirculateBaseActivity;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.FileInfo;
import cc.seedland.oa.circulate.modle.bean.FileListInfo;
import cc.seedland.oa.circulate.modle.net.BaseResponse;
import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.utils.LogUtil;
import cc.seedland.oa.circulate.utils.UISkipUtils;
import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.MyToolbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class FileListActivity extends CirculateBaseActivity implements ResponseHandler,
        BaseQuickAdapter
                .RequestLoadMoreListener {

    private RecyclerView mRvList;
    private AttachListAdapter mAdapter;
    private List<FileInfo> mData = new ArrayList<>();
    private int page = 0;
    private boolean mLoadMoreEnd = false;
    private boolean mLoadMoreFail = false;
    private String mApi = "";
    private MyToolbar mToolbar;
    private String mType;
    private String mPath;
    private LinearLayout mLlSearch;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_search_attach;
    }

    @Override
    public void initView() {
        Global.sFileListInstance.add(this);
        initToolbar();
        initStatusBar();
        initList();
        mLlSearch = findView(R.id.lly_search);
    }

    private void initToolbar() {
        mToolbar = findView(R.id.toolbar);
//        toolbar.setOnBackClickListenerWithResult(this,UISkipUtils.FROM_EDIT,null);
        mToolbar.setOnBackClickListener(this);
        mToolbar.setRightText("确认");
        mToolbar.setOnRightTextClickListener(this);
    }

    private void initList() {
        mRvList = findView(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AttachListAdapter(mData);
        mRvList.setAdapter(mAdapter);
    }

    private void initStatusBar() {
        FrameLayout flTopBar = findView(R.id.fl_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        flTopBar.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void initListener() {
        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<FileInfo> data = adapter.getData();
                FileInfo fileInfo = data.get(position);
                int itemType = fileInfo.getItemType();
                if (itemType == FileInfo.TYPE_DIR) {
                    UISkipUtils.skipToFileListActivity(FileListActivity.this, fileInfo.desc,
                            fileInfo.pathType,
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
        mAdapter.setOnLoadMoreListener(this, mRvList);

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
                                    LogUtil.e("page = " + page);
                                    HttpService.loadFileList(LOAD_MORE, page, TextUtils.isEmpty
                                            (mPath) ? "" : mPath, mType, FileListActivity.this);
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
        String dir_name = intent.getStringExtra("dir_name");
        mType = intent.getStringExtra("type");
        mPath = intent.getStringExtra("path");
        mToolbar.setTitle(dir_name);
        if ("ent".equals(mType)) {
            mLlSearch.setVisibility(View.GONE);
        }else {
            mLlSearch.setVisibility(View.VISIBLE);
        }
        HttpService.loadFileList(INIT_DATA, page, TextUtils.isEmpty(mPath) ? "" : mPath, mType,
                this);
    }

    @Override
    public void onClick(View v, int id) {
        if(id == R.id.tv_cancel) {
            finish();
        }else if(id == R.id.tv_right) {
            for (FileListActivity fileListActivity : Global.sFileListInstance) {
                fileListActivity.finish();
            }
        }else if(id == R.id.tv_search) {
            UISkipUtils.skipToSearchFileActivity(this, mPath, mType);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UISkipUtils.TO_EDIT) {
            if (resultCode == UISkipUtils.FROM_SEARCH) {
                for (FileListActivity fileListActivity : Global.sFileListInstance) {
                    fileListActivity.finish();
                }
            } else if (resultCode == UISkipUtils.FROM_EDIT) {
                List<FileInfo> fileInfos = mAdapter.getData();
                List<FileInfo> selectedFiles = Global.sSelectedFiles;
                for (FileInfo fileInfo : fileInfos) {
                    for (FileInfo selectedFile : selectedFiles) {
                        if (selectedFile.neid == fileInfo.neid) {
                            fileInfo.isSelected = true;
                        } else {
                            fileInfo.isSelected = false;
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
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
        FileListInfo data = Utils.parseJson(dataStr, FileListInfo.class);
        if (data != null) {
            int end = data.totalPage;
            if (page + 1 == end) {
                mLoadMoreEnd = true;
                mAdapter.loadMoreEnd();
            }
            List<FileInfo> list = data.list;
            mData.addAll(list);
            mAdapter.notifyDataSetChanged();
        } else {
            mLoadMoreEnd = true;
            mAdapter.loadMoreEnd();
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
    protected void onDestroy() {
        super.onDestroy();
        Global.sFileListInstance.remove(this);
    }
}
