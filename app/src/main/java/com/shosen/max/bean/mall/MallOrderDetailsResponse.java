package com.shosen.max.bean.mall;

import java.util.ArrayList;

public class MallOrderDetailsResponse {

    private ArrayList<CartList> orderGoods;

    private MallOrderInfo orderInfo;

    public class MallOrderInfo {
        private String consignee;//kathy;,
        private String address;//北京市 市辖区 西城区 123;,
        private String addTime;//2018-12-12 16:34:54;,
        private String orderSn;//20181212555039;,
        private String actualPrice;//47,
        private String mobile;//17777810050;,
        private String orderStatusText;//未付款;,
        private double goodsPrice;//39,
        private String id;//19,
        private double freightPrice;//8,
        private double balancePrice;
        private long countDownStamp;//为付款可用
        private int day;//已发货可用
        private String orderType;

        //integral
        private String integralPrice;

        public String getIntegralPrice() {
            return integralPrice;
        }

        public void setIntegralPrice(String integralPrice) {
            this.integralPrice = integralPrice;
        }

        public double getBalancePrice() {
            return balancePrice;
        }

        public void setBalancePrice(double balancePrice) {
            this.balancePrice = balancePrice;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        private int orderStatus;
        public HandleOptions handleOption;

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public String getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(String actualPrice) {
            this.actualPrice = actualPrice;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getOrderStatusText() {
            return orderStatusText;
        }

        public void setOrderStatusText(String orderStatusText) {
            this.orderStatusText = orderStatusText;
        }

        public double getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(double goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getFreightPrice() {
            return freightPrice;
        }

        public void setFreightPrice(double freightPrice) {
            this.freightPrice = freightPrice;
        }

        public HandleOptions getHandleOption() {
            return handleOption;
        }

        public void setHandleOption(HandleOptions handleOption) {
            this.handleOption = handleOption;
        }

        public long getCountDownStamp() {
            return countDownStamp;
        }

        public void setCountDownStamp(long countDownStamp) {
            this.countDownStamp = countDownStamp;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }
    }

    public ArrayList<CartList> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(ArrayList<CartList> orderGoods) {
        this.orderGoods = orderGoods;
    }

    public MallOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(MallOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
