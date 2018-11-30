package com.ecology.view.seedland.circulate.modle.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ecology.view.seedland.circulate.utils.PinyinUtils;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class ContactsMultiInfo implements MultiItemEntity {
    public static final int HEAD = 1;
    public static final int CONTENT = 2;
    private int itemType;
    private String name;
    public UserInfo userInfo;
    public boolean isSelected;

    /** 首字母 */
    private String initLetter;

    // 中国 zhongguo
    /** 名字的拼音 */
    private String pingyin;

    public ContactsMultiInfo(int itemType) {
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPingyin() {
        if (TextUtils.isEmpty(name))
            return "";
        pingyin = PinyinUtils.getPinyin(name.charAt(0)+"");
        return pingyin;
    }

    public String getInitLetter() {
        String pingyin = getPingyin();
        if (TextUtils.isEmpty(pingyin))
            return "#";
        initLetter = pingyin.charAt(0) + "";
        return initLetter;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
