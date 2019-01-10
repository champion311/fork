package com.shosen.max.presenter;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MyCommunityContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MyCommunityPresenter extends RxPresenter<MyCommunityContract.View> implements
        MyCommunityContract.Presenter {


    @Override
    public void getVerifyCode(String phoneNumber) {
        HashMap<String, String> map = new HashMap<>(8);
        map.put("phone", phoneNumber);
        map.put("invitorUserPhone", "");
        //map.put("code", "13520639126");
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).loginCode(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult())
                .subscribe(accept -> {
                    if (mView != null) {
                        mView.verifyCodeBack(accept.getMessage());
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.verifyCodeBack(throwable.getMessage());
                    }
                });

        addSubscribe(di);
    }

    @Override
    public void confirmVerifyCode(String phoneNumber, String smsCode, String password) {
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("phone", phoneNumber);
        map.put("smsCode", smsCode);
        map.put("password", password);
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).updatePass(requestBody).
                compose(RxUtils.ToMain())
                .subscribe(accept -> {
                    if (mView != null) {
                        mView.updateSuccess(accept);
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.verifyCodeBack(throwable.getMessage());
                    }
                });

        addSubscribe(di);

    }
}
