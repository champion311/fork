package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.MallAddressBean;
import com.shosen.max.bean.OrderSubmitResponse;
import com.shosen.max.bean.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public interface SubmitOrderContract {

    interface View extends BaseView {
        void addressBack(List<MallAddressBean> mData);

        void showErrorMessage(Throwable throwable);

        void submitOrderBack(BaseResponse<OrderSubmitResponse> response);

        void cartCheckBack();

        void showBalanceMoney();

        void updateUser(User mUser);

    }

    interface Presenter extends BasePresenter<View> {
        void getAddressList();

        //        userId	Int	是	用户编号
//        addressId	int	是	地址自增编号
//        cartId	Int	是	默认为0
//        couponId	Int	是	默认为0
//        message	String	是
//        grouponRulesId	Int	是	默认为0
//        grouponLinkId	Int	是	默认为0
//        integralPrice	Int	否	积分
//        money	String	否	余额
//        actualPrice	String	否	实付金额
        void subMitOrder(Map<String, Object> data);

        void cartChecked(boolean isChecked, Integer[] productIds);

        //获取余额数量
        void getBalanceMoney();

        void fastBuy(String goodsId, String productId, int number);

        /**
         * @param goodsId   商品编号
         * @param productId 货品编号
         * @param number    数量
         */
        Observable<BaseResponse> getAddToShoppingCartObservable(String goodsId, String productId, int number);

    }
}
