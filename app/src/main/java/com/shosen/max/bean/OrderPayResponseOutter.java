package com.shosen.max.bean;

public class OrderPayResponseOutter {

    private OrderPayResponseInnner data;

    public OrderPayResponseInnner getData() {
        return data;
    }

    public void setData(OrderPayResponseInnner data) {
        this.data = data;
    }

    public class OrderPayResponseInnner extends WxPayBean {

        private String payStr;

        public String getPayStr() {
            return payStr;
        }

        public void setPayStr(String payStr) {
            this.payStr = payStr;
        }
    }


}
