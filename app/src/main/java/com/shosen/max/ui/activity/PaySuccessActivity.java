package com.shosen.max.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.ui.mall.MallOrderDetailActivity;
import com.shosen.max.ui.mall.MallOrderListActivity;
import com.shosen.max.ui.mall.ShoppingCartActivity;
import com.shosen.max.ui.mall.SubmitOrderActivity;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaySuccessActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.tv_look_order)
    TextView tvLookOrder;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.tv_pay_success)
    TextView tvPaySuccess;

    private String bookId;

    private String orderId;

    private int requestType;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.home_page_color));
        tvHeadTitle.setText("支付成功");
        if (getIntent() != null) {
            bookId = getIntent().getStringExtra("bookId");
            orderId = getIntent().getStringExtra("orderId");
            requestType = getIntent().getIntExtra(PayActivity.REQUEST_TYPE, 99);
        }
        if (!TextUtils.isEmpty(orderId)) {
            //商城订单
            tvPaySuccess.setText("恭喜你支付成功");
            tvLookOrder.setText("查看订单");
        }

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_pay_success;
    }

    @OnClick({R.id.iv_back, R.id.tv_look_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_look_order:
                if (requestType == PayActivity.CHARGE_REQUEST) {
                    ActivityUtils.startActivity(MyWalletActivity.class);
                    return;
                }
                if (!TextUtils.isEmpty(bookId)) {
                    //max支付
                    ActivityUtils.finishActivity(OrderConfirmActivity.class);
                    ActivityUtils.finishActivity(OrderCarActivity.class);
                    ActivityUtils.finishActivity(PayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("bookId", bookId);
                    ActivityUtils.startActivity(MyOrderActivity.class, bundle);
                    finish();
                } else {
                    //商场支付
                    ActivityUtils.finishActivity(ShoppingCartActivity.class);
                    ActivityUtils.finishActivity(SubmitOrderActivity.class);
                    ActivityUtils.finishActivity(MallOrderDetailActivity.class);
                    ActivityUtils.startActivity(MallOrderListActivity.class);
                    ActivityUtils.finishActivity(PayActivity.class);
                }
                break;

        }
    }

}
