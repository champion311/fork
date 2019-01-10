package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.OrderResponse;
import com.shosen.max.bean.TableBean;

import java.util.List;

public interface CommunityAssertsContract {

    interface View extends BaseView {

        void showProList(List<TableBean> mData);

        void showErrorMessage(Throwable throwable);

    }

    interface Presenter extends BasePresenter<View> {

        void getOrdersList();
    }
}
