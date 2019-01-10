package com.shosen.max.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.presenter.ContributeListPresenter;
import com.shosen.max.ui.DataGenerator;
import com.shosen.max.ui.circle.adapter.CircleFragmentAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ContributeListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.tl_top_tab)
    TabLayout tlTopTab;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new ContributeListPresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,ContextCompat.getColor(this,R.color.home_page_color));
        tvHeadTitle.setText("贡献值明细");
        for (int i = 0; i < 2; i++) {
            String text = "";
            switch (i) {
                case 0:
                    text = "全部明细";
                    break;
                case 1:
                    text = "今日明细";
                    break;
            }
            tlTopTab.addTab(tlTopTab.newTab().setText(text));
        }
        tlTopTab.addOnTabSelectedListener(this);
        vpContainer.setAdapter(new CircleFragmentAdapter(getSupportFragmentManager(),
                DataGenerator.getContributeListFragments()));
        vpContainer.addOnPageChangeListener(this);

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_contribute_list;
    }


    @OnClick({R.id.iv_back})
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
        if (tlTopTab.getTabAt(i) != null) {
            tlTopTab.getTabAt(i).select();
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
