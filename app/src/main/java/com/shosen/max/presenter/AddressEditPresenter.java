package com.shosen.max.presenter;

import android.util.Log;

import com.bin.david.form.data.form.IForm;
import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.AddressContract;
import com.shosen.max.ui.mall.AddressEditActivity;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddressEditPresenter extends RxPresenter<AddressContract.EditView> implements
        AddressContract.EditPresenter {
    @Override
    public void addOrModifyAddress(String id,
                                   String name,
                                   String mobile,
                                   String provinceId,
                                   String cityId, String areaId, String address, boolean isDefault) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(16);
        //TODO 待修改
        map.put("userId", Integer.valueOf(LoginUtils.getUser().getUid()));
        map.put("id", id);
        map.put("mobile", mobile);
        map.put("provinceId", provinceId);
        map.put("cityId", cityId);
        map.put("areaId", areaId);
        map.put("address", address);
        map.put("isDefault", isDefault);
        map.put("name", name);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).addressSave(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.addOrModifyBack();
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
    public void delAddress(String id) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(16);
        //TODO 待修改
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("id", id);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).addressDel(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.delAddressBack();
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
