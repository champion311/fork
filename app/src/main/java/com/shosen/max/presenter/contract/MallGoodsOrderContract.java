package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.mall.GoodsOrderList;

import java.util.List;

public interface MallGoodsOrderContract {

    interface View extends BaseView {

        void showOrderListData(List<GoodsOrderList> mData);

        void showErrorMessage(Throwable throwable);

        void commonCallBack(int requestCode);
    }

    interface Presenter extends BasePresenter<View> {
        void getMallOrderList(int showType);

        void commonAction(int requestCode, String orderId,String extra);

    }
}
