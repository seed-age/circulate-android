package cc.seedland.oa.circulate.utils;

import java.util.List;

import cc.seedland.oa.circulate.modle.bean.UserInfo;

/**
 * @author Created by Administrator.
 * @time 2018/12/25 0025 17:07
 * Description:
 */

public class ReceivessCache {

    public static List<UserInfo> receivess;//收件人集合

    public static void clear() {
        if (receivess != null) {
            receivess.clear();
        }
    }

    public static boolean pullReceivessObj(List<UserInfo> receivess) {
        ReceivessCache.receivess = receivess;
        return true;
    }

}
