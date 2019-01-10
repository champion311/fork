package com.shosen.max.bean;

import android.text.TextUtils;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;
import com.google.gson.annotations.SerializedName;
import com.shosen.max.utils.HDateUtils;
import com.shosen.max.utils.RegexUtils;

@SmartTable(name = "allowance_table")
public class TableBean {

    @SmartColumn(id = 1, name = "日期")
    @SerializedName("createTime")
    private String date;

    @SmartColumn(id = 2, name = "好友名称")
    @SerializedName("bookName")
    private String friendName;


    @SmartColumn(id = 3, name = "联系方式")
    @SerializedName("bookPhone")
    private String contactMethod;

    @SmartColumn(id = 4, name = "已支付金额")
    @SerializedName("payValue")
    private String payMoney;

    @SmartColumn(id = 5, name = "奖励 资产")
    @SerializedName("typeValue")
    private String property;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getContactMethod() {
        return contactMethod;
    }

    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public TableBean(String date, String friendName, String contactMethod, String payMoney, String property) {
        this.date = date;
        this.friendName = friendName;
        this.contactMethod = contactMethod;
        this.payMoney = payMoney;
        this.property = property;
    }

    public void init() {
        if (!TextUtils.isEmpty(date) && RegexUtils.isMatch("[0-9]+", date)) {
            date = HDateUtils.getDate(Long.valueOf(date), "yyyy-MM");
        }
        if ("0".equals(property)) {
            property = "---";
        }
    }
}
