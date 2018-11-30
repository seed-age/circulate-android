package com.ecology.view.seedland.circulate.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.adapter.ContactsAdapter;
import com.ecology.view.seedland.circulate.base.CirculateBaseActivity;
import com.ecology.view.seedland.circulate.global.Constants;
import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.modle.bean.ContactsMultiInfo;
import com.ecology.view.seedland.circulate.modle.bean.UserInfo;
import com.ecology.view.seedland.circulate.utils.UISkipUtils;
import com.ecology.view.seedland.circulate.view.LimitDialog;
import com.ecology.view.seedland.circulate.view.MyToolbar;
import com.ecology.view.seedland.circulate.view.QuickIndexBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        toolbar.setOnBackClickListener(this);
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
//                        mRvList.smoothScrollToPosition(i);
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
                    if (mSelectedUserList.size() < Constants.selectLimit) {
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
                            mSelectedUserList, mGroupType);
                } else if (id == R.id.ll_common_group) {
                    mGroupType = 1;
                    UISkipUtils.skipToOrganizationActivity(ContactsActivity.this,
                            mSelectedUserList, mGroupType);
                }else if (id == R.id.ll_private_group) {
                    mGroupType = 2;
                    UISkipUtils.skipToOrganizationActivity(ContactsActivity.this,
                            mSelectedUserList, mGroupType);
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
    private void refreshSelected(List<ContactsMultiInfo> data) {
        int userCount = 0;
        mSelectedUserList.clear();
        for (ContactsMultiInfo datum : data) {
            if (datum.isSelected) {
                mSelectedUserList.add(datum.userInfo);
                userCount++;
            }
        }
        mTvSelected.setText("(" + userCount + ")");
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mMailId = intent.getLongExtra("MAIL_ID", -1);
        List<UserInfo> user_list = (List<UserInfo>) intent.getSerializableExtra("USER_LIST");
        if (user_list != null)
            mSelectedUserList = user_list;
        List<ContactsMultiInfo> contentInfo = new ArrayList<>();
        List<UserInfo> userInfos = Global.sUserInfo;
        //正常添加数据的方法
        if (userInfos != null) {
            for (int i = 0; i < userInfos.size(); i++) {
                UserInfo userInfo = userInfos.get(i);
                if (userInfo != null) {
                    ContactsMultiInfo contactsMultiInfo = new ContactsMultiInfo(ContactsMultiInfo
                            .CONTENT);

                    contactsMultiInfo.setName(userInfo.lastName);
                    contactsMultiInfo.userInfo = userInfo;
                    if (mSelectedUserList != null && mSelectedUserList.size() > 0) {
                        for (UserInfo info : mSelectedUserList) {
                            if (userInfo.userId.equals(info.userId)) {
                                contactsMultiInfo.isSelected = true;
                            }
                        }
                    }
                    contentInfo.add(contactsMultiInfo);
                }
            }
        }
        refreshSelected(contentInfo);
        //添加测试数据
//        for (int i = 0; i < 500; i++) {
//            ContactsMultiInfo contactsMultiInfo = new ContactsMultiInfo(ContactsMultiInfo.CONTENT);
//            contactsMultiInfo.setName("阿" + i);
//            UserInfo userInfo = new UserInfo();
//            userInfo.lastName = "阿" + i;
//            userInfo.userId = i+"";
//            userInfo.fullName = "阿";
//            userInfo.deptFullname = "阿";
//            contentInfo.add(contactsMultiInfo);
//        }
//        for (int i = 0; i < 500; i++) {
//            ContactsMultiInfo contactsMultiInfo = new ContactsMultiInfo(ContactsMultiInfo.CONTENT);
//            contactsMultiInfo.setName("陈" + i);
//            UserInfo userInfo = new UserInfo();
//            userInfo.lastName = "陈" + i;
//            userInfo.userId = i+"";
//            userInfo.fullName = "陈";
//            userInfo.deptFullname = "陈";
//            contentInfo.add(contactsMultiInfo);
//        }
//        for (int i = 0; i < 500; i++) {
//            ContactsMultiInfo contactsMultiInfo = new ContactsMultiInfo(ContactsMultiInfo.CONTENT);
//            contactsMultiInfo.setName("何" + i);
//            UserInfo userInfo = new UserInfo();
//            userInfo.lastName = "何" + i;
//            userInfo.userId = i+"";
//            userInfo.fullName = "何";
//            userInfo.deptFullname = "何";
//            contentInfo.add(contactsMultiInfo);
//        }
//        for (int i = 0; i < 500; i++) {
//            ContactsMultiInfo contactsMultiInfo = new ContactsMultiInfo(ContactsMultiInfo.CONTENT);
//            contactsMultiInfo.setName("万" + i);
//            UserInfo userInfo = new UserInfo();
//            userInfo.lastName = "万" + i;
//            userInfo.userId = i+"";
//            userInfo.fullName = "万";
//            userInfo.deptFullname = "万";
//            contentInfo.add(contactsMultiInfo);
//        }
//        for (int i = 0; i < 500; i++) {
//            ContactsMultiInfo contactsMultiInfo = new ContactsMultiInfo(ContactsMultiInfo.CONTENT);
//            contactsMultiInfo.setName("有" + i);
//            UserInfo userInfo = new UserInfo();
//            userInfo.lastName = "有" + i;
//            userInfo.userId = i+"";
//            userInfo.fullName = "有";
//            userInfo.deptFullname = "有";
//            contentInfo.add(contactsMultiInfo);
//        }
//        for (int i = 0; i < 500; i++) {
//            ContactsMultiInfo contactsMultiInfo = new ContactsMultiInfo(ContactsMultiInfo.CONTENT);
//            contactsMultiInfo.setName("这" + i);
//            UserInfo userInfo = new UserInfo();
//            userInfo.lastName = "这" + i;
//            userInfo.userId = i+"";
//            userInfo.fullName = "这";
//            userInfo.deptFullname = "这";
//            contentInfo.add(contactsMultiInfo);
//        }
        // 按首字母进行排序
        Collections.sort(contentInfo, new Comparator<ContactsMultiInfo>() {

            @Override
            public int compare(ContactsMultiInfo o1, ContactsMultiInfo o2) {
                if (o1.getItemType() == ContactsMultiInfo.CONTENT && o2.getItemType() ==
                        ContactsMultiInfo.CONTENT)
                    return o1.getPingyin().compareTo(o2.getPingyin());
                else
                    return 0;
            }
        });
        data.add(0, new ContactsMultiInfo(ContactsMultiInfo.HEAD));
        data.addAll(contentInfo);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v, int id) {
        switch (id) {
            case R.id.ll_selected:
                UISkipUtils.skipToSelectedContactsActivity(this, mSelectedUserList, mMailId);
                break;
            case R.id.tv_right:
                List<UserInfo> userInfos = new ArrayList<>();
                List<ContactsMultiInfo> data = mAdapter.getData();
                for (ContactsMultiInfo datum : data) {
                    if (datum.getItemType() == ContactsMultiInfo.CONTENT && datum.isSelected) {
                        userInfos.add(datum.userInfo);
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("USER", (Serializable) userInfos);
                setResult(UISkipUtils.FROM_EDIT, intent);
                finish();
                break;
            case R.id.tv_btn:
                mLimitDialog.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UISkipUtils.TO_EDIT) {
            if (resultCode == UISkipUtils.FROM_EDIT) {
                List<ContactsMultiInfo> listData = mAdapter.getData();
                List<UserInfo> selected = (List<UserInfo>) data.getSerializableExtra("DATA");
                for (ContactsMultiInfo listDatum : listData) {
                    listDatum.isSelected = false;
                    if (listDatum.getItemType() == ContactsMultiInfo.CONTENT) {
                        for (UserInfo userInfo : selected) {
                            if (userInfo.userId.equals(listDatum.userInfo.userId)) {
                                listDatum.isSelected = true;
                            }
                        }
                    }
                }
                refreshSelected(listData);
                mAdapter.notifyDataSetChanged();
            } else if (resultCode == UISkipUtils.FROM_SELECTED) {
                List<UserInfo> selected = (List<UserInfo>) data.getSerializableExtra("DATA");
                Intent intent = new Intent();
                intent.putExtra("USER", (Serializable) selected);
                setResult(UISkipUtils.FROM_EDIT, intent);
                finish();
            }
        }
    }
}
