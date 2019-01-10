package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.IsBookPayResponse;

public interface ActivityCommunityContract {

    interface View extends BaseView {

        void showErrorMessage(Throwable throwable);

        void handleResponse(IsBookPayResponse response);

        void activeResponse(BaseResponse response);
    }

    interface Presenter extends BasePresenter<View> {
        void searchIsInvalid();

        void activeCommunity(String password);

    }
}