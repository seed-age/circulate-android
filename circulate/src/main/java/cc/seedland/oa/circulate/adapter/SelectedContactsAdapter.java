package cc.seedland.oa.circulate.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.view.SwipeLayout;
import cc.seedland.oa.circulate.view.SwipeLayoutManager;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class SelectedContactsAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {
    public SelectedContactsAdapter(int layoutResId, @Nullable List<UserInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfo item) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.img_head);
        requestOptions.placeholder(R.drawable.img_head);
        Glide.with(mContext).load(item.headerUrl).apply(requestOptions).into((ImageView) helper.getView(R.id.civ_head));
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
        String department = "";
        if (item.subcompanyName != null) {
            department += item.subcompanyName + "/";
        }
        if (item.departmentName != null) {
            department += item.departmentName;
        }
        if (department.length() > 0) {
            String last = String.valueOf(department.charAt(department.length() - 1));
            if (last.equals("/")) {
                department = department.substring(0, department.length() - 1);
            }
        }
        helper.setText(R.id.tv_department, department);
        Global.sKnife.loadImage(TextUtils.isEmpty(item.headerUrl) ? "/avatar/" + item.lastName : item.headerUrl, (ImageView)helper.getView(R.id.civ_head));
        helper.addOnClickListener(R.id.tv_delete);
    }
}
