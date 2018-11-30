package com.ecology.view.seedland.circulate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.modle.bean.DiscussesInfo;

import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class CommentListAdapter extends BaseQuickAdapter<DiscussesInfo,BaseViewHolder>{
    private String text = "解决末尾显示的指示标识文字与原来文字宽度不一致时的显示问题（如原始文字与行尾指示标识文字为不同语言）。如当结尾指示标识文字较宽时，可能会显示到下一行。以此优化UI体验。\n" +
            "解决末尾单词过长或者跟随标点后，换行留下的空白问题。此问题源于TextView自带的一个属性：当结尾为完整单词或者跟随标点时会连同之前的部分文字一起换行。";
    public CommentListAdapter(int layoutResId, @Nullable List<DiscussesInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscussesInfo item) {
        ExpandableTextView etv = helper.getView(R.id.etv);
        //普通视图中的更新
        etv.setText(item.discussContent);
        //在ListView/RecyclerView中的应用
        etv.updateForRecyclerView(item.discussContent, etv.getWidth(), ExpandableTextView.STATE_SHRINK);//etvWidth为控件的真实宽度，state是控件所处的状态，“收缩”/“伸展”状态

        helper.setText(R.id.tv_name,item.lastName);
        helper.setText(R.id.tv_department,item.loginId);
        helper.setText(R.id.tv_time,item.time);

    }
}
