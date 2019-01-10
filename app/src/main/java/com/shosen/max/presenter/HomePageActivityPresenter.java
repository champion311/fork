package com.shosen.max.presenter;

import android.util.Log;

import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.HomePageContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import io.reactivex.disposables.Disposable;

public class HomePageActivityPresenter extends RxPresenter<HomePageContract.HomePageActivityView>
        implements HomePageContract.HomePageActPresenter {

    @Override
    public void getLocations() {
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).getLocations().
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.showLocations(accept);
                        Log.d("tag", accept.toString());
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable);
                    }
                });
        addSubscribe(di);
    }

    @Override
    public void sendSms(String code) {
        if (!LoginUtils.isLogin) {
            return;
        }

        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).postPositionSms(LoginUtils.getUser().getPhone(), code).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        if (accept != null) {
                            mView.sendSmsBack(accept.getMsg());
                        }
                        Log.d("tag", accept.toString());
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable);
                    }
                });
        addSubscribe(di);
    }
}
