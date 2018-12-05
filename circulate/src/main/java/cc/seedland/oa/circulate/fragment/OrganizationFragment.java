package cc.seedland.oa.circulate.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cc.seedland.oa.circulate.R;
import cc.seedland.oa.circulate.base.CirculateBaseFragment;
import cc.seedland.oa.circulate.global.Constants;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.modle.bean.DepartmentInfo;
import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.view.LimitDialog;
import cc.seedland.oa.circulate.view.OrganizationDepartmentItemHolder;
import cc.seedland.oa.circulate.view.OrganizationMemberItemHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        return Global.sKnife.buildTreeNode(mActivity, this);
    }

    @Override
    public void onClick(View v, int id) {
        if(id == R.id.tv_btn) {
            mLimitDialog.dismiss();
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
    public void onToggleListener(TreeNode treeNode, boolean active, DepartmentInfo info) {
        if (active) {
            Global.sKnife.buildSubTree(mActivity, treeNode, info, this, this, mSelectedUsers);
        }
    }

}
