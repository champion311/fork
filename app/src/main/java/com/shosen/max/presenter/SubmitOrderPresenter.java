package com.shosen.max.presenter;

import android.util.Log;

import com.bin.david.form.data.form.IForm;
import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.OrderSubmitResponse;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.SubmitOrderContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SubmitOrderPresenter extends RxPresenter<SubmitOrderContract.View> implements
        SubmitOrderContract.Presenter {
    /**
     * 获取地址列表
     */
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

    /**
     * 提交订单
     *
     * @param data
     */
    @Override
    public void subMitOrder(Map<String, Object> data) {
        if (!LoginUtils.isLogin) {
            return;
        }
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(data));
        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).submitOrder(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        //mView.delAddressBack();
                        //mView.submitOrderBack();
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
     * 从购物车直接操作
     *
     * @param data
     * @param checkedIds
     * @param uncheckedIds
     */
    public void subMitOrder(Map<String, Object> data, Integer[] checkedIds, Integer[] uncheckedIds) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Disposable di = Observable.zip(getCartCheckObservable(true, checkedIds),
                getCartCheckObservable(false, uncheckedIds),
                new BiFunction<BaseResponse, BaseResponse, Boolean>() {
                    @Override
                    public Boolean apply(BaseResponse o, BaseResponse o2) throws Exception {
                        return true;
                    }
                }).flatMap(aBoolean ->
                getSubmitOrderObservable(data, checkedIds)
        ).compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.submitOrderBack(accept);
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.showErrorMessage(throwable);
                    }
                });
        addSubscribe(di);
    }

    /**
     * 只处理checkedIds true时
     *
     * @param data
     * @param checkedIds
     */
    public void subMitOrder(Map<String, Object> data, Integer[] checkedIds) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Disposable di = getCartCheckObservable(true, checkedIds).flatMap(baseResponse ->
                getSubmitOrderObservable(data, checkedIds)
        ).compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.submitOrderBack(accept);
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.showErrorMessage(throwable);
                    }
                });
        addSubscribe(di);
    }

    /**
     * 预先加入购物车的情况
     *
     * @param data
     * @param checkedIds
     * @param goodsId
     * @param productId
     * @param number
     */
    public void subMitOrder(Map<String, Object> data,
                            Integer[] checkedIds, String goodsId, String productId, int number) {
        if (!LoginUtils.isLogin) {
            return;
        }

        Disposable di = getFastBuyObservable(goodsId, productId, number).
                flatMap(baseResponse ->
                        getCartCheckObservable(true, checkedIds))
                .flatMap(baseResponse ->
                        getSubmitOrderObservable(data, checkedIds)
                ).compose(RxUtils.ToMain()).
                        subscribe(accept -> {
                            if (mView != null) {
                                mView.submitOrderBack(accept);
                            }
                        }, throwable -> {
                            if (mView != null) {
                                mView.showErrorMessage(throwable);
                            }
                        });
        addSubscribe(di);

    }


    @Override
    public void cartChecked(boolean isChecked, Integer[] productIds) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("productIds", productIds);
        map.put("isChecked", isChecked);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).cartChecked(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.cartCheckBack();
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
     * 获取selectUserByPhone余额数量
     */

    @Override
    public void getBalanceMoney() {

        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", LoginUtils.getUser().getPhone());
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).selectUserByPhone(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(accept -> {
            if (mView != null) {
                mView.updateUser(accept);
                Log.d("tag", accept.toString());
            }

        }, throwable -> {
            if (mView != null) {
                // mView.updateDataFailed(throwable.toString());
                Log.d("tag", throwable.toString());
            }
        });
        addSubscribe(di);
    }

    @Override
    public void fastBuy(String goodsId, String productId, int number) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>(8);
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("goodsId", goodsId);
        map.put("productId", productId);
        map.put("number", number);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));

        Disposable di = RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).fastBuy(requestBody).
                compose(RxUtils.ToMain()).subscribe(accept -> {
            if (mView != null) {
                mView.submitOrderBack(accept);
                Log.d("tag", accept.toString());
            }

        }, throwable -> {
            if (mView != null) {
                // mView.updateDataFailed(throwable.toString());
                Log.d("tag", throwable.toString());
            }
        });
        addSubscribe(di);

    }

    public Observable<BaseResponse> getFastBuyObservable(String goodsId, String productId, int number) {
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("goodsId", goodsId);
        map.put("productId", productId);
        map.put("number", number);
        map.put("userId", LoginUtils.getUser().getUid());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        return RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).fastBuy(requestBody);
    }


    /**
     * 获取加入购物车Observable
     *
     * @param goodsId   商品编号
     * @param productId 货品编号
     * @param number    数量
     * @return
     */
    @Override
    public Observable<BaseResponse>
    getAddToShoppingCartObservable(String goodsId, String productId, int number) {
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("goodsId", goodsId);
        map.put("productId", productId);
        map.put("number", number);
        //map.put("username", LoginUtils.getUser().getPhone());
        map.put("userId", LoginUtils.getUser().getUid());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        return RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).addToShoppingCart(requestBody);
    }

    /**
     * 获取提交订单实例
     *
     * @param data
     * @return
     */
    public Observable<BaseResponse<OrderSubmitResponse>> getSubmitOrderObservable(Map<String, Object> data) {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(data));
        return RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).submitOrder(requestBody);
    }

    /**
     * 获取提交订单实例
     *
     * @param data
     * @return
     */
    public Observable<BaseResponse<OrderSubmitResponse>> getSubmitOrderObservable(Map<String, Object>
                                                                                          data, Integer[] checkId) {
        data.put("productIds", checkId);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(data));
        return RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).submitOrder(requestBody);
    }

    /**
     * 获取修改选取实例
     *
     * @param isChecked
     * @param productIds
     * @return
     */

    public Observable<BaseResponse> getCartCheckObservable(boolean isChecked, Integer[] productIds) {
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("productIds", productIds);
        map.put("isChecked", isChecked);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        return RetrofitClient.getInstance(RetrofitClient.MALL_URL).
                create(ApiService.class, RetrofitClient.MALL_URL).cartChecked(requestBody);
    }
}
