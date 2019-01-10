package com.shosen.max.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MallHomeContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class GoodsDetailPresenter extends RxPresenter<MallHomeContract.GoodsDetailsView>
        implements MallHomeContract.GoodsDetailsPresenter {

    @Override
    public void loadDetailsData(String id) {
        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).goodsDetails(id).
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

    /**
     * @param goodsId   商品编号
     * @param productId 货品编号
     * @param number    数量
     * @param type      0，直接加入购物车 1，跳转到生成订单界面
     */

    @Override
    public void addToShoppingCart(String goodsId, String productId, int number, int type) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("goodsId", goodsId);
        map.put("productId", productId);
        map.put("number", number);
        //map.put("username", LoginUtils.getUser().getPhone());
        map.put("userId", LoginUtils.getUser().getUid());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));

        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).addToShoppingCart(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.addShopCartCallBack(accept,type);
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
     * @param goodsId   商品编号
     * @param productId 货品编号
     * @param number    数量
     */
    @Override
    public void payNow(String goodsId, String productId, int number) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("goodsId", goodsId);
        map.put("productId", productId);
        map.put("number", number);
        //map.put("username", LoginUtils.getUser().getPhone());
        map.put("userId", LoginUtils.getUser().getUid());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));

        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).payNow(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.payNowCallBack(accept);
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
    public void getCartCount() {
        if (!LoginUtils.isLogin) {
            return;
        }
        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).
                getGoodsCount(LoginUtils.getUser().getUid()).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.getGoodsCount(accept);
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
