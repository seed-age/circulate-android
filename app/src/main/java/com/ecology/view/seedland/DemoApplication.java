package cc.seedland.oa;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.unnamed.b.atv.model.TreeNode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.seedland.oa.circulate.fragment.OrganizationFragment;
import cc.seedland.oa.circulate.global.Global;
import cc.seedland.oa.circulate.global.SeedKnife;
import cc.seedland.oa.circulate.modle.bean.DepartmentInfo;
import cc.seedland.oa.circulate.modle.bean.HeaderInfo;
import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.modle.net.HttpService;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.view.CommonGroupDepartmentItemHolder;
import cc.seedland.oa.circulate.view.OrganizationDepartmentItemHolder;
import cc.seedland.oa.circulate.view.OrganizationMemberItemHolder;
import cc.seedland.oa.demo.BuildConfig;
import cc.seedland.oa.demo.R;

/**
 * 作者 ： 徐春蕾
 * 联系方式 ： xuchunlei@seedland.cc / QQ:22003950
 * 时间 ： 2018/12/05 12:33
 * 描述 ：
 **/
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化

        Global.init(getApplicationContext(), new SeedKnife() {
            @Override
            public String getHost() {
                return "http://sloa2.isunn.cn";
            }

            @Override
            public void loadImage(HeaderInfo url, ImageView v) {
//                if (!TextUtils.isEmpty(url)) {
//                    try {
//                        String imageUrl = Global.sKnife.getImageHost().replace("client",
//                                "downloadpic")
//                                + "?url="
//                                + URLEncoder.encode(item.userInfo.headerUrl, "utf-8") + "&thumbnail=1";
//                        Glide.with(mContext)
//                                .load(imageUrl)
//                                .apply(new RequestOptions().placeholder(cc.seedland.oa.circulate.R.drawable.ic_avatar_loading))
//                                .into((ImageView) helper.getView(cc.seedland.oa.circulate.R.id.civ_head));
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }

            }
            @Override
            public String getCurrentUserId() {
                return "4173";
            }

            @Override
            public void loadAllUsers(ResponseHandler handler) {
                HttpService.testContacts(handler);
            }

            @Override
            public TreeNode buildTreeNode(Context context, OrganizationDepartmentItemHolder.ToggleListener listener) {
                DepartmentInfo top = new DepartmentInfo();
                top.departmentName = "广州实地房地产开发有限公司";
                top.iconRes = cc.seedland.oa.circulate.R.drawable.icon_top_company;
                List<DepartmentInfo> subDepartment = new ArrayList<>();
                DepartmentInfo sub = new DepartmentInfo();
                sub.departmentName = "营销管理中心";
                sub.iconRes = cc.seedland.oa.circulate.R.drawable.icon_organization_add;
                sub.haveSubDepart = true;
                List<UserInfo> userInfos = Global.sUserInfo;
                sub.member = userInfos;
                subDepartment.add(sub);
                top.subDepartment = subDepartment;

                //1、创建树根节点
                TreeNode root = TreeNode.root();
                //2、创建和添加加点(使用自定义对象作为构造函数参数)
                final TreeNode parent = new TreeNode(top).setViewHolder(new OrganizationDepartmentItemHolder(context));
                if (top.subDepartment != null) {
                    for (final DepartmentInfo departmentInfo : top.subDepartment) {
                        OrganizationDepartmentItemHolder subDetartmentHolder = new OrganizationDepartmentItemHolder(context);
                        TreeNode childDepartment = new TreeNode(departmentInfo).setViewHolder(subDetartmentHolder);
                        subDetartmentHolder.setOnToggleListener(childDepartment,listener);
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
            public void buildSubTree(Context context, TreeNode node, DepartmentInfo info,
                                     OrganizationDepartmentItemHolder.ToggleListener toggleListener,
                                     OrganizationMemberItemHolder.OnNodeSelectListener nodeListener,
                                     List<UserInfo> users) {
                if (info.haveSubDepart) {
                    DepartmentInfo subA = new DepartmentInfo();
                    subA.departmentName = "子部门A";
                    subA.iconRes = R.drawable.icon_organization_add;
                    subA.member = Global.sUserInfo;
                    OrganizationDepartmentItemHolder subDetartmentHolderA = new OrganizationDepartmentItemHolder(context);
                    TreeNode childSubDepartmentA = new TreeNode(subA).setViewHolder(subDetartmentHolderA);
                    node.addChild(childSubDepartmentA);
                    subDetartmentHolderA.setOnToggleListener(childSubDepartmentA,toggleListener);
                }
                if (info.member != null) {
                    outer:for (UserInfo userInfo : info.member) {
                        OrganizationMemberItemHolder memberItemHolder = new OrganizationMemberItemHolder(context);
                        memberItemHolder.setOnNodeSelectListener(nodeListener);
                        TreeNode member = new TreeNode(userInfo).setViewHolder(memberItemHolder);
                        for (UserInfo selectedUser : users) {
                            if (selectedUser.userId.equals(userInfo.userId)) {
                                member.setSelected(true);
                            }
                        }
                        //处理重复问题
                        List<TreeNode> children = node.getChildren();
                        for (TreeNode child : children) {
                            if (child.getValue() instanceof UserInfo) {
                                UserInfo member1 = (UserInfo) child.getValue();
                                if (userInfo.userId.equals(member1.userId)) {
                                    continue outer;
                                }
                            }
                        }
                        node.addChild(member);
                    }
                }

            }

            @Override
            public TreeNode buildTreeForPrivateGroup(Context context, CommonGroupDepartmentItemHolder.ToggleListener listener) {
                List<DepartmentInfo> subDepartment = new ArrayList<>();
                DepartmentInfo sub = new DepartmentInfo();
                sub.departmentName = "营销管理中心";
                sub.iconRes = cc.seedland.oa.circulate.R.drawable.icon_organization_add;
                sub.haveSubDepart = true;
                List<UserInfo> userInfos = Global.sUserInfo;
                sub.member = userInfos;
                subDepartment.add(sub);
//        top.subDepartment = subDepartment;

                //1、创建树根节点
                TreeNode root = TreeNode.root();
                //2、创建和添加加点(使用自定义对象作为构造函数参数)
                CommonGroupDepartmentItemHolder parentHolder = new CommonGroupDepartmentItemHolder(context);
                TreeNode parent = new TreeNode(sub).setViewHolder(parentHolder);
                parentHolder.setOnToggleListener(parent,listener);
                root.addChild(parent);
                return root;
            }

            @Override
            public TreeNode buildTreeForCommonGroup(Context context, CommonGroupDepartmentItemHolder.ToggleListener listener) {
                List<DepartmentInfo> subDepartment = new ArrayList<>();
                DepartmentInfo sub = new DepartmentInfo();
                sub.departmentName = "营销管理中心";
                sub.iconRes = cc.seedland.oa.circulate.R.drawable.icon_organization_add;
                sub.haveSubDepart = true;
                List<UserInfo> userInfos = Global.sUserInfo;
                sub.member = userInfos;
                subDepartment.add(sub);
//        top.subDepartment = subDepartment;

                //1、创建树根节点
                TreeNode root = TreeNode.root();
                //2、创建和添加加点(使用自定义对象作为构造函数参数)
                CommonGroupDepartmentItemHolder parentHolder = new CommonGroupDepartmentItemHolder(context);
                TreeNode parent = new TreeNode(sub).setViewHolder(parentHolder);
                parentHolder.setOnToggleListener(parent,listener);
                root.addChild(parent);
                return root;
            }
        });
    }
}
