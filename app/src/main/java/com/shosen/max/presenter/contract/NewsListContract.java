package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.NewsBean;
import com.shosen.max.bean.OrderResponse;

import java.util.List;

public interface NewsListContract {

    interface View extends BaseView {

        void showNews(List<NewsBean> mData);

        void showErrorMessage(String message);


    }

    interface Presenter extends BasePresenter<View> {

        void getNews();


    }
}
