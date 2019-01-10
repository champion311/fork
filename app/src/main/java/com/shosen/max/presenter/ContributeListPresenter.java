package com.shosen.max.presenter;

import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.ContributeContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import io.reactivex.disposables.Disposable;

public class ContributeListPresenter extends RxPresenter<ContributeContract.ContributeListView>
        implements ContributeContract.ContributeListPresenter {
    @Override
    public void getContributeList(int pageNum) {
        if (!LoginUtils.isLogin) {
            return;
        }
        String phone = LoginUtils.getUser().getPhone();
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).
                getContributeList(phone, pageNum).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        mView.showContributeList(accept, pageNum);
                    }

                }, throwable -> {
                    if (mView != null) {
                        mView.showErrorMessage(throwable);
                    }
                }
        );
        addSubscribe(di);
    }

    @Override
    public void getContributeToday() {
        if (!LoginUtils.isLogin) {
            return;
        }
        String phone = LoginUtils.getUser().getPhone();
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).
                getContributeToday(phone).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        mView.showContributeList(accept, 1);
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
