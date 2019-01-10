package com.shosen.max.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.OrderReChargeResponse;
import com.shosen.max.presenter.ReChargePresenter;
import com.shosen.max.presenter.contract.ReChargeContract;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.MultiRadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity implements
        MultiRadioGroup.OnCheckedChangeListener, ReChargeContract.View {
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
    @BindView(R.id.ra_1)
    RadioButton ra1;
    @BindView(R.id.ra_2)
    RadioButton ra2;
    @BindView(R.id.ra_3)
    RadioButton ra3;
    @BindView(R.id.ra_4)
    RadioButton ra4;
    @BindView(R.id.tv_exchange_now)
    TextView tvExchangeNow;
    @BindView(R.id.ed_charge_money)
    EditText edChargeMoney;
    @BindView(R.id.muti_radio)
    MultiRadioGroup mutiRadio;

    private ReChargePresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new ReChargePresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.home_page_color));
        ivHeadTitle.setText("充值");
        mutiRadio.setOnCheckedChangeListener(this);


    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_rechage;
    }

    @OnClick({R.id.iv_back, R.id.tv_exchange_now})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_exchange_now:
                String val = edChargeMoney.getText().toString().trim();
                if (TextUtils.isEmpty(val)) {
                    ToastUtils.show(this, "请选择金额");
                    return;
                }
                mPresenter.getReChargeOrder(val);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton button = group.findViewById(checkedId);
        String text = button.getText().toString().trim();
        int index = text.indexOf("¥");
        edChargeMoney.setText(text.substring(index + 1, text.length()));
    }

    @Override
    public void reChargeOrderBack(OrderReChargeResponse response) {
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra("charge_order_id", response.getChargeNo());
        intent.putExtra(PayActivity.REQUEST_TYPE, PayActivity.CHARGE_REQUEST);
        intent.putExtra("amount", response.getChargeVal());
        startActivity(intent);
    }

    @Override
    public void showErrorMessage(Throwable throwable) {

    }
}
