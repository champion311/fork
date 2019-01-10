package com.shosen.max.presenter;

import android.util.Log;

import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MallHomeContract;
import com.shosen.max.utils.RxUtils;

import io.reactivex.disposables.Disposable;

public class MallHomePresenter extends
        RxPresenter<MallHomeContract.View> implements MallHomeContract.Presenter {
    @Override
    public void loadData() {
        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class,RetrofitClient.MALL_URL).hallHomeIndex().
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.showData(accept);
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
