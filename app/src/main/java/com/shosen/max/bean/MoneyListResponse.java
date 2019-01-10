package com.shosen.max.bean;

public class MoneyListResponse {
    private String recordSn;
    private String bussOrderNo;
    private String userPhone;
    private String money;
    private String changeMoney;//-90,
    private String changedMoney;
    private int type;
    private String remark;
    private String createTime;
    private String id;

    public String getRecordSn() {
        return recordSn;
    }

    public void setRecordSn(String recordSn) {
        this.recordSn = recordSn;
    }

    public String getBussOrderNo() {
        return bussOrderNo;
    }

    public void setBussOrderNo(String bussOrderNo) {
        this.bussOrderNo = bussOrderNo;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getChangeMoney() {
        return changeMoney;
    }

    public void setChangeMoney(String changeMoney) {
        this.changeMoney = changeMoney;
    }

    public String getChangedMoney() {
        return changedMoney;
    }

    public void setChangedMoney(String changedMoney) {
        this.changedMoney = changedMoney;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String bookOrderNo;//": "123;,
    private String payValue;//:0,
    private String typeValue;//:600,

    public String getBookOrderNo() {
        return bookOrderNo;
    }

    public void setBookOrderNo(String bookOrderNo) {
        this.bookOrderNo = bookOrderNo;
    }

    public String getPayValue() {
        return payValue;
    }

    public void setPayValue(String payValue) {
        this.payValue = payValue;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }
}
