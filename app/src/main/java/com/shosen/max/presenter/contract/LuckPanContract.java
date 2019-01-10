package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.LotteryResponse;
import com.shosen.max.bean.LuckyPanReward;
import com.shosen.max.bean.RewardListBean;

import java.util.List;

public interface LuckPanContract {

    interface View extends BaseView {
        void showErrorMessage(String message);

        void showDictionaryData(int type, List<DictionaryBean> mData);

        void showLotteryResponse(LotteryResponse response);

        void showAllRewardList(List<LuckyPanReward> mData);

        void showLeftLotteryTime(String time);
    }

    interface Presenter extends BasePresenter<View> {

        void loadPanData(int type);

        void lottery(String phoneNum);

        void getRewardList();

        void getLotteryLeftTimes();
    }

    interface MyRewardView extends BaseView {
        void showErrorMessage(String message);

        void showRewardList(List<LuckyPanReward> mData);

    }

    interface MyRewardPresenter extends BasePresenter<MyRewardView> {

        void getRewardList();

    }

}
