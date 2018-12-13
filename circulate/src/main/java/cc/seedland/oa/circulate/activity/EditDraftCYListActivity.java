package cc.seedland.oa.circulate.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.adapter.EditSentCYListAdapter;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/6 0006.
 */

public class EditDraftCYListActivity extends CirculateBaseActivity implements ResponseHandler,
        BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView mRecyclerView;
    private EditSentCYListAdapter mAdapter;
    private int page = 1;
    private boolean mLoadMoreEnd = false;
    private boolean mLoadMoreFail = false;
    private List<MailInfo> mData = new ArrayList<>();
    private boolean isSelectAll = false;
    private int mStatus;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_edit_cylist;
    }

    @Override
    public void initView() {
        initStatusBar();
        initToolbar();
        initList();
        LinearLayout llFocus = findView(R.id.ll_focus);
        llFocus.setVisibility(View.GONE);
        ImageView ivRemove = findView(R.id.iv_remove);
        TextView tvRemove = findView(R.id.tv_remove);
        ivRemove.setImageResource(R.drawable.icon_trash);
        tvRemove.setText("删除");
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
                List<MailInfo> data = adapter.getData();
                MailInfo listBean = data.get(position);
                listBean.isSelected = !listBean.isSelected;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<MailInfo> data = adapter.getData();
                int id = view.getId();
                if(id == R.id.fl_selector) {
                    MailInfo listBean = data.get(position);
                    listBean.isSelected = !listBean.isSelected;
                    adapter.notifyDataSetChanged();
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
                                    HttpService.loadDraftList(LOAD_MORE,page, EditDraftCYListActivity.this);
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
        showDelayDialog();
        HttpService.loadDraftList(INIT_DATA,page, this);
    }

    @Override
    public void onClick(View v, int id) {
        if(id == R.id.tv_right){
            List<MailInfo> data = mAdapter.getData();
            isSelectAll = !isSelectAll;
            for (MailInfo datum : data) {
                datum.isSelected = isSelectAll;
            }
            mAdapter.notifyDataSetChanged();
        }else if(id == R.id.ll_remove) {
            removeAll();
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
        HttpService.deleteDraft(removeMailId, this);
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
        }else if (type == LOAD_MORE) {
            refreshList(dataStr);
        }else if (type == HttpApis.getDeleteDraft().hashCode()) {
            showToast(jsonObject.optString("msg"));
//            page = 1;
//            HttpService.loadDraftList(INIT_DATA,page, this);
            setResult(UISkipUtils.FROM_EDIT);
            page = 1;
            HttpService.loadDraftList(INIT_DATA,page, this);
//            finish();
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
