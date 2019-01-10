package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.OrderResponse;

import java.util.List;

public interface MyCommunityContract {

    interface View extends BaseView {

        void verifyCodeBack(String phoneNumber);

        void showErrorMessage(Throwable throwable);

        void updateSuccess(BaseResponse baseResponse);

    }

    interface Presenter extends BasePresenter<View> {

        void getVerifyCode(String phoneNumber);

        void confirmVerifyCode(String phoneNumber, String smsCode, String password);
    }
}
