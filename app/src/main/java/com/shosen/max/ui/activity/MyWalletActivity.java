package com.shosen.max.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.ui.DataGenerator;
import com.shosen.max.ui.circle.adapter.CircleFragmentAdapter;
import com.shosen.max.ui.others.HelpActivity;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.CommonUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.umeng.commonsdk.debug.E;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyWalletActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_head_title)
    TextView ivHeadTitle;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.tv_my_balance)
    TextView tvMyBalance;
    @BindView(R.id.tv_balance_value)
    TextView tvBalanceValue;
    @BindView(R.id.tv_balance_recharge)
    TextView tvBalanceRecharge;
    @BindView(R.id.tv_my_point)
    TextView tvMyPoint;
    @BindView(R.id.tv_my_point_value)
    TextView tvMyPointValue;
    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.home_page_color));

        ivHeadTitle.setText("我的钱包");
        tlTab.addOnTabSelectedListener(this);
        for (int i = 0; i < DataGenerator.WALLET_TEXT.length; i++) {
            tlTab.addTab(tlTab.newTab().setText(DataGenerator.WALLET_TEXT[i]));
        }
        vpContainer.setAdapter(new CircleFragmentAdapter(getSupportFragmentManager(),
                DataGenerator.getWalletFragments()));
        vpContainer.setOffscreenPageLimit(1);
        vpContainer.addOnPageChangeListener(this);
        if (LoginUtils.isLogin) {
            tvBalanceValue.setText(CommonUtils.getFormatPrice(LoginUtils.getUser().getMoney(), false));
            tvMyPointValue.setText(LoginUtils.getUser().getxProperty());
        }
        rightText.setVisibility(View.VISIBLE);
        rightText.setTextColor(ContextCompat.getColor(this, R.color.white));
        rightText.setText("帮助");
    }

    @Override
    protected int getContentViewID() {

        return R.layout.activity_my_wallet;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        int position = tab.getPosition();
        vpContainer.setCurrentItem(position);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        tlTab.getTabAt(i).select();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @OnClick({R.id.iv_back, R.id.tv_balance_recharge, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_balance_recharge:
                ActivityUtils.startActivity(RechargeActivity.class);
                break;
            case R.id.right_text:
                ActivityUtils.startActivity(HelpActivity.class);
                break;
        }
    }
}
