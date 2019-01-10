package com.shosen.max.bean;

public class LotteryResponse {
    //"data":2,"time":"3"
    private String data;//返回结果

    private String time;//剩余次数

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
