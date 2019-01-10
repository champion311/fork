package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.ShoppingCartBean;

public interface ShoppingCartContract {

    interface View extends BaseView {
        void showErrorMessage(Throwable throwable);

        void showCartIndex(ShoppingCartBean bean);

        void delCartItemBack();

        void updateCartBack();
    }

    interface Presenter extends BasePresenter<View> {
        void getShoppingCartData();

        void delCartItem(Integer[] productIds);

//        id	int	是	商品自增id
//        username	String	是	手机号
//        goodsId	int	是	商品编号
//        number	int	是	数量
//        productId	int	是	货品编号

        /**
         * @param id        商品自增id
         * @param goodsId   商品编号
         * @param productId 货品编号
         * @param number    数量
         */
        void cartUpdate(int id, int goodsId, int productId, int number);
    }
}
