package com.shosen.max.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.LuckyPanReward;
import com.shosen.max.presenter.MyRewardPresenter;
import com.shosen.max.presenter.contract.LuckPanContract;
import com.shosen.max.ui.adapter.MyRewardAdapter;
import com.shosen.max.utils.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyRewardListActivity extends BaseActivity implements LuckPanContract.MyRewardView {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.rc_award_list)
    RecyclerView rcAwardList;
    @BindView(R.id.no_award_image)
    ImageView noAwardImage;
    @BindView(R.id.tv_no_award_text)
    TextView tvNoAwardText;

    private MyRewardPresenter mPresenter;

    private MyRewardAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new MyRewardPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
        mPresenter.getRewardList();
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.base_101012));
        rcAwardList.setLayoutManager(new LinearLayoutManager(this));
        tvHeadTitle.setText("我的奖品");
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_my_reward_list;
    }


    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void showRewardList(List<LuckyPanReward> mData) {
        if (mData == null || mData.size() == 0) {
            noAwardImage.setVisibility(View.VISIBLE);
            tvNoAwardText.setVisibility(View.VISIBLE);
            tvNoAwardText.setText("暂无中奖记录");
            return;
        }
        noAwardImage.setVisibility(View.GONE);
        tvNoAwardText.setVisibility(View.GONE);
        mAdapter = new MyRewardAdapter(this, mData);
        rcAwardList.setAdapter(mAdapter);


    }

    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

}
