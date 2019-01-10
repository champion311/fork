package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.OrderReChargeResponse;

public interface ReChargeContract {


    public interface View extends BaseView {
        void reChargeOrderBack(OrderReChargeResponse response);

        void showErrorMessage(Throwable throwable);
    }

    public interface Presenter extends BasePresenter<View> {
        void getReChargeOrder(String val);
    }


}
