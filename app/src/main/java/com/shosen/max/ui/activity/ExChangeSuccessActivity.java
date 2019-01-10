package com.shosen.max.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.ui.mall.GoodsDetailActivity;
import com.shosen.max.ui.mall.MallOrderDetailActivity;
import com.shosen.max.ui.mall.MallOrderListActivity;
import com.shosen.max.ui.mall.ShoppingCartActivity;
import com.shosen.max.ui.mall.SubmitOrderActivity;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExChangeSuccessActivity extends BaseActivity {
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
    @BindView(R.id.tv_look_order)
    TextView tvLookOrder;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.home_page_color));
        ivHeadTitle.setText("支付成功");
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_exchange_success;
    }

    @OnClick({R.id.iv_back, R.id.tv_look_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_look_order:
                //商场支付
                ActivityUtils.finishActivity(ShoppingCartActivity.class);
                ActivityUtils.finishActivity(SubmitOrderActivity.class);
                ActivityUtils.finishActivity(MallOrderDetailActivity.class);
                ActivityUtils.startActivity(MallOrderListActivity.class);
                ActivityUtils.finishActivity(GoodsDetailActivity.class);
                ActivityUtils.finishActivity(PayActivity.class);
                finish();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityUtils.finishActivity(ShoppingCartActivity.class);
            ActivityUtils.finishActivity(SubmitOrderActivity.class);
            ActivityUtils.finishActivity(MallOrderDetailActivity.class);
            ActivityUtils.startActivity(MallOrderListActivity.class);
            ActivityUtils.finishActivity(GoodsDetailActivity.class);
            ActivityUtils.finishActivity(PayActivity.class);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
