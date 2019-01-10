package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.GoodsDetailBean;
import com.shosen.max.bean.MallHomePageBean;

public interface MallHomeContract {

    interface View extends BaseView {

        void showErrorMessage(Throwable throwable);

        void showData(MallHomePageBean mData);
    }

    interface Presenter extends BasePresenter<View> {
        void loadData();
    }

    interface GoodsDetailsView extends BaseView {
        void showErrorMessage(Throwable throwable);

        void showData(GoodsDetailBean mData);

        /**
         * @param baseResponse
         * @param type    0，直接加入购物车 1，跳转到生成订单界面
         */
        void addShopCartCallBack(BaseResponse baseResponse, int type);

        void payNowCallBack(String message);

        void getGoodsCount(Integer goodsCount);
    }

    interface GoodsDetailsPresenter extends BasePresenter<GoodsDetailsView> {
        void loadDetailsData(String id);


        /**
         * @param goodsId   商品编号
         * @param productId 货品编号
         * @param number    数量
         * @param type      0，直接加入购物车 1，跳转到生成订单界面
         */
        void addToShoppingCart(String goodsId, String productId, int number, int type);

        /**
         * @param goodsId   商品编号
         * @param productId 货品编号
         * @param number    数量
         */
        void payNow(String goodsId, String productId, int number);


        void getCartCount();
    }
}


