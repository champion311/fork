package com.shosen.max.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.constant.Contstants;
import com.shosen.max.constant.TimeConstants;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.LuckPanContract;
import com.shosen.max.utils.CacheUtils;
import com.shosen.max.utils.FileUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class LuckPanPresenter extends RxPresenter<LuckPanContract.View>
        implements LuckPanContract.Presenter {


    private Context mContext;

    private ApiService apiService;

    public LuckPanPresenter(Context mContext) {
        this.mContext = mContext;
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    @Override
    public void loadPanData(int type) {

        if (Contstants.IS_LOAD_FAKE_DATA) {
            String data = FileUtils.getFakeData(mContext, "fakejson_1.json");
            if (!TextUtils.isEmpty(data)) {
                BaseResponse<List<DictionaryBean>> ret = new Gson().fromJson(data, new
                        TypeToken<BaseResponse<List<DictionaryBean>>>() {
                        }.getType());
                if (ret != null) {
                    if (mView != null) {
                        mView.showDictionaryData(type, ret.getData());
                    } else {
                        mView.showErrorMessage("json error");
                    }
                }
            } else {
                if (mView != null) {
                    mView.showErrorMessage("error");
                }
            }
            return;
        }
        Gson gson = new Gson();
        CacheUtils cacheUtils = CacheUtils.getInstance(Contstants.DICTIONARY_CACHE);
//        if (!TextUtils.isEmpty(cacheUtils.getString(String.valueOf(type)))) {
//            List<DictionaryBean> accept = gson.fromJson(cacheUtils.getString(String.valueOf(type)),
//                    new TypeToken<List<DictionaryBean>>() {
//                    }.getType());
//            mView.showDictionaryData(type, accept);
//            return;
//        }
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).getDictionary(String.valueOf(type)).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        cacheUtils.put(String.valueOf(type), gson.toJson(accept), 1 * TimeConstants.DAY);
                        mView.showDictionaryData(type, accept);
                        //cacheUtils.put(String.valueOf(type), gson.toJson(accept), 7 * TimeConstants.DAY);
                    }

                }, throwable -> {
                    if (mView != null) {
                        mView.showErrorMessage(throwable.getMessage());
                    }
                }
        );
        addSubscribe(di);
    }

    /**
     * 抽奖
     *
     * @param phoneNum
     */
    @Override
    public void lottery(String phoneNum) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).
                lottery(phoneNum).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        mView.showLotteryResponse(accept);
                    }

                }, throwable -> {
                    if (mView != null) {
                        mView.showErrorMessage(throwable.getMessage());
                    }
                }
        );
        addSubscribe(di);
    }


    /**
     * 获取所有获奖列表
     */
    @Override
    public void getRewardList() {
        if (!LoginUtils.isLogin) {
            return;
        }
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).getPanRewardList(LoginUtils.getUser().getPhone()).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        mView.showAllRewardList(accept);
                    }

                }, throwable -> {
                    if (mView != null) {
                        mView.showErrorMessage(throwable.getMessage());
                    }
                }
        );
        addSubscribe(di);
    }

    /**
     * 剩余抽奖次数
     */
    @Override
    public void getLotteryLeftTimes() {
        if (!LoginUtils.isLogin) {
            return;
        }
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).
                getLeftLotteryTime(LoginUtils.getUser().getPhone()).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        mView.showLeftLotteryTime(accept);
                    }

                }, throwable -> {
                    if (mView != null) {
                        mView.showErrorMessage(throwable.getMessage());
                    }
                }
        );
        addSubscribe(di);
    }
}
