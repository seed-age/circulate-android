package cc.seedland.oa.circulate.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.global.Constants;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.ContactsMultiInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
        } else if (itemType == ContactsMultiInfo.CONTENT) {
            helper.setText(R.id.tv_name, item.getName());
            // ============把首字母逻辑注释==============
//            TextView tvLetter = helper.getView(R.id.tv_letter);
//            String initLetter = item.getInitLetter();
//            Pattern p = Pattern.compile(regex);
//            Matcher m = p.matcher(initLetter);
//            if (m.find()) {
//                initLetter = initLetter.toUpperCase();
//            } else {
//                initLetter = "#";
//            }
//            tvLetter.setText(initLetter);
//            int position = helper.getAdapterPosition();
            // 第一个列表项会显示首字母
//            if (position == 0) {
//                tvLetter.setVisibility(View.VISIBLE);
//            } else {
//                // 上一个列表项的javabean
//                ContactsMultiInfo contactsMultiInfo = super.getData().get(position - 1);
//
//                // 与上一个列表项的javabean的首字母不一样，是显示
//                String initLetter1 = contactsMultiInfo.getInitLetter();
//                Matcher m1 = p.matcher(initLetter1);
//                if (m1.find()) {
//                    initLetter1 = initLetter1.toUpperCase();
//                }
//                if (!initLetter.equals(initLetter1)) {
//                    tvLetter.setVisibility(View.VISIBLE);
//                } else { // 不显示
//                    tvLetter.setVisibility(View.GONE);
//                }
//            }
            // ============把首字母逻辑注释==============
            String department = "";
            if (item.userInfo.subcompanyName != null) {
                department += item.userInfo.subcompanyName + "/";
            }
            if (item.userInfo.departmentName != null) {
                department += item.userInfo.departmentName;
            }
            if (department.length()>0) {
                String last = String.valueOf(department.charAt(department.length() - 1));
                if (last.equals("/")) {
                    department = department.substring(0,department.length() - 1);
                }
            }
            helper.setText(R.id.tv_department, department);
            Global.sKnife.loadImage(item.userInfo, (ImageView) helper.getView(R.id
                    .civ_head));
            ImageView ivSelector = helper.getView(R.id.iv_selector);
            ivSelector.setSelected(item.isSelected);
        }
    }
}
