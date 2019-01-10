package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.LocationBean;
import com.shosen.max.bean.NewsBean;

import java.util.List;

public interface HomePageContract {

    interface View extends BaseView {

        void showBannerList(List<String> data);

        void showErrorMessage(String message);

        void singSuccess(BaseResponse baseResponse);

        void showNews(List<NewsBean> mData);

    }

    interface Presenter extends BasePresenter<View> {

        void getBannerList(int type);

        //签到
        void signIn();

        void getNews();

    }

    interface HomePageActivityView extends BaseView {

        void showLocations(List<LocationBean> mData);

        void showErrorMessage(Throwable throwable);

        void sendSmsBack(String msg);
    }

    interface HomePageActPresenter extends BasePresenter<HomePageActivityView> {

        void getLocations();

        void sendSms(String code);

    }

}
