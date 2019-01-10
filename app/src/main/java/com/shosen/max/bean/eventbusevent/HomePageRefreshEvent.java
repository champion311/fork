package com.shosen.max.bean.eventbusevent;

public class HomePageRefreshEvent {

    public HomePageRefreshEvent(int pos) {
        this.pos = pos;
    }

    private int pos;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
