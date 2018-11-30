package com.ecology.view.seedland.circulate.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.modle.bean.DepartmentInfo;
import com.ecology.view.seedland.circulate.utils.LogUtil;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by hch on 2018/2/2.
 */

public class OrganizationDepartmentItemHolder extends TreeNode.BaseNodeViewHolder<DepartmentInfo> {

    private ImageView mIvIcon;
    private int mLevel;
    private ToggleListener mToggleListener;
    private DepartmentInfo mDepartmentInfo;
    private TreeNode mDepartment;

    public OrganizationDepartmentItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, DepartmentInfo value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.item_organization_department, null, false);
        TextView tvName = view.findViewById(R.id.tv_name);
        tvName.setText(value.departmentName);
        mIvIcon = view.findViewById(R.id.iv_icon);
        mIvIcon.setImageResource(value.iconRes);
        mLevel = node.getLevel();
        LogUtil.e("level = " +mLevel);
        mDepartmentInfo = value;
        return view;
    }

    @Override
    public void toggle(boolean active) {
        if (mLevel != 1) {
            mIvIcon.setImageResource(active ? R.drawable.icon_organization_minus : R.drawable
                    .icon_organization_add);
            if (mToggleListener != null) {
                mToggleListener.onToggleListener(mDepartment,active,mDepartmentInfo);
            }
        }
    }

    public void setOnToggleListener(TreeNode department,ToggleListener toggleListener) {
        mToggleListener = toggleListener;
        mDepartment = department;
    }

    public interface ToggleListener {
        void onToggleListener(TreeNode department,boolean active,DepartmentInfo value);
    }
}
