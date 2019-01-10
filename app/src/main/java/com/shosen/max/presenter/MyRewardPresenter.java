package com.shosen.max.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.LuckyPanReward;
import com.shosen.max.bean.RewardListBean;
import com.shosen.max.constant.Contstants;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.LuckPanContract;
import com.shosen.max.utils.FileUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MyRewardPresenter extends RxPresenter
        <LuckPanContract.MyRewardView> implements LuckPanContract.MyRewardPresenter {

    private Context mContext;

    private ApiService apiService;

    public MyRewardPresenter(Context mContext) {
        this.mContext = mContext;
        this.apiService = RetrofitClient.getInstance().getApiService();
    }


    @Override
    public void getRewardList() {

        if (Contstants.IS_LOAD_FAKE_DATA) {
            String data = FileUtils.getFakeData(mContext, "fakejson_2.json");
            if (!TextUtils.isEmpty(data)) {
                BaseResponse<List<LuckyPanReward>> ret = new Gson().fromJson(data, new
                        TypeToken<BaseResponse<List<LuckyPanReward>>>() {
                        }.getType());
                if (ret != null) {
                    if (mView != null) {
                        mView.showRewardList(ret.getData());
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
        if (!LoginUtils.isLogin) {
            return;
        }

        HashMap<String, Object> map = new HashMap<>(8);
        map.put("user", LoginUtils.getUser());
        map.put("phone", LoginUtils.getUser().getPhone());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).getMineRewardList(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        mView.showRewardList(accept);
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
}
