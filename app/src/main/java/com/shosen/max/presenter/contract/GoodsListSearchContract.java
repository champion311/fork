package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.mall.GoodsOrderList;

import java.util.List;

public interface GoodsListSearchContract {

    interface View extends BaseView {

        void showErrorMessage(Throwable throwable);

        void showData(List<GoodsOrderList> mData);

        void commonCallBack(int requestCode);

    }

    interface Presenter extends BasePresenter<View> {
        void searchGoodsList(int showType, String goodsName);

        void commonAction(int requestCode, String orderId, String extra);

    }
}
