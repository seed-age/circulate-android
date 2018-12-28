package cc.seedland.oa.circulate.modle.bean;

import java.util.List;

/**
 * @author Created by Administrator.   530
 * @time 2018/12/25 0025 14:37
 * Description:
 */

public class NewObjectInfo {


    /**
     * code : 200
     * data : {"currentPage":1,"firstPage":true,"lastPage":false,"list":[{"afreshConfim":false,
     * "departmentName":"技术部","ifConfirm":false,"joinTime":"2018-12-25 11:26:24","lastName":"周诗博",
     * "loginId":"zhoushibo","mailState":5,"mailStatusss":0,"reDifferentiate":4173,"receiveAttention":false,
     * "receiveId":74128,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24","serialNum":0,"stepStatus":2,
     * "subcompanyName":"实地集团","userId":4248,"workCode":"9052"},{"afreshConfim":false,"departmentName":"市场部",
     * "ifConfirm":false,"joinTime":"2018-12-25 11:26:24","lastName":"刘星","loginId":"liuxing","mailState":5,
     * "mailStatusss":0,"reDifferentiate":4173,"receiveAttention":false,"receiveId":74129,"receiveStatus":0,
     * "receiveTime":"2018-12-25 11:26:24","serialNum":0,"stepStatus":2,"subcompanyName":"握手投资","userId":1549,
     * "workCode":"5662"},{"afreshConfim":false,"departmentName":"成本管理部","ifConfirm":false,"joinTime":"2018-12-25
     * 11:26:24","lastName":"李佳南","loginId":"lijianan","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
     * "receiveAttention":false,"receiveId":74130,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
     * "serialNum":0,"stepStatus":2,"subcompanyName":"三亚城市公司","userId":3655,"workCode":"8379"},{"afreshConfim":false,
     * "departmentName":"成本控制中心","ifConfirm":false,"joinTime":"2018-12-25 11:26:24","lastName":"廖敏敏",
     * "loginId":"liaominmin","mailState":5,"mailStatusss":0,"reDifferentiate":4173,"receiveAttention":false,
     * "receiveId":74131,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24","serialNum":0,"stepStatus":2,
     * "subcompanyName":"实地建设集团","userId":1114,"workCode":"4771"},{"afreshConfim":false,"departmentName":"装修机电版块",
     * "ifConfirm":false,"joinTime":"2018-12-25 11:26:24","lastName":"王稳","loginId":"wangwen","mailState":5,
     * "mailStatusss":0,"reDifferentiate":4173,"receiveAttention":false,"receiveId":74132,"receiveStatus":0,
     * "receiveTime":"2018-12-25 11:26:24","serialNum":0,"stepStatus":2,"subcompanyName":"实地建设集团","userId":118,
     * "workCode":"803"},{"afreshConfim":false,"departmentName":"采购部","ifConfirm":false,"joinTime":"2018-12-25
     * 11:26:24","lastName":"徐晓曼","loginId":"xuxiaoman","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
     * "receiveAttention":false,"receiveId":74133,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
     * "serialNum":0,"stepStatus":2,"subcompanyName":"逗号科技","userId":3411,"workCode":"8100"},{"afreshConfim":false,
     * "departmentName":"客服部","ifConfirm":false,"joinTime":"2018-12-25 11:26:24","lastName":"娄路路",
     * "loginId":"loululu","mailState":5,"mailStatusss":0,"reDifferentiate":4173,"receiveAttention":false,
     * "receiveId":74134,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24","serialNum":0,"stepStatus":2,
     * "subcompanyName":"遵义蔷薇国际9-11#服务中心","userId":6269,"workCode":"11239"},{"afreshConfim":false,
     * "departmentName":"人力行政部","ifConfirm":false,"joinTime":"2018-12-25 11:26:24","lastName":"邱奕","loginId":"qiuyi",
     * "mailState":5,"mailStatusss":0,"reDifferentiate":4173,"receiveAttention":false,"receiveId":74135,
     * "receiveStatus":0,"receiveTime":"2018-12-25 11:26:24","serialNum":0,"stepStatus":2,"subcompanyName":"无锡公司",
     * "userId":6907,"workCode":"11921"},{"afreshConfim":false,"departmentName":"渠道组","ifConfirm":false,
     * "joinTime":"2018-12-25 11:26:24","lastName":"陈港成","loginId":"chengangcheng","mailState":5,"mailStatusss":0,
     * "reDifferentiate":4173,"receiveAttention":false,"receiveId":74136,"receiveStatus":0,"receiveTime":"2018-12-25
     * 11:26:24","serialNum":0,"stepStatus":2,"subcompanyName":"增城公司","userId":6899,"workCode":"11913"},
     * {"afreshConfim":false,"departmentName":"客服组","ifConfirm":false,"joinTime":"2018-12-25 11:26:24",
     * "lastName":"任璐","loginId":"renlu","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
     * "receiveAttention":false,"receiveId":74137,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
     * "serialNum":0,"stepStatus":2,"subcompanyName":"六盘水公司","userId":521,"workCode":"2578"}],"listSize":10,
     * "nextPageNoSequence":[2,3,4],"pageSize":10,"previousPageNoSequence":[],"scale":3,"totalPage":431,
     * "totalRecord":4303}
     * msg : 查询传阅对象成功!
     * success : true
     */

