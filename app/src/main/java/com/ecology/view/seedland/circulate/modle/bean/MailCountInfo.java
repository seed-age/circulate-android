package com.ecology.view.seedland.circulate.modle.bean;

/**
 * Created by hch on 2018/1/16.
 */

public class MailCountInfo {

    /**
     * deleteCount : 1
     * todoCount : 4
     * receiveInCount : 4
     * sendCount : 5
     * receiveCount : 4
     * sendInCount : 4
     * ceceiveCompleteCount : 0
     * waitSendCount : 2
     * completeCount : 1
     */

    private String deleteCount; //已删除传阅数量
    private String todoCount;//待办传阅数量
    private String receiveInCount;//收到传阅 传阅中数量
    private String sendCount;//所有已发传阅
    private String receiveCount;//已收到传阅数量
    private String sendInCount;//已发传阅 传阅中数量
    public String receiveCompleteCount;//收到传阅 已完成数量
    private String waitSendCount;//待发传阅
    private String completeCount;//已发传阅 已完成数量
    private String unreadCount;//收到传阅 未读数量
    private String count;//所有收到传阅数量

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(String deleteCount) {
        this.deleteCount = deleteCount;
    }

    public String getTodoCount() {
        return todoCount;
    }

    public void setTodoCount(String todoCount) {
        this.todoCount = todoCount;
    }

    public String getReceiveInCount() {
        return receiveInCount;
    }

    public void setReceiveInCount(String receiveInCount) {
        this.receiveInCount = receiveInCount;
    }

    public String getSendCount() {
        return sendCount;
    }

    public void setSendCount(String sendCount) {
        this.sendCount = sendCount;
    }

    public String getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(String receiveCount) {
        this.receiveCount = receiveCount;
    }

    public String getSendInCount() {
        return sendInCount;
    }

    public void setSendInCount(String sendInCount) {
        this.sendInCount = sendInCount;
    }

    public String getWaitSendCount() {
        return waitSendCount;
    }

    public void setWaitSendCount(String waitSendCount) {
        this.waitSendCount = waitSendCount;
    }

    public String getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(String completeCount) {
        this.completeCount = completeCount;
    }

    public String getReceiveCompleteCount() {
        return receiveCompleteCount;
    }

    public void setReceiveCompleteCount(String receiveCompleteCount) {
        this.receiveCompleteCount = receiveCompleteCount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
