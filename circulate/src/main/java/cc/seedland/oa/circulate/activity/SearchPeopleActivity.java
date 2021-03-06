package cc.seedland.oa.circulate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.adapter.SearchPeopleAdapter;
import cc.seedland.oa.circulate.base.CirculateBaseActivity;
import cc.seedland.oa.circulate.global.Constants;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.ContactsMultiInfo;
import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.utils.PreferenceUtils;
import cc.seedland.oa.circulate.utils.UISkipUtils;
import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.LimitDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class SearchPeopleActivity extends CirculateBaseActivity {

    private EditText mEdtKeyword;
    private RecyclerView mRvList;
    private SearchPeopleAdapter mAdapter;
    private List<UserInfo> mSelectedData;
    private List<UserInfo> mUserInfos = new ArrayList<>();
    private List<UserInfo> mData = new ArrayList<>();
    private LinearLayout mLlNull;
    private LimitDialog mLimitDialog;

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
        TextView tvCancel = findView(R.id.tv_cancel);
        tvCancel.setText("确认");
    }

    private void initList() {
        mRvList = findView(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchPeopleAdapter(R.layout.item_contacts_contact, mData);
        mRvList.setAdapter(mAdapter);
    }

    private void initStatusBar() {
        LinearLayout llTopBar = findView(R.id.ll_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        llTopBar.setPadding(0, statusBarHeight + Global.dp2px(10), 0, Global.dp2px(10));
    }

    @Override
    public void initListener() {
        mEdtKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = mEdtKeyword.getText().toString();
                    mData.clear();
                    for (UserInfo userInfo : mUserInfos) {
                        if (userInfo.lastName.contains(keyword)) {
                            mData.add(userInfo);
                        }
                    }
                    if (mData.size() == 0) {
                        mLlNull.setVisibility(View.VISIBLE);
                        mRvList.setVisibility(View.GONE);
                    } else {
                        mLlNull.setVisibility(View.GONE);
                        mRvList.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                    }
                    InputMethodManager im = (InputMethodManager) getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }
        });

        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<UserInfo> data = adapter.getData();
                UserInfo userInfo = data.get(position);
                if (mSelectedData.size() < Constants.selectLimit) {
                    userInfo.isSelected = !userInfo.isSelected;
                    refreshSelected(data);
                } else {
                    if (userInfo.isSelected) {
                        userInfo.isSelected = !userInfo.isSelected;
                        refreshSelected(data);
                    } else {
                        showLimitDialog();
                        return;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showLimitDialog() {
        if (mLimitDialog == null)
            mLimitDialog = new LimitDialog(this);
        mLimitDialog.show();
        mLimitDialog.findViewById(R.id.tv_btn).setOnClickListener(this);
    }

    //刷新选中人数
    private void refreshSelected(List<UserInfo> data) {
//        mSelectedData.clear();
        if (mSelectedData.size() == 0) {
            List<UserInfo> needAdd = new ArrayList<>();
            for (UserInfo datum : data) {
                if (datum.isSelected) {
                    needAdd.add(datum);
//                    mSelectedData.add(datum);
                }
            }
            mSelectedData.addAll(needAdd);
        } else {
            List<UserInfo> needAdd = new ArrayList<>();
            List<UserInfo> needDel = new ArrayList<>();
//            for (UserInfo datum : data) {
//                boolean isContainer = false;
//                Iterator<UserInfo> iterator = mSelectedData.iterator();
//                while (iterator.hasNext()) {
//                    UserInfo selectedDatum = iterator.next();
//                    if (datum.userId.equals(selectedDatum.userId)) {
//                        isContainer = true;
//                        if (!datum.isSelected) {
////                            iterator.remove();
//                            if (needDel.size()>0) {
//
//                            }
//                        }
//                    }
//                }
//            }
            for (UserInfo selectedDatum : mSelectedData) {
                for (UserInfo datum : data) {
                    if (selectedDatum.userId.equals(datum.userId)) {
                        if (!datum.isSelected) {
//                            mSelectedData.remove(selectedDatum);
                            if (needDel.size() > 0) {
                                boolean isContainer = false;
                                for (UserInfo userInfo : needDel) {
                                    if (userInfo.userId.equals(selectedDatum.userId)) {
                                        isContainer = true;
                                    }
                                }
                                if (!isContainer) {
                                    needDel.add(selectedDatum);
                                }
                            } else {
                                needDel.add(selectedDatum);
                            }
                        }
                    } else {
                        if (datum.isSelected) {
//                            mSelectedData.add(datum);
                            if (needAdd.size() > 0) {
                                boolean isContainer = false;
                                for (UserInfo userInfo : needAdd) {
                                    if (userInfo.userId.equals(datum.userId)) {
                                        isContainer = true;
                                    }
                                }
                                for (UserInfo userInfo : mSelectedData) {
                                    if (userInfo.userId.equals(datum.userId)) {
                                        isContainer = true;
                                    }
                                }
                                if (!isContainer) {
                                    needAdd.add(datum);
                                }
                            } else {
                                boolean isContainer = false;
                                for (UserInfo userInfo : mSelectedData) {
                                    if (userInfo.userId.equals(datum.userId)) {
                                        isContainer = true;
                                    }
                                }
                                if (!isContainer) {
                                    needAdd.add(datum);
                                }
                            }
                        }
                    }
                }
            }
            mSelectedData.removeAll(needDel);
            mSelectedData.addAll(needAdd);
        }
    }

    @Override
    public void initData() {
        List<UserInfo> userInfoList = Global.sUserInfo==null?new ArrayList<UserInfo>():Global.sUserInfo;
        Intent intent = getIntent();
        for (UserInfo userInfo : (Global.sUserInfo)) {
            UserInfo user = new UserInfo();
            user.departmentId = userInfo.departmentId;
            user.lastName = userInfo.lastName;
            user.loginId = userInfo.loginId;
            user.subCompanyId = userInfo.subCompanyId;
            user.userId = userInfo.userId;
            user.userMessageId = userInfo.userMessageId;
            user.workCode = userInfo.workCode;
            user.subcompanyName = userInfo.subcompanyName;
            user.departmentName = userInfo.departmentName;
            mUserInfos.add(user);
        }
        mSelectedData = (List<UserInfo>) intent.getSerializableExtra("SELECTED_DATA");
        for (UserInfo userInfo : mUserInfos) {
            for (UserInfo selectedDatum : mSelectedData) {
                if (userInfo.userId.equals(selectedDatum.userId)) {
                    userInfo.isSelected = true;
                }
            }
        }

    }

    @Override
    public void onClick(View v, int id) {
        if (id == R.id.tv_cancel) {
            refreshSelectedUser(mSelectedData);
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("DATA", (ArrayList<? extends Parcelable>) mSelectedData);
            setResult(UISkipUtils.FROM_SELECTED, intent);
            finish();
        } else if (id == R.id.tv_btn) {
            mLimitDialog.dismiss();
        }
    }

    private void refreshSelectedUser(List<UserInfo> selectedNode) {
        String selectedUser = PreferenceUtils.getString(this, "SELECTED_USER");
        List<UserInfo> userInfos = null;
        if (!TextUtils.isEmpty(selectedUser)) {
            userInfos = Utils.parseJsonArray(selectedUser, UserInfo.class);
        }
        if (userInfos == null) {
            if (selectedNode.size()>10) {
                List<UserInfo> userInfoList = selectedNode.subList(0, 10);
                userInfos = userInfoList;
            }else {
                userInfos = selectedNode;
            }
        } else {
            List<String> userIds = new ArrayList<>();
            for (UserInfo userInfo : userInfos) {
                userIds.add(userInfo.userId);
            }
            for (UserInfo userInfo : selectedNode) {
                if (!userIds.contains(userInfo.userId)) {
                    if (userInfos.size() >= 10) {
                        userInfos.remove(0);
                    }
                    userInfos.add(userInfo);
                }
            }
        }
        String newSelectedUser = JSONArray.toJSONString(userInfos);
        PreferenceUtils.putString(this, "SELECTED_USER", newSelectedUser);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("DATA", (Serializable) mSelectedData);
        setResult(UISkipUtils.FROM_EDIT, intent);
        finish();
    }
}