    private String code;
    private DataBean data;
    private String msg;
    private boolean success;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        /**
         * currentPage : 1
         * firstPage : true
         * lastPage : false
         * list : [{"afreshConfim":false,"departmentName":"技术部","ifConfirm":false,"joinTime":"2018-12-25 11:26:24",
         * "lastName":"周诗博","loginId":"zhoushibo","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
         * "receiveAttention":false,"receiveId":74128,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
         * "serialNum":0,"stepStatus":2,"subcompanyName":"实地集团","userId":4248,"workCode":"9052"},
         * {"afreshConfim":false,"departmentName":"市场部","ifConfirm":false,"joinTime":"2018-12-25 11:26:24",
         * "lastName":"刘星","loginId":"liuxing","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
         * "receiveAttention":false,"receiveId":74129,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
         * "serialNum":0,"stepStatus":2,"subcompanyName":"握手投资","userId":1549,"workCode":"5662"},
         * {"afreshConfim":false,"departmentName":"成本管理部","ifConfirm":false,"joinTime":"2018-12-25 11:26:24",
         * "lastName":"李佳南","loginId":"lijianan","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
         * "receiveAttention":false,"receiveId":74130,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
         * "serialNum":0,"stepStatus":2,"subcompanyName":"三亚城市公司","userId":3655,"workCode":"8379"},
         * {"afreshConfim":false,"departmentName":"成本控制中心","ifConfirm":false,"joinTime":"2018-12-25 11:26:24",
         * "lastName":"廖敏敏","loginId":"liaominmin","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
         * "receiveAttention":false,"receiveId":74131,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
         * "serialNum":0,"stepStatus":2,"subcompanyName":"实地建设集团","userId":1114,"workCode":"4771"},
         * {"afreshConfim":false,"departmentName":"装修机电版块","ifConfirm":false,"joinTime":"2018-12-25 11:26:24",
         * "lastName":"王稳","loginId":"wangwen","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
         * "receiveAttention":false,"receiveId":74132,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
         * "serialNum":0,"stepStatus":2,"subcompanyName":"实地建设集团","userId":118,"workCode":"803"},
         * {"afreshConfim":false,"departmentName":"采购部","ifConfirm":false,"joinTime":"2018-12-25 11:26:24",
         * "lastName":"徐晓曼","loginId":"xuxiaoman","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
         * "receiveAttention":false,"receiveId":74133,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
         * "serialNum":0,"stepStatus":2,"subcompanyName":"逗号科技","userId":3411,"workCode":"8100"},
         * {"afreshConfim":false,"departmentName":"客服部","ifConfirm":false,"joinTime":"2018-12-25 11:26:24",
         * "lastName":"娄路路","loginId":"loululu","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
         * "receiveAttention":false,"receiveId":74134,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
         * "serialNum":0,"stepStatus":2,"subcompanyName":"遵义蔷薇国际9-11#服务中心","userId":6269,"workCode":"11239"},
         * {"afreshConfim":false,"departmentName":"人力行政部","ifConfirm":false,"joinTime":"2018-12-25 11:26:24",
         * "lastName":"邱奕","loginId":"qiuyi","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
         * "receiveAttention":false,"receiveId":74135,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
         * "serialNum":0,"stepStatus":2,"subcompanyName":"无锡公司","userId":6907,"workCode":"11921"},
         * {"afreshConfim":false,"departmentName":"渠道组","ifConfirm":false,"joinTime":"2018-12-25 11:26:24",
         * "lastName":"陈港成","loginId":"chengangcheng","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
         * "receiveAttention":false,"receiveId":74136,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
         * "serialNum":0,"stepStatus":2,"subcompanyName":"增城公司","userId":6899,"workCode":"11913"},
         * {"afreshConfim":false,"departmentName":"客服组","ifConfirm":false,"joinTime":"2018-12-25 11:26:24",
         * "lastName":"任璐","loginId":"renlu","mailState":5,"mailStatusss":0,"reDifferentiate":4173,
         * "receiveAttention":false,"receiveId":74137,"receiveStatus":0,"receiveTime":"2018-12-25 11:26:24",
         * "serialNum":0,"stepStatus":2,"subcompanyName":"六盘水公司","userId":521,"workCode":"2578"}]
         * listSize : 10
         * nextPageNoSequence : [2,3,4]
         * pageSize : 10
         * previousPageNoSequence : []
         * scale : 3
         * totalPage : 431
         * totalRecord : 4303
         */

