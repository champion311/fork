package com.shosen.max.bean.mall;


public class GoodsBean {

    private String id;//1110016,
    private String name;// 天然硅胶宠物除毛按摩刷;,
    private String brief;// 顺滑平面，猫狗通用，去除死毛;,
    private String picUrl;// http://yanxuan.nosdn.127.net/3bd73b7279a83d1cbb50c0e45778e6d6.png;,
    private boolean isNew;//false,
    private boolean isHot;//false,
    private String counterPrice;//59,
    private String retailPrice;//39
    private boolean isHeader = false;
    private String headerName;

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public String getCounterPrice() {
        return counterPrice;
    }

    public void setCounterPrice(String counterPrice) {
        this.counterPrice = counterPrice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public boolean isPointGoods() {
        return headerName.equals("积分兑换专区");
    }
}
