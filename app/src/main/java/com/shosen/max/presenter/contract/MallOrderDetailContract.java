package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.mall.MallOrderDetailsResponse;

public interface MallOrderDetailContract {

    interface View extends BaseView {
        void showErrorMessage(Throwable throwable);

        void showOrderDetails(MallOrderDetailsResponse response);

        void commonCallBack(int requestCode);

        /**
         * @param baseResponse
         * @param type    0，直接加入购物车 1，跳转到生成订单界面
         */
        void addShopCartCallBack(BaseResponse baseResponse, int type);
    }

    interface Presenter extends BasePresenter<View> {

        void getMallOrderDetails(String orderId);

        /**
         *
         * @param requestCode
         * @param orderId
         * @param extra 额外参数
         */
        void commonAction(int requestCode, String orderId,String extra);

        /**
         * @param goodsId   商品编号
         * @param productId 货品编号
         * @param number    数量
         * @param type      0，直接加入购物车 1，跳转到生成订单界面
         */
        void addToShoppingCart(String goodsId, String productId, int number, int type);

    }
}
