package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.ContributeListBean;

import java.util.List;

public interface ContributeContract {

    interface View extends BaseView {

        void showContributeToday(List<ContributeListBean> mData);

        void showErrorMessage(Throwable throwable);

    }

    interface Presenter extends BasePresenter<View> {

        //void getContributeList(String phone, int pageNum);

        void getContributeToday(String phone);
    }

    interface ContributeListView extends BaseView {

        void showContributeList(List<ContributeListBean> mData, int pageNum);

        void showErrorMessage(Throwable throwable);

    }

    interface ContributeListPresenter extends BasePresenter<ContributeListView> {

        void getContributeList(int pageNum);

        void getContributeToday();
    }
}
