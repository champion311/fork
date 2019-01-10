package com.shosen.max.bean.mall;

import java.util.List;

public class GoodsOrderList {


    public class GoodsInnerList {
        private int number;//1,
        private String picUrl;//http://yanxuan.nosdn.127.net/1e7e392b6fc9da99dc112197b7444eec.png;,
        private String id;//16,
        private String goodsName;//宠物合金钢安全除菌指甲护理组合;
        private double price;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }
    }


    private String updateTime;// 2018-12-12 14:26:08;,
    private String id;//15,
    private String orderStatusText;// 已发货;
    private int orderStatus;
    private String orderType;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    private boolean isGroupin;//false,
    private String orderSn;// 20181212428594;,
    private double actualPrice;//227,
    private List<GoodsInnerList> goodsList;
    private HandleOptions handleOption;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderStatusText() {
        return orderStatusText;
    }

    public void setOrderStatusText(String orderStatusText) {
        this.orderStatusText = orderStatusText;
    }

    public boolean isGroupin() {
        return isGroupin;
    }

    public void setGroupin(boolean groupin) {
        isGroupin = groupin;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public List<GoodsInnerList> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsInnerList> goodsList) {
        this.goodsList = goodsList;
    }

    public HandleOptions getHandleOption() {
        return handleOption;
    }

    public void setHandleOption(HandleOptions handleOption) {
        this.handleOption = handleOption;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
