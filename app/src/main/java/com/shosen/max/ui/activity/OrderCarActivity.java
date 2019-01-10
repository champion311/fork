package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderCarActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.tv_pay_continue)
    TextView tvPayContinue;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,ContextCompat.getColor(this,R.color.home_page_color));
        tvHeadTitle.setText("汽车商务智慧社区");
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_order_cars;
    }

    @OnClick({R.id.iv_back, R.id.tv_pay_continue})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_pay_continue:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(this, QustionnaireActivity.class));
                break;
        }
    }
}
