package cc.seedland.oa.circulate.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.base.CirculateBaseActivity;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.MailCountInfo;
import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.modle.net.BaseResponse;
import cc.seedland.oa.circulate.modle.net.HttpApis;
import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.utils.UISkipUtils;
import cc.seedland.oa.circulate.utils.Utils;
import cc.seedland.oa.circulate.view.MyToolbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Route(path = "/Function/Circulate")
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
        Global.sKnife.loadAllUsers(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDelayDialog();
        HttpService.loadMailCount(this);
    }

    @Override
    public void onClick(View v, int id) {
        if(id == R.id.tv_new){
            UISkipUtils.skipToCreateMailActivity(this);
        }else if(id == R.id.ll_received_chuanyue) {
            status = 5;
            UISkipUtils.skipToReceiveListActivity(this, status, "收到传阅", true);
        }else if(id == R.id.ll_sent_list) {
            status = 0;
            UISkipUtils.skipToSentListActivity(this, status, "已发传阅");
        }else if(id == R.id.ll_delete) {
            UISkipUtils.skipToRemovedListActivity(this);
        }else if(id == R.id.rl_wait_deal) {
            status = 2;
            UISkipUtils.skipToReceiveListActivity(this, status, "收到传阅", true);
        }else if(id == R.id.rl_receiving) {
            status = 1;
            UISkipUtils.skipToReceiveListActivity(this, status, "收到传阅", false);
        }else if(id == R.id.rl_received_complete_count){
            status = 3;
            UISkipUtils.skipToReceiveListActivity(this, status, "收到传阅", false);
        }else if(id == R.id.rl_sending){
            status = 1;
            UISkipUtils.skipToSentListActivity(this, status, "已发传阅");
        }else if(id == R.id.rl_sent_complete) {
            status = 3;
            UISkipUtils.skipToSentListActivity(this, status, "已发传阅");
        }else if(id == R.id.rl_draft){
            UISkipUtils.skipToDraftListActivity(this);
        }else if(id == R.id.rl_unread) {
            status = 5;
            UISkipUtils.skipToReceiveListActivity(this, status, "收到传阅", true);
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
        if (type == HttpApis.getMailCount().hashCode()) {
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
        } else if (type == HttpApis.getTestContacts().hashCode()) {
            List<UserInfo> userInfos = Utils.parseJsonArray(data, UserInfo.class);
            Global.sUserInfo = userInfos;
        }
    }
}
