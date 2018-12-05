package cc.seedland.oa.circulate.global;

import android.content.Context;

import com.unnamed.b.atv.model.TreeNode;

import java.util.List;
import java.util.Map;

import cc.seedland.oa.circulate.modle.bean.DepartmentInfo;
import cc.seedland.oa.circulate.modle.bean.UserInfo;
import cc.seedland.oa.circulate.modle.net.ResponseHandler;
import cc.seedland.oa.circulate.view.OrganizationDepartmentItemHolder;
import cc.seedland.oa.circulate.view.OrganizationMemberItemHolder;

/**
 * 作者 ： 徐春蕾
 * 联系方式 ： xuchunlei@seedland.cc / QQ:22003950
 * 时间 ： 2018/12/05 13:53
 * 描述 ：
 **/
public abstract class SeedKnife {

    /**
     * 注入域名
     * @return
     */
    public abstract String getHost();


    /**
     * 获取当前用户ID
     * @return
     */
    public abstract String getCurrentUserId();

    /**
     * 载入
     * @param handler
     */
    public abstract void loadAllUsers(ResponseHandler handler);

    /**
     * 构建当前组织树节点
     * @param context
     * @param listener
     * @return
     */
    public abstract TreeNode buildTreeNode(Context context, OrganizationDepartmentItemHolder.ToggleListener listener);


    /**
     *
     * @param parentId
     */
    public abstract List<Map<String, String>> loadSubCompany(String parentId);

    /**
     * 构建子树节点
     * @param context
     * @param node
     */
    public abstract void buildSubTree(Context context,
                                      TreeNode node,
                                      DepartmentInfo info,
                                      OrganizationDepartmentItemHolder.ToggleListener toggleListener,
                                      OrganizationMemberItemHolder.OnNodeSelectListener nodeListener,
                                      List<UserInfo> users);
}
