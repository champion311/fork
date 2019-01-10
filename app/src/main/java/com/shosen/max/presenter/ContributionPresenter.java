package com.shosen.max.presenter;

import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.constant.TimeConstants;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.ContributeContract;
import com.shosen.max.utils.RxUtils;

import io.reactivex.disposables.Disposable;

public class ContributionPresenter extends RxPresenter<ContributeContract.View> implements
        ContributeContract.Presenter {


    @Override
    public void getContributeToday(String phone) {
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).
                getContributeToday(phone).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        mView.showContributeToday(accept);
                    }

                }, throwable -> {
                    if (mView != null) {
                        mView.showErrorMessage(throwable);
                    }
                }
        );
        addSubscribe(di);
    }
}
