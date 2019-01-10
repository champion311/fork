package com.shosen.max.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.ReChargeContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ReChargePresenter extends RxPresenter<ReChargeContract.View>
        implements ReChargeContract.Presenter {

    @Override
    public void getReChargeOrder(String val) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", LoginUtils.getUser().getPhone());
        map.put("val", val);
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));

        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).getUserCharge(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult())
                .subscribe(accept -> {
                    Log.d("PayPresenter", accept.toString());
                    if (mView != null) {
                        mView.reChargeOrderBack(accept);
                    }
                }, throwable -> {
                    Log.d("PayPresenter", throwable.toString());
                    if (mView != null) {
                        mView.showErrorMessage(throwable);
                    }
                });
        addSubscribe(di);

    }
}
