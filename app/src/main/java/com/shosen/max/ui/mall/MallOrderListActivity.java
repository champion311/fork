package com.shosen.max.ui.mall;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.ui.DataGenerator;
import com.shosen.max.ui.circle.adapter.CircleFragmentAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MallOrderListActivity extends BaseActivity implements
        TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {


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
    @BindView(R.id.tl_top_tab)
    TabLayout tlTopTab;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.home_page_color));
        ivHeadTitle.setText("商品订单");
        tlTopTab.addOnTabSelectedListener(this);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setImageResource(R.drawable.orange_search_icon);
        for (int i = 0; i < DataGenerator.MALL_TEXTS.length; i++) {
            tlTopTab.addTab(tlTopTab.newTab().setText(DataGenerator.MALL_TEXTS[i]));
        }
        vpContainer.setAdapter(new CircleFragmentAdapter(getSupportFragmentManager(),
                DataGenerator.getMallOrderListFragments()));
        vpContainer.setOffscreenPageLimit(1);
        vpContainer.addOnPageChangeListener(this);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_goods_order_whole;
    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
            case R.id.tv_right:
                ActivityUtils.startActivity(this, MallOrderSearchActivity.class);
                break;
        }
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
        tlTopTab.getTabAt(i).select();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
