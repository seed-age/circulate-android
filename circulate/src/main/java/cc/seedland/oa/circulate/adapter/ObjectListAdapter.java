package cc.seedland.oa.circulate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.global.Constants;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.ObjectInfo;
import cc.seedland.oa.circulate.view.SwipeLayout;
import cc.seedland.oa.circulate.view.SwipeLayoutManager;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class ObjectListAdapter extends BaseQuickAdapter<ObjectInfo,BaseViewHolder>{
    public ObjectListAdapter(int layoutResId, @Nullable List<ObjectInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ObjectInfo item) {
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
        helper.setText(R.id.tv_department,item.subcompanyName+"/"+item.departmentName);
        int mailStatusss = item.mailStatusss;
        if (mailStatusss == 0) {
            helper.setText(R.id.tv_status,"未读");
            helper.setTextColor(R.id.tv_status, Global.getColor(R.color.color_008cd6));
        }else if (mailStatusss == 1) {
            helper.setText(R.id.tv_status,"等待确认");
            helper.setTextColor(R.id.tv_status, Global.getColor(R.color.color_53585C));
        }else if (mailStatusss == 2) {
            helper.setText(R.id.tv_status,"已确认");
            helper.setTextColor(R.id.tv_status, Global.getColor(R.color.color_00C21E));
        }
        if (Global.sKnife.getCurrentUserId().equals(String.valueOf(item.reDifferentiate))) {
            helper.setGone(R.id.tv_delete,true);
            helper.addOnClickListener(R.id.tv_delete);
        }else {
            helper.setGone(R.id.tv_delete,false);
        }
    }
}
