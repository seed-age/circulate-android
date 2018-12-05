package cc.seedland.oa.circulate.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.modle.bean.MailInfo;
import cc.seedland.oa.circulate.view.SwipeLayout;
import cc.seedland.oa.circulate.view.SwipeLayoutManager;

import java.util.List;

/**
 * Created by Administrator on 2018/1/6 0006.
 */

public class DraftAdapter extends BaseQuickAdapter<MailInfo, BaseViewHolder> {

    public DraftAdapter(int layoutResId, @Nullable List<MailInfo> data) {
        super(layoutResId, data);
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
        helper.setGone(R.id.iv_has_notify, false);
        String date = "";
        if (!TextUtils.isEmpty(item.sendTime)) {
            date = item.sendTime.substring(item.sendTime.indexOf("-") + 1, item.sendTime.length());
        }
        helper.setText(R.id.tv_date, date);
        helper.setGone(R.id.iv_accessory, item.hasAttachment);
        helper.setGone(R.id.iv_focus, item.attention);
        helper.setText(R.id.tv_focus, item.attention ? "取消关注" : "关注");
        helper.addOnClickListener(R.id.tv_focus);
        helper.addOnClickListener(R.id.tv_delete);
        helper.setGone(R.id.tv_focus, false);
    }
}
