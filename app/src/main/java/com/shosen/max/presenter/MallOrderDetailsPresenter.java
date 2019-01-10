package com.shosen.max.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MallGoodsOrderContract;
import com.shosen.max.presenter.contract.MallOrderDetailContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MallOrderDetailsPresenter extends RxPresenter<MallOrderDetailContract.View>
        implements MallOrderDetailContract.Presenter {

    @Override
    public void getMallOrderDetails(String orderId) {
        if (!LoginUtils.isLogin || orderId == null) {
            return;
        }
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("userId", Integer.valueOf());
//        map.put("orderId", Integer.valueOf(orderId));
//        RequestBody requestBody = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).mallOrderDetails
                (LoginUtils.getUser().getUid(), orderId).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.showOrderDetails(accept);
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
        String url = "";
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("orderId", orderId);
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


    public static final int DELETE_ORDER = 100;

    public static final int CANCEL_ORDER = 101;

    public static final int CONFIRM_ORDER = 102;

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
                        mView.addShopCartCallBack(accept, type);
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
