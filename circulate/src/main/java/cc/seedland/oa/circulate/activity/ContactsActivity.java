package cc.seedland.oa.circulate.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.json.JSONObject;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.adapter.ContactsAdapter;
import cc.seedland.oa.circulate.base.CirculateBaseActivity;
import cc.seedland.oa.circulate.global.Constants;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.ContactsMultiInfo;

import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.utils.LogUtil;
import cc.seedland.oa.circulate.utils.PreferenceUtils;
import cc.seedland.oa.circulate.utils.ReceivessCache;
import cc.seedland.oa.circulate.utils.UISkipUtils;
import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.LimitDialog;
import cc.seedland.oa.circulate.view.MyToolbar;
import cc.seedland.oa.circulate.view.QuickIndexBar;

import java.io.Serializable;
import java.util.ArrayList;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class ContactsActivity extends CirculateBaseActivity {
    private List<ContactsMultiInfo> data = new ArrayList<>();
    private ContactsAdapter mAdapter;
    private QuickIndexBar mQuickIndexBar;
    private TextView mTvLetter;
    private RecyclerView mRvList;
    private TextView mTvSelected;
    private List<UserInfo> mSelectedUserList = new ArrayList<>();
    private long mMailId;
    private LimitDialog mLimitDialog;
    private LinearLayoutManager mLayoutManager;
    private int mGroupType; //0:组织架构  1:公用组
    private int mReceiverCount;
    private int mType;
    private List<String> mUserIdList;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_contacts;
    }

    @Override
    public void initView() {
        initStatusBar();
        initToolbar();
        initList();
        mQuickIndexBar = findView(R.id.quick_index_bar);
        mTvLetter = findView(R.id.tv_init_letter);
        mQuickIndexBar.setTextView(mTvLetter);
        mTvSelected = findView(R.id.tv_selected);
    }

    private void initList() {
        mRvList = findView(R.id.rv_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLayoutManager);
        mAdapter = new ContactsAdapter(data);
        mRvList.setAdapter(mAdapter);
    }

    private void initToolbar() {
        MyToolbar toolbar = findView(R.id.toolbar);
        toolbar.setBackClickListener(this);
        toolbar.setTitle("选择联系人");
        toolbar.setRightText("确认");
        toolbar.setOnRightTextClickListener(this);
    }

    private void initStatusBar() {
        FrameLayout flTopBar = findView(R.id.fl_top_bar);
        int statusBarHeight = Global.getStatusBarHeight(this);
        flTopBar.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void initListener() {
        mQuickIndexBar.setOnLetterSelectedListener(new QuickIndexBar.OnLetterSelectedListener() {
            @Override
            public void onLetterSelected(String letter) {
                for (int i = 0; i < data.size(); i++) {
                    ContactsMultiInfo bean = data.get(i);
                    if (letter.equals(bean.getInitLetter())) {
                        // 列表滚动到对应的位置
//                        mRvList.scrollToPosition(i);
                        mLayoutManager.scrollToPositionWithOffset(i, 0);
                        mLayoutManager.setStackFromEnd(false);
                        return;
                    }
                }
            }
        });
        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ContactsMultiInfo> data = adapter.getData();
                ContactsMultiInfo contactsMultiInfo = data.get(position);
                int itemType = contactsMultiInfo.getItemType();
                if (itemType == ContactsMultiInfo.CONTENT) {
                    if (mSelectedUserList.size() < Constants.selectLimit) {//点击选择联系人，私人组下面的List
                        contactsMultiInfo.isSelected = !contactsMultiInfo.isSelected;
                        refreshSelected(data);
                    } else {
                        if (contactsMultiInfo.isSelected) {
                            contactsMultiInfo.isSelected = !contactsMultiInfo.isSelected;
                            refreshSelected(data);
                        } else {
                            showLimitDialog();
                            return;
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.ll_search) {
                    UISkipUtils.skipToSearchPeopleActivity(ContactsActivity.this,
                            mSelectedUserList);
                } else if (id == R.id.ll_organization) {
                    mGroupType = 0;
                    UISkipUtils.skipToOrganizationActivity(ContactsActivity.this,
                            mSelectedUserList, mGroupType, mReceiverCount);
                } else if (id == R.id.ll_common_group) {
                    mGroupType = 1;
                    UISkipUtils.skipToOrganizationActivity(ContactsActivity.this,
                            mSelectedUserList, mGroupType, mReceiverCount);
                } else if (id == R.id.ll_private_group) {
                    mGroupType = 2;
                    UISkipUtils.skipToOrganizationActivity(ContactsActivity.this,
                            mSelectedUserList, mGroupType, mReceiverCount);
                }
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
    private boolean isFirstSelect = true;

    private void refreshSelected(List<ContactsMultiInfo> data) {
        int addCount = 0;
//        mSelectedUserList.clear();
//        List<String> selectedUserId = new ArrayList<>();
//        for (UserInfo userInfo : mSelectedUserList) {
//            selectedUserId.add(userInfo.userId);
//        }
        if (mSelectedUserList.size() > 0) {
            if (isFirstSelect) {
//                addCount = mSelectedUserList.size();
                addCount = mReceiverCount;
                isFirstSelect = false;
            } else {
                addCount = mReceiverCount;
            }
        } else {
            isFirstSelect = false;
            addCount = mReceiverCount;
        }
        for (ContactsMultiInfo datum : data) {
            UserInfo userInfo = datum.userInfo;
            if (userInfo != null) {
                String userId = userInfo.userId;
                if (mUserIdList.contains(userId)) {
                    if (!datum.isSelected) {
                        addCount--;
                        mUserIdList.remove(userId);
                        mSelectedUserList.remove(userInfo);
                    }
                } else {
                    if (datum.isSelected) {
                        addCount++;
                        mUserIdList.add(userId);
                        mSelectedUserList.add(userInfo);
                    }
                }
            }
        }
//        for (ContactsMultiInfo datum : data) {
//            boolean isContainer = false;
//            Iterator<UserInfo> iterator = mSelectedUserList.iterator();
//            UserInfo userInfo = datum.userInfo;
//            if (userInfo != null) {
//                while (iterator.hasNext()) {
//                    UserInfo next = iterator.next();
//                    if (next.userId.equals(userInfo.userId)) {
//                        if (!datum.isSelected) {
//                            addCount--;
//                            iterator.remove();
//                        }
//                        isContainer = true;
//                    }
//                }
//                if (datum.isSelected) {
//                    if (isContainer) {
//                        continue;
//                    }
//                    addCount++;
//                    mSelectedUserList.add(userInfo);
//                }
//            }
//        }
        mReceiverCount = addCount;
        mTvSelected.setText("(" + (addCount) + ")");
    }

    List<UserInfo> user_list;

    @Override
    protected void onDestroy() {
        ///TODO 530
        ReceivessCache.clear();
        super.onDestroy();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mMailId = intent.getLongExtra("MAIL_ID", -1);
        user_list = intent.getParcelableArrayListExtra("USER_LIST");
        mReceiverCount = intent.getIntExtra("RECEIVER_COUNT", 0);
        mType = intent.getIntExtra("type", 1);
        String userIds = intent.getStringExtra("USER_ID");
        String[] userIdArray = userIds.split(",");
        mUserIdList = new ArrayList(Arrays.asList(userIdArray));
//        user_list = ReceivessCache.receivess;
        if (user_list != null) {
            mSelectedUserList = user_list;
//            mReceiverCount = mSelectedUserList.size();
            mTvSelected.setText("(" + mReceiverCount + ")");
        } else {
            mTvSelected.setText("(" + mReceiverCount + ")");
        }
        List<ContactsMultiInfo> contentInfo = new ArrayList<>();
        String selectedUser = PreferenceUtils.getString(this, "SELECTED_USER");
        if (!TextUtils.isEmpty(selectedUser)) {
            List<UserInfo> userInfos = Utils.parseJsonArray(selectedUser, UserInfo.class);
//        List<UserInfo> userInfos = Global.sUserInfo;
//        //正常添加数据的方法
            if (userInfos != null) {
                for (int i = 0; i < userInfos.size(); i++) {
                    UserInfo userInfo = userInfos.get(i);
                    if (userInfo != null) {
                        ContactsMultiInfo contactsMultiInfo = new ContactsMultiInfo(ContactsMultiInfo.CONTENT);
                        contactsMultiInfo.setName(userInfo.lastName);
                        contactsMultiInfo.userInfo = userInfo;
                        if (mUserIdList != null && mUserIdList.size() > 0) {
                            if (mUserIdList.contains(userInfo.userId)) {
                                contactsMultiInfo.isSelected = true;
                                mSelectedUserList.add(userInfo);
                            }
                        }
//                        if (mSelectedUserList != null && mSelectedUserList.size() > 0) {
//                            for (UserInfo info : mSelectedUserList) {
//                                if (userInfo.userId.equals(info.userId)) {
//                                    contactsMultiInfo.isSelected = true;
//                                }
//                            }
//                        }
                        contentInfo.add(contactsMultiInfo);
                    }
                }
            }
        }
        data.add(0, new ContactsMultiInfo(ContactsMultiInfo.HEAD));
        data.addAll(contentInfo);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v, int id) {
        if (id == R.id.ll_selected) {
            UISkipUtils.skipToSelectedContactsActivity(this, mSelectedUserList, mMailId, mType);
        } else if (id == R.id.tv_right) {
            List<UserInfo> userInfos = new ArrayList<>();
            List<ContactsMultiInfo> data = mAdapter.getData();
            for (ContactsMultiInfo datum : data) {
                if (datum.getItemType() == ContactsMultiInfo.CONTENT && datum.isSelected) {
                    userInfos.add(datum.userInfo);
                }
            }
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("USER", (ArrayList<? extends Parcelable>) userInfos);
            setResult(UISkipUtils.FROM_EDIT, intent);
            finish();
        } else if (id == R.id.tv_btn) {
            mLimitDialog.dismiss();
        } else if (id == R.id.ll_back) {
            setResult(UISkipUtils.FROM_EDIT);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(UISkipUtils.FROM_EDIT);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UISkipUtils.TO_EDIT) {
            if (resultCode == UISkipUtils.FROM_EDIT) {
                //注释刷新已选人数逻辑
//                List<ContactsMultiInfo> listData = mAdapter.getData();
                List<UserInfo> selected = data.getParcelableArrayListExtra("DATA");
                ArrayList<String> deleteUserIdList = data.getStringArrayListExtra("DELETE_LIST");
                mReceiverCount = data.getIntExtra("COUNT", 0);
//                mReceiverCount = selected.size();
                mTvSelected.setText("(" + mReceiverCount + ")");
                if (deleteUserIdList.size() > 0) {
                    for (ContactsMultiInfo datum : this.data) {
                        if (datum.getItemType() == ContactsMultiInfo.CONTENT) {
                            String userId = datum.userInfo.userId;
                            if (deleteUserIdList.contains(userId)) {
                                datum.isSelected = false;
                            }
//                            if (mUserIdList.contains(userId)) {
//                                mUserIdList.remove(userId);
//                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
//                if (mType == 2) {
//                    if (deleteUserIdList.size() > 0) {
//                        for (ContactsMultiInfo datum : this.data) {
//                            if (datum.getItemType() == ContactsMultiInfo.CONTENT) {
//                                if (deleteUserIdList.contains(datum.userInfo.userId)) {
//                                    datum.isSelected = false;
//                                }
//                            }
//                        }
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }
//                for (ContactsMultiInfo listDatum : listData) {
//                    listDatum.isSelected = false;
//                    if (listDatum.getItemType() == ContactsMultiInfo.CONTENT) {
//                        for (UserInfo userInfo : selected) {
//                            if (userInfo.userId.equals(listDatum.userInfo.userId)) {
//                                listDatum.isSelected = true;
//                            }
//                        }
//                    }
//                }
//                refreshSelected(listData);
//                mAdapter.notifyDataSetChanged();
            } else if (resultCode == UISkipUtils.FROM_SELECTED) {
                List<UserInfo> selected = data.getParcelableArrayListExtra("DATA");
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("USER", (ArrayList<? extends Parcelable>) selected);
                setResult(UISkipUtils.FROM_EDIT, intent);
                finish();
            }
        }
    }
}
