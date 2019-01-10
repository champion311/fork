package com.shosen.max.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.ContributeListBean;
import com.shosen.max.presenter.ContributionPresenter;
import com.shosen.max.presenter.contract.ContributeContract;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.BitmapCompressUtils;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.widget.CustomBarChartView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ContributionValueActivity extends BaseActivity implements ContributeContract.View {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.tv_today_contribute_value)
    TextView tvTodayContributeValue;
    @BindView(R.id.tv_today_contribute)
    TextView tvTodayContribute;
    @BindView(R.id.tv_current_contribute)
    TextView tvCurrentContribute;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.my_single_chart_view)
    CustomBarChartView mySingleChartView;
    private ContributionPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new ContributionPresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.home_page_color));
        tvHeadTitle.setText("会员中心");
        if (LoginUtils.isLogin) {
            mPresenter.getContributeToday(LoginUtils.getUser().getPhone());
            tvCurrentContribute.setText("当前贡献值 " + LoginUtils.getUser().getContribution());
        }
        initCustomBarView();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_contribution_value;
    }

    public void initCustomBarView() {
        int width = DensityUtils.dip2px(this, 26);
        if (LoginUtils.isLogin) {
            RequestOptions requestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.default_avater)
                    .error(R.drawable.default_avater)
                    .priority(Priority.HIGH);

            Glide.with(this).asBitmap().load(LoginUtils.getUser().
                    getHeadimg()).apply(requestOptions).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    int level = 0;
                    if (RegexUtils.isMatch("[0-9]", LoginUtils.getUser().getLevel())) {
                        level = Integer.valueOf(LoginUtils.getUser().getLevel()) - 1;
                    }
                    mySingleChartView.setList(level, BitmapCompressUtils.getCircleBitmap(resource, width, width, width / 2));
                }


                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    int level = 0;
                    if (RegexUtils.isMatch("[0-9]", LoginUtils.getUser().getLevel())) {
                        level = Integer.valueOf(LoginUtils.getUser().getLevel()) - 1;
                    }
                    Bitmap bitmap = BitmapFactory.decodeResource(ContributionValueActivity.this.getResources(), R.drawable.default_avater);
                    mySingleChartView.setList(level, BitmapCompressUtils.getCircleBitmap(bitmap, width, width, width / 2));
                    super.onLoadFailed(errorDrawable);
                }
            });
        }

//        new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                int level = 0;
//                if (RegexUtils.isMatch("[0-9]", LoginUtils.getUser().getLevel())) {
//                    level = Integer.valueOf(LoginUtils.getUser().getLevel()) - 1;
//                }
//                mySingleChartView.setList(level, BitmapCompressUtils.getCircleBitmap(resource, width, width, width / 2));
//            }
//        }
    }


    @Override
    public void showContributeToday(List<ContributeListBean> mData) {
        int sum = 0;
        StringBuilder sb = new StringBuilder();
        for (ContributeListBean bean : mData) {
            if (RegexUtils.isMatch("[0-9]+", bean.getTypeValue())) {
                sum += Integer.valueOf(bean.getTypeValue());
            }
            if (sb.length() != 0) {
                sb.append("\t\t");
            }
            sb.append(bean.getType() + bean.getTypeValue());
        }
        tvTodayContributeValue.setText("+" + String.valueOf(sum));

    }

    @Override
    public void showErrorMessage(Throwable throwable) {

    }

    @OnClick({R.id.iv_back, R.id.tv_details})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_details:
                ActivityUtils.startActivity(ContributeListActivity.class);
                break;
        }
    }
}
