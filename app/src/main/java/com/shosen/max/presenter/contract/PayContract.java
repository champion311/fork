package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.AlipayReqBean;
import com.shosen.max.bean.OrderPayResponseOutter;
import com.shosen.max.bean.WxPayBean;

import java.util.Map;

public interface PayContract {

    interface View extends BaseView {

        void invokeAliPayReqBack(AlipayReqBean aliBean);

        void invokeWxPayReqBack(WxPayBean wxBean);

        void showErrorMessage(String message);

        void invokeOrderPayResponse(OrderPayResponseOutter data);
    }

    interface Presenter extends BasePresenter<View> {

        void invokeWxPay(String bookId);

        void invokeAliPay(String bookId);

        /**
         * @param orderId
         * @param payType  支付宝:aliPay 微信:wxAppPay
         * @param totalFee actualPrice
         */
        void invokeOrderPay(String orderId, String payType, String totalFee);


        /**
         * 订单编号
         *
         * @param orderNo
         */
        void invokeWxChargePay(String orderNo);

        /**
         * 订单编号
         *
         * @param orderNo
         */
        void invokeAliPayChargePay(String orderNo);
    }
}
