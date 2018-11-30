package com.ecology.view.seedland.circulate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.modle.bean.UserInfo;
import com.ecology.view.seedland.circulate.view.SwipeLayout;
import com.ecology.view.seedland.circulate.view.SwipeLayoutManager;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class SelectedContactsAdapter extends BaseQuickAdapter<UserInfo,BaseViewHolder>{
    public SelectedContactsAdapter(int layoutResId, @Nullable List<UserInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfo item) {
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

        helper.setText(R.id.tv_name,item.lastName);
        helper.setText(R.id.tv_department,item.fullName+"/"+item.deptFullname);
        helper.addOnClickListener(R.id.tv_delete);
    }
}
