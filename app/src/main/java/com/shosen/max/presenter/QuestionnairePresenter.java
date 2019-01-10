package com.shosen.max.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.QuestionnaireBean;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.QuestionnaireContract;
import com.shosen.max.utils.FileUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class QuestionnairePresenter extends RxPresenter<QuestionnaireContract.View> implements
        QuestionnaireContract.Presenter {

    private Context mContext;

    public QuestionnairePresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void getData() {
        String data = FileUtils.getFakeData(mContext, "questionnaire_res.json");
        if (!TextUtils.isEmpty(data)) {
            List<QuestionnaireBean> ret = new Gson().fromJson(data, new
                    TypeToken<List<QuestionnaireBean>>() {
                    }.getType());
            if (ret != null) {
                if (mView != null) {
                    mView.showQuestionData(ret);
                } else {
                    mView.showErrorMessage("json error");
                }
            }
        } else {
            if (mView != null) {
                mView.showErrorMessage("error");
            }
        }
    }

    @Override
    public void submitAnswer(List<QuestionnaireBean.AnswerResponse> mData) {
        if (!LoginUtils.isLogin || mData == null) {
            return;
        }
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(mData));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).
                subjectAns(requestBody).
                compose(RxUtils.ToMain()).subscribe(accept -> {

            if (mView != null) {
                mView.showErrorMessage(accept.getMsg());
            }
        }, throwable -> {
            if (mView != null) {
                mView.showErrorMessage(throwable.toString());
            }
        });
        addSubscribe(di);
    }
}
