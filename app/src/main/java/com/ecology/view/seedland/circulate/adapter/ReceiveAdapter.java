package com.ecology.view.seedland.circulate.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.modle.bean.MailInfo;
import com.ecology.view.seedland.circulate.view.SwipeLayout;
import com.ecology.view.seedland.circulate.view.SwipeLayoutManager;

import java.util.List;

/**
 * Created by Administrator on 2018/1/6 0006.
 */

public class ReceiveAdapter extends BaseQuickAdapter<MailInfo, BaseViewHolder> {
    private boolean mIsSearch;
    private int type;
    public ReceiveAdapter(int layoutResId, @Nullable List<MailInfo> data) {
        super(layoutResId, data);
    }

    public ReceiveAdapter(int layoutResId, @Nullable List<MailInfo> data,boolean isSearch) {
        super(layoutResId, data);
        mIsSearch = isSearch;
    }

    @Override
    protected void convert(BaseViewHolder helper, MailInfo item) {
        SwipeLayout swipeLayout = helper.getView(R.id.swipeLayout);
        swipeLayout.setOnDragListener(new SwipeLayout.OnDragListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                SwipeLayoutManager.getInstance().setSwipeLayout(layout);
            }

            @Override
            public void onClose(SwipeLayout layout) {
            }

            @Override
            public void onDragging(SwipeLayout layout) {
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
            }
        });

        helper.setText(R.id.tv_name, item.lastName);
        helper.setText(R.id.tv_desc, item.title);
        helper.setGone(R.id.iv_has_notify, item.receiveMailState == 5);
        String date = "";
        if (!TextUtils.isEmpty(item.receiveTime)) {
            date = item.receiveTime.substring(item.receiveTime.indexOf("-") + 1, item.receiveTime.length());
        }
        helper.setText(R.id.tv_date, date);
        helper.setGone(R.id.iv_accessory, item.hasAttachment);
        helper.setGone(R.id.iv_focus, item.receiveAttention);
        helper.setText(R.id.tv_focus, item.receiveAttention ? "取消关注" : "关注");
        helper.addOnClickListener(R.id.tv_focus);
        helper.addOnClickListener(R.id.tv_delete);
        helper.setText(R.id.tv_delete, "跳过");
        helper.setBackgroundRes(R.id.tv_delete, R.color.color_204_204_204);
        helper.setTextColor(R.id.tv_delete, Global.getColor(R.color.color_22262A));
        if (mIsSearch) {
            helper.setGone(R.id.tv_focus,false);
            helper.setGone(R.id.tv_delete,false);
        }else {
            helper.setGone(R.id.tv_focus,true);
//            helper.setGone(R.id.tv_delete,true);
            if (type == 1 || type == 3) {
                helper.setGone(R.id.tv_delete,false);
            }else {
                helper.setGone(R.id.tv_delete,true);
            }
        }
    }

    public void setType(int type) {
        this.type = type;
    }
}
