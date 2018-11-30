package com.ecology.view.seedland.circulate.modle.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hch on 2018/1/17.
 */

public class SentCYListInfo implements Serializable {
    public int currentPage;
    public boolean firstPage;
    public boolean lastPage;
    public List<MailInfo> list;
    public int listSize;
    public int pageSize;
}