        private int currentPage;
        private boolean firstPage;
        private boolean lastPage;
        private int listSize;
        private int pageSize;
        private int scale;
        private int totalPage;
        private int totalRecord;
        private List<ListBean> list;
        private List<Integer> nextPageNoSequence;
        private List<Object> previousPageNoSequence;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public boolean isFirstPage() {
            return firstPage;
        }

        public void setFirstPage(boolean firstPage) {
            this.firstPage = firstPage;
        }

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public int getListSize() {
            return listSize;
        }

        public void setListSize(int listSize) {
            this.listSize = listSize;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalRecord() {
            return totalRecord;
        }

        public void setTotalRecord(int totalRecord) {
            this.totalRecord = totalRecord;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<Integer> getNextPageNoSequence() {
            return nextPageNoSequence;
        }

        public void setNextPageNoSequence(List<Integer> nextPageNoSequence) {
            this.nextPageNoSequence = nextPageNoSequence;
        }

        public List<?> getPreviousPageNoSequence() {
            return previousPageNoSequence;
        }

        public void setPreviousPageNoSequence(List<Object> previousPageNoSequence) {
            this.previousPageNoSequence = previousPageNoSequence;
        }

        public static class ListBean {
            /**
             * afreshConfim : false
             * departmentName : 技术部
             * ifConfirm : false
             * joinTime : 2018-12-25 11:26:24
             * lastName : 周诗博
             * loginId : zhoushibo
             * mailState : 5
             * mailStatusss : 0
             * reDifferentiate : 4173
             * receiveAttention : false
             * receiveId : 74128
             * receiveStatus : 0
             * receiveTime : 2018-12-25 11:26:24
             * serialNum : 0
             * stepStatus : 2
             * subcompanyName : 实地集团
             * userId : 4248
             * workCode : 9052
             */

            private boolean afreshConfim;
            private String departmentName;
            private boolean ifConfirm;
            private String joinTime;
            private String lastName;
            private String loginId;
            private int mailState;
            private int mailStatusss;
            private int reDifferentiate;
            private boolean receiveAttention;
            private int receiveId;
            private int receiveStatus;
            private String receiveTime;
            private int serialNum;
            private int stepStatus;
            private String subcompanyName;
            private int userId;
            private String workCode;

            public boolean isAfreshConfim() {
                return afreshConfim;
            }

            public void setAfreshConfim(boolean afreshConfim) {
                this.afreshConfim = afreshConfim;
            }

            public String getDepartmentName() {
                return departmentName;
            }

            public void setDepartmentName(String departmentName) {
                this.departmentName = departmentName;
            }

            public boolean isIfConfirm() {
                return ifConfirm;
            }

            public void setIfConfirm(boolean ifConfirm) {
                this.ifConfirm = ifConfirm;
            }

            public String getJoinTime() {
                return joinTime;
            }

            public void setJoinTime(String joinTime) {
                this.joinTime = joinTime;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getLoginId() {
                return loginId;
            }

            public void setLoginId(String loginId) {
                this.loginId = loginId;
            }

            public int getMailState() {
                return mailState;
            }

            public void setMailState(int mailState) {
                this.mailState = mailState;
            }

            public int getMailStatusss() {
                return mailStatusss;
            }

            public void setMailStatusss(int mailStatusss) {
                this.mailStatusss = mailStatusss;
            }

            public int getReDifferentiate() {
                return reDifferentiate;
            }

            public void setReDifferentiate(int reDifferentiate) {
                this.reDifferentiate = reDifferentiate;
            }

            public boolean isReceiveAttention() {
                return receiveAttention;
            }

            public void setReceiveAttention(boolean receiveAttention) {
                this.receiveAttention = receiveAttention;
            }

            public int getReceiveId() {
                return receiveId;
            }

            public void setReceiveId(int receiveId) {
                this.receiveId = receiveId;
            }

            public int getReceiveStatus() {
                return receiveStatus;
            }

            public void setReceiveStatus(int receiveStatus) {
                this.receiveStatus = receiveStatus;
            }

            public String getReceiveTime() {
                return receiveTime;
            }

            public void setReceiveTime(String receiveTime) {
                this.receiveTime = receiveTime;
            }

            public int getSerialNum() {
                return serialNum;
            }

            public void setSerialNum(int serialNum) {
                this.serialNum = serialNum;
            }

            public int getStepStatus() {
                return stepStatus;
            }

            public void setStepStatus(int stepStatus) {
                this.stepStatus = stepStatus;
            }

            public String getSubcompanyName() {
                return subcompanyName;
            }

            public void setSubcompanyName(String subcompanyName) {
                this.subcompanyName = subcompanyName;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getWorkCode() {
                return workCode;
            }

            public void setWorkCode(String workCode) {
                this.workCode = workCode;
            }
        }
    }
}
