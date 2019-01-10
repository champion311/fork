package com.shosen.max.presenter;

import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.NewsListContract;
import com.shosen.max.utils.RxUtils;

import io.reactivex.disposables.Disposable;

public class NewsListPresenter extends
        RxPresenter<NewsListContract.View> implements NewsListContract.Presenter {
    @Override
    public void getNews() {
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).
                getNews().
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        mView.showNews(accept);
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
