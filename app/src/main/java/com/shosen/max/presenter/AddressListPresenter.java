package com.shosen.max.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.MallAddressBean;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.AddressContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddressListPresenter extends
        RxPresenter<AddressContract.View> implements AddressContract.Presenter {


    @Override
    public void getAddressList() {
        if (!LoginUtils.isLogin) {
            return;
        }
        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).
                addressList(LoginUtils.getUser().getUid()).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.addressBack(accept);
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
    public void modifyAddress(int position, MallAddressBean bean, boolean isDefault) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(16);
        //TODO 待修改
        map.put("userId", Integer.valueOf(LoginUtils.getUser().getUid()));
        map.put("id", bean.getId());
        map.put("mobile", bean.getMobile());
        map.put("provinceId", bean.getProvince());
        map.put("cityId", bean.getCity());
        map.put("areaId", bean.getArea());
        map.put("address", bean.getAddress());
        map.put("isDefault", isDefault);
        map.put("name",bean.getName());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).addressSave(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.editAddressBack(position, isDefault);
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
