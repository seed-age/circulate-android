package cc.seedland.oa.circulate.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.ContactsMultiInfo;
import cc.seedland.oa.circulate.modle.bean.UserInfo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class SearchPeopleAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {
    public SearchPeopleAdapter(int layoutResId, @Nullable List<UserInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfo item) {
        helper.setGone(R.id.tv_letter, false);
        helper.setText(R.id.tv_name, item.lastName);
      //  helper.setText(R.id.tv_department, item.fullCompanyName + "/" + item.deptFullName);
        String department = "";
        if (item.subcompanyName != null) {
            department += item.subcompanyName + "/";
        }
        if (item.departmentName != null) {
            department += item.departmentName;
        }
        if (department.length()>0) {
            String last = String.valueOf(department.charAt(department.length() - 1));
            if (last.equals("/")) {
                department = department.substring(0,department.length() - 1);
            }
        }
        helper.setText(R.id.tv_department, department);
        ImageView ivSelector = helper.getView(R.id.iv_selector);
        ivSelector.setSelected(item.isSelected);
        Global.sKnife.loadImage(item, (ImageView)helper.getView(R.id.civ_head));
    }
}
