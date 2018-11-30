package com.ecology.view.seedland.circulate.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.base.CirculateBaseActivity;
import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.modle.bean.MailCountInfo;
import com.ecology.view.seedland.circulate.modle.bean.UserInfo;
import com.ecology.view.seedland.circulate.modle.net.BaseResponse;
import com.ecology.view.seedland.circulate.modle.net.HttpApis;
import com.ecology.view.seedland.circulate.modle.net.HttpService;
import com.ecology.view.seedland.circulate.modle.net.ResponseHandler;
import com.ecology.view.seedland.circulate.utils.UISkipUtils;
import com.ecology.view.seedland.circulate.utils.Utils;
import com.ecology.view.seedland.circulate.view.MyToolbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChuanYueActivity extends CirculateBaseActivity implements ResponseHandler {

    private TextView mTvReceivedCount;
    private TextView mTvSendCount;
    private TextView mTvDeleteCount;
    private TextView mTvWaitDeal;
    private TextView mTvWaitSend;
    private TextView mTvReceivingCount;
    private TextView mTvSentCount;
    private TextView mTvReceivedCompleteCount;
    private TextView mTvSentCompleteCount;
    private int status; //0 所有收到的传阅  1 传阅中 2 待办传阅 3 已完成  5 未读 6 已确认
    private TextView mTvUnread;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_circulate;
    }

    @Override
    public void initView() {
        initStatusBar();
        initToolbar();
        mTvReceivedCount = findView(R.id.tv_received_count);
        mTvSendCount = findView(R.id.tv_send_count);
        mTvDeleteCount = findView(R.id.tv_delete_count);
        mTvWaitDeal = findView(R.id.tv_wait_deal);
        mTvWaitSend = findView(R.id.tv_wait_send);
        mTvReceivingCount = findView(R.id.tv_receiving_count);
        mTvSentCount = findView(R.id.tv_sent_count);
        mTvReceivedCompleteCount = findView(R.id.tv_received_complete_count);
        mTvSentCompleteCount = findView(R.id.tv_sent_complete_count);
        mTvUnread = findView(R.id.tv_unread);

    }

    private void initToolbar() {
        MyToolbar toolbar = findView(R.id.toolbar);
        toolbar.setOnBackClickListener(this);
        toolbar.setTitle("传阅");
    }

    private void initStatusBar() {
        LinearLayout llTopLayout = findView(R.id.ll_top_layout);
        int statusBarHeight = Global.getStatusBarHeight(this);
        llTopLayout.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        //使用测试数据
        HttpService.testContacts(this);
        //使用本地数据
//        ArrayList<Map<String, String>> allUserList = new ArrayList<>(
//                1); // 所有人的数据
//        allUserList.addAll(SQLTransaction.getInstance().queryHRAllPeopelForWorkCenter());
//        List<UserInfo> users = new ArrayList<>();
//        for (Map<String, String> map : allUserList) {
//            UserInfo info = new UserInfo();
//            info.headerUrl = map.get(TableFiledName.HrmResource.HEADER_URL);
//            info.fullCompanyName = map.get(TableFiledName.HrmResource.SUB_COMPANY_NAME);
//            info.deptFullName = map.get(TableFiledName.HrmResource.DEPARTMENT_NAME);
//            info.userId = map.get(TableFiledName.HrmResource.ID);
//            info.lastName = map.get(TableFiledName.HrmResource.NAME);
//            info.mobile = map.get(TableFiledName.HrmResource.MOBILE);
//            users.add(info);
//        }
//        Global.sUserInfo = users;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDelayDialog();
        HttpService.loadMailCount(this);
    }

    @Override
    public void onClick(View v, int id) {
        switch (id) {
            case R.id.tv_new:
                UISkipUtils.skipToCreateMailActivity(this);
                break;
            case R.id.ll_received_chuanyue:
                status = 5;
                UISkipUtils.skipToReceiveListActivity(this, status, "收到传阅", true);
                break;
            case R.id.ll_sent_list:
                status = 0;
                UISkipUtils.skipToSentListActivity(this, status, "已发传阅");
                break;
            case R.id.ll_delete:
//                UISkipUtils.skipToReceiveListActivity(this,2);
                UISkipUtils.skipToRemovedListActivity(this);
                break;
            case R.id.rl_wait_deal:
                status = 2;
                UISkipUtils.skipToReceiveListActivity(this, status, "收到传阅", true);
                break;
            case R.id.rl_receiving:
                status = 1;
                UISkipUtils.skipToReceiveListActivity(this, status, "收到传阅", false);
                break;
            case R.id.rl_received_complete_count:
                status = 3;
                UISkipUtils.skipToReceiveListActivity(this, status, "收到传阅", false);
                break;
            case R.id.rl_sending:
                status = 1;
                UISkipUtils.skipToSentListActivity(this, status, "已发传阅");
                break;
            case R.id.rl_sent_complete:
                status = 3;
                UISkipUtils.skipToSentListActivity(this, status, "已发传阅");
                break;
            case R.id.rl_draft:
                UISkipUtils.skipToDraftListActivity(this);
                break;
            case R.id.rl_unread:
                status = 5;
                UISkipUtils.skipToReceiveListActivity(this, status, "收到传阅", true);
                break;
        }
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }

    @Override
    public void onSuccess(String json, JSONObject jsonObject, BaseResponse response) {
        hideDelayDialog();
        String data = jsonObject.optString("data");
        int type = response.getType();
        if (type == HttpApis.MAIL_COUNT.hashCode()) {
            MailCountInfo mailCountInfo = Utils.parseJson(data, MailCountInfo.class);
            mTvReceivedCount.setText(mailCountInfo.getCount());
            mTvSendCount.setText(mailCountInfo.getSendCount());
            mTvDeleteCount.setText(mailCountInfo.getDeleteCount());
            mTvWaitDeal.setText(mailCountInfo.getTodoCount());
            mTvWaitSend.setText(mailCountInfo.getWaitSendCount());
            mTvSentCount.setText(mailCountInfo.getSendInCount());
            mTvReceivingCount.setText(mailCountInfo.getReceiveInCount());
            mTvReceivedCompleteCount.setText(mailCountInfo.getReceiveCompleteCount());
            mTvSentCompleteCount.setText(mailCountInfo.getCompleteCount());
            mTvUnread.setText(mailCountInfo.getUnreadCount());
        } else if (type == HttpApis.TEST_CONTACTS.hashCode()) {
            List<UserInfo> userInfos = Utils.parseJsonArray(data, UserInfo.class);
            Global.sUserInfo = userInfos;
        }
    }
}
