package com.ecology.view.seedland.circulate.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ecology.view.seedland.circulate.R;
import com.ecology.view.seedland.circulate.base.CirculateBaseFragment;
import com.ecology.view.seedland.circulate.global.Constants;
import com.ecology.view.seedland.circulate.global.Global;
import com.ecology.view.seedland.circulate.modle.bean.DepartmentInfo;
import com.ecology.view.seedland.circulate.modle.bean.UserInfo;
import com.ecology.view.seedland.circulate.utils.LogUtil;
import com.ecology.view.seedland.circulate.view.LimitDialog;
import com.ecology.view.seedland.circulate.view.OrganizationDepartmentItemHolder;
import com.ecology.view.seedland.circulate.view.OrganizationMemberItemHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hch on 2018/2/2.
 */

public class OrganizationFragment extends CirculateBaseFragment implements OrganizationMemberItemHolder.OnNodeSelectListener, OrganizationDepartmentItemHolder.ToggleListener {

    private RelativeLayout mRlContainer;
    private AndroidTreeView mTView;
    private List<UserInfo> mSelectedUsers;
    private LimitDialog mLimitDialog;
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_organization;
    }

    @Override
    public void initView() {
        mRlContainer = findView(R.id.rl_container);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        mSelectedUsers = (List<UserInfo>) arguments.getSerializable("SELECTED_DATA");
        TreeNode root = getTreeNode();
        //3、将tree view添加到布局中
        mTView = new AndroidTreeView(mActivity, root);
        mTView.setDefaultAnimation(false);
        mTView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        mTView.setSelectionModeEnabled(true);
        mRlContainer.addView(mTView.getView());
    }

    @NonNull
    private TreeNode getTreeNode() {
        DepartmentInfo top = new DepartmentInfo();
        top.departmentName = "广州实地房地产开发有限公司";
        top.iconRes = R.drawable.icon_top_company;
        List<DepartmentInfo> subDepartment = new ArrayList<>();
        DepartmentInfo sub = new DepartmentInfo();
        sub.departmentName = "营销管理中心";
        sub.iconRes = R.drawable.icon_organization_add;
        sub.haveSubDepart = true;
        List<UserInfo> userInfos = Global.sUserInfo;
        sub.member = userInfos;
        subDepartment.add(sub);
        top.subDepartment = subDepartment;

        //1、创建树根节点
        TreeNode root = TreeNode.root();
        //2、创建和添加加点(使用自定义对象作为构造函数参数)
        final TreeNode parent = new TreeNode(top).setViewHolder(new OrganizationDepartmentItemHolder(mActivity));
        if (top.subDepartment != null) {
            for (final DepartmentInfo departmentInfo : top.subDepartment) {
                OrganizationDepartmentItemHolder subDetartmentHolder = new OrganizationDepartmentItemHolder(mActivity);
                TreeNode childDepartment = new TreeNode(departmentInfo).setViewHolder(subDetartmentHolder);
                subDetartmentHolder.setOnToggleListener(childDepartment,this);
//                if (departmentInfo.member != null) {
//                    for (UserInfo userInfo : departmentInfo.member) {
//                        OrganizationMemberItemHolder memberItemHolder = new OrganizationMemberItemHolder(mActivity);
//                        memberItemHolder.setOnNodeSelectListener(this);
//                        TreeNode member = new TreeNode(userInfo).setViewHolder(memberItemHolder);
//                        for (UserInfo selectedUser : mSelectedUsers) {
//                            if (selectedUser.userId == userInfo.userId) {
//                                member.setSelected(true);
//                            }
//                        }
//                        childDepartment.addChild(member);
//                    }
//                }
                parent.addChild(childDepartment);
            }
        }
        root.addChild(parent);
        return root;
    }

    @Override
    public void onClick(View v, int id) {
        switch (id){
            case R.id.tv_btn:
                mLimitDialog.dismiss();
                break;
        }

    }

    public List<UserInfo> getSelectedNode() {
        List<UserInfo> selectedValues = mTView.getSelectedValues(UserInfo.class);
        if (selectedValues.size() == 0){
            return mSelectedUsers;
        }
        return selectedValues;
    }

    public void setSelectedNode(List<UserInfo> selectedNode) {
        mSelectedUsers = selectedNode;
        mRlContainer.removeAllViews();
        mTView.setRoot(getTreeNode());
        mRlContainer.addView(mTView.getView());
    }

    @Override
    public void onNodeSelectListener(ImageView ivSelector,TreeNode node, UserInfo value) {
        if (mSelectedUsers.size() < Constants.selectLimit) {
            node.setSelected(!node.isSelected());
            refreshSelected(node.isSelected(),value);
        } else {
            if (node.isSelected()) {
                node.setSelected(!node.isSelected());
                refreshSelected(node.isSelected(),value);
            } else {
                showLimitDialog();
                return;
            }
        }
        ivSelector.setSelected(node.isSelected());
    }

    private void showLimitDialog() {
        if (mLimitDialog == null)
            mLimitDialog = new LimitDialog(mActivity);
        mLimitDialog.show();
        mLimitDialog.findViewById(R.id.tv_btn).setOnClickListener(this);
    }

    //刷新选中人数
    private void refreshSelected(boolean isSelected,UserInfo data) {
        if (isSelected) {
            mSelectedUsers.add(data);
        }else {
            for (int i = 0; i < mSelectedUsers.size(); i++) {
                UserInfo selectedUser = mSelectedUsers.get(i);
                if (selectedUser.userId.equals(data.userId)) {
                    mSelectedUsers.remove(i);
                }
            }
        }
    }

    @Override
    public void onToggleListener(TreeNode department, boolean active, DepartmentInfo value) {
        if (active) {
            if (value.haveSubDepart) {
                DepartmentInfo subA = new DepartmentInfo();
                subA.departmentName = "子部门A";
                subA.iconRes = R.drawable.icon_organization_add;
                subA.member = Global.sUserInfo;
                OrganizationDepartmentItemHolder subDetartmentHolderA = new OrganizationDepartmentItemHolder(mActivity);
                TreeNode childSubDepartmentA = new TreeNode(subA).setViewHolder(subDetartmentHolderA);
                department.addChild(childSubDepartmentA);
                subDetartmentHolderA.setOnToggleListener(childSubDepartmentA,this);
            }
            if (value.member != null) {
                outer:for (UserInfo userInfo : value.member) {
                    OrganizationMemberItemHolder memberItemHolder = new OrganizationMemberItemHolder(mActivity);
                    memberItemHolder.setOnNodeSelectListener(OrganizationFragment.this);
                    TreeNode member = new TreeNode(userInfo).setViewHolder(memberItemHolder);
                    for (UserInfo selectedUser : mSelectedUsers) {
                        if (selectedUser.userId.equals(userInfo.userId)) {
                            member.setSelected(true);
                        }
                    }
                    //处理重复问题
                    List<TreeNode> children = department.getChildren();
                    for (TreeNode child : children) {
                        if (child.getValue() instanceof UserInfo) {
                            UserInfo member1 = (UserInfo) child.getValue();
                            if (userInfo.userId.equals(member1.userId)) {
                                continue outer;
                            }
                        }
                    }
                    department.addChild(member);
                }
            }
        }
    }
}
