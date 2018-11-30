package com.ecology.view.seedland.circulate.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.modle.bean.ContactsMultiInfo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class ContactsAdapter extends BaseMultiItemQuickAdapter<ContactsMultiInfo, BaseViewHolder> {

    private String regex = "[a-zA-Z]";

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ContactsAdapter(List<ContactsMultiInfo> data) {
        super(data);
        addItemType(ContactsMultiInfo.HEAD, R.layout.item_contacts_head);
        addItemType(ContactsMultiInfo.CONTENT, R.layout.item_contacts_contact);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactsMultiInfo item) {
        int itemType = item.getItemType();
        if (itemType == ContactsMultiInfo.HEAD) {
            helper.addOnClickListener(R.id.ll_organization);
            helper.addOnClickListener(R.id.ll_common_group);
            helper.addOnClickListener(R.id.ll_private_group);
            helper.addOnClickListener(R.id.ll_search);
        }else if (itemType == ContactsMultiInfo.CONTENT) {
            TextView tvLetter = helper.getView(R.id.tv_letter);
            helper.setText(R.id.tv_name,item.getName());
            String initLetter = item.getInitLetter();
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(initLetter);
            if (m.find()) {
                initLetter = initLetter.toUpperCase();
            } else {
                initLetter = "#";
            }
            tvLetter.setText(initLetter);
            int position = helper.getAdapterPosition();
            // 第一个列表项会显示首字母
            if (position == 0) {
                tvLetter.setVisibility(View.VISIBLE);
            } else {
                // 上一个列表项的javabean
                ContactsMultiInfo contactsMultiInfo = super.getData().get(position - 1);

                // 与上一个列表项的javabean的首字母不一样，是显示
                String initLetter1 = contactsMultiInfo.getInitLetter();
                Matcher m1 = p.matcher(initLetter1);
                if (m1.find()) {
                    initLetter1 = initLetter1.toUpperCase();
                }
                if (!initLetter.equals(initLetter1)) {
                    tvLetter.setVisibility(View.VISIBLE);
                } else { // 不显示
                    tvLetter.setVisibility(View.GONE);
                }
            }

            helper.setText(R.id.tv_department,item.userInfo.fullName+"/"+item.userInfo.deptFullname);
            ImageView ivSelector = helper.getView(R.id.iv_selector);
            ivSelector.setSelected(item.isSelected);
        }
    }
}
