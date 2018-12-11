package cc.seedland.oa.circulate.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.modle.bean.MailInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class EditSentCYListAdapter extends BaseQuickAdapter<MailInfo,BaseViewHolder>{
    public EditSentCYListAdapter(int layoutResId, @Nullable List<MailInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MailInfo item) {
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
        ImageView ivSelector = helper.getView(R.id.iv_selector);
        ivSelector.setSelected(item.isSelected);
//        helper.addOnClickListener(R.id.fl_selector);
    }
}
