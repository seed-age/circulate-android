package cc.seedland.oa.circulate.utils;

import android.text.TextUtils;

/**
 * 作者 ： 徐春蕾
 * 联系方式 ： xuchunlei@seedland.cc / QQ:22003950
 * 时间 ： 2019/01/02 19:58
 * 描述 ：
 **/
public class BizUtils {

    private BizUtils() {

    }

    public static String formatOrganInfo(String subComp, String dept) {
        String result = "";
        if (!TextUtils.isEmpty(subComp)) {
            result += subComp + "/";
        }
        if (dept != null) {
            result += dept;
        }

        if (result.length()>0) {
            String last = String.valueOf(result.charAt(result.length() - 1));
            if (last.equals("/")) {
                result = result.substring(0,result.length() - 1);
            }
        }
        return result;
    }
}
