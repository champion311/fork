package com.shosen.max.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.LoginResponse;
import com.shosen.max.presenter.ActivePresenter;
import com.shosen.max.presenter.LoginPresenter;
import com.shosen.max.presenter.contract.LoginContract;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCommunityConfirmPassWordActivity
        extends BaseActivity  {

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
    @BindView(R.id.tv_cellphone)
    TextView tvCellphone;
    @BindView(R.id.ed_cellphone)
    EditText edCellphone;
    @BindView(R.id.tv_verify_code)
    TextView tvVerifyCode;
    @BindView(R.id.ed_verify_code)
    EditText edVerifyCode;
    @BindView(R.id.tv_get_verify)
    TextView tvGetVerify;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;



    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                ContextCompat.getColor(this, R.color.home_page_color));
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_my_community_forget_password;
    }

}
