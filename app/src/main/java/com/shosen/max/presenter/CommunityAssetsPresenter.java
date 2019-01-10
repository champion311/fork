package com.shosen.max.presenter;

import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.CommunityAssertsContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import io.reactivex.disposables.Disposable;

public class CommunityAssetsPresenter extends RxPresenter<CommunityAssertsContract.View> implements
        CommunityAssertsContract.Presenter {
    @Override
    public void getOrdersList() {
        if (!LoginUtils.isLogin) {
            return;
        }
        String phone = LoginUtils.getUser().getPhone();
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).
                getProList(phone).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        mView.showProList(accept);
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
