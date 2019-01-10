package com.shosen.max.presenter;

import android.util.Log;

import com.bin.david.form.data.form.IForm;
import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.GoodsListSearchContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.shosen.max.presenter.MallOrderDetailsPresenter.CANCEL_ORDER;
import static com.shosen.max.presenter.MallOrderDetailsPresenter.CONFIRM_ORDER;
import static com.shosen.max.presenter.MallOrderDetailsPresenter.DELETE_ORDER;

public class GoodsSearchPresenter extends
        RxPresenter<GoodsListSearchContract.View> implements GoodsListSearchContract.Presenter {


    @Override
    public void searchGoodsList(int showType, String goodsName) {
        if (!LoginUtils.isLogin) {
            return;
        }

        Disposable di = RetrofitClient.
                getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).
                findOrderList(LoginUtils.getUser().getUid(), showType, goodsName).
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

    @Override
    public void commonAction(int requestCode, String orderId, String extra) {
        if (!LoginUtils.isLogin || orderId == null) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("orderId", orderId);
        String url = "";
        switch (requestCode) {
            case DELETE_ORDER:
                url = "wx/order/delete";
                break;
            case CANCEL_ORDER:
                url = "wx/order/cancel";
                map.put("cancelDescrip", extra);
                map.put("userPhone", LoginUtils.getUser().getPhone());
                break;
            case CONFIRM_ORDER:
                url = "wx/order/confirm";
                break;
            default:
                break;
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).executePost(url, requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.commonCallBack(requestCode);
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
