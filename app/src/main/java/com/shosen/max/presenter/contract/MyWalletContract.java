package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.MoneyListResponse;

import java.util.List;

public interface MyWalletContract {

    interface View extends BaseView {


        void showErrorMessage(Throwable throwable);


        void showDataBack(List<MoneyListResponse> responses, int requestType, int pageNum);

    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 获取余额列表
         *
         * @param type
         * @param requestType 1
         */
        void getMoneyList(String type, int requestType, int pageNum);

        /**
         * 获取积分列表
         *
         * @param type
         * @param requestType 2
         */
        void getXProperty(String type, int requestType, int pageNum);


        void getData(String type, int requestType, int pageNum);

    }
}
