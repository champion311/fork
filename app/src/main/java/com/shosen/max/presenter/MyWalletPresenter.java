package com.shosen.max.presenter;

import android.util.Log;

import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MyWalletContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import io.reactivex.disposables.Disposable;

public class MyWalletPresenter extends RxPresenter<MyWalletContract.View> implements
        MyWalletContract.Presenter {

    public static final int GET_BALANCE_LIST = 100;

    public static final int GET_PROPERTY_LIST = 101;

    /**
     * @param type        1,2,3
     * @param requestType 1
     */
    @Override
    public void getMoneyList(String type, int requestType, int pageNum) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).
                moneyList(LoginUtils.getUser().getPhone(), type, pageNum).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.showDataBack(accept, requestType,pageNum);
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

    /**
     * @param type        3,5
     * @param requestType 2
     */
    @Override
    public void getXProperty(String type, int requestType, int pageNum) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).
                xProList(LoginUtils.getUser().getPhone(), type, pageNum).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.showDataBack(accept, requestType,pageNum);
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
    public void getData(String type, int requestType, int pageNum) {
        if (requestType == GET_BALANCE_LIST) {
            getMoneyList(type, requestType, pageNum);
        } else if (requestType == GET_PROPERTY_LIST) {
            getXProperty(type, requestType, pageNum);
        }
    }
}
