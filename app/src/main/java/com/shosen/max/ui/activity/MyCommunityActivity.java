package com.shosen.max.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.IsBookPayResponse;
import com.shosen.max.presenter.ActivePresenter;
import com.shosen.max.presenter.contract.ActivityCommunityContract;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.CommonUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.ShareUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MyCommunityActivity extends BaseActivity implements ActivityCommunityContract.View {


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
    @BindView(R.id.tv_back_home)
    TextView tvBackHome;
    @BindView(R.id.ll_invalid)
    LinearLayout llInvalid;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.cb_password_eye)
    CheckBox cbPasswordEye;
    @BindView(R.id.ed_confirm_password)
    EditText edConfirmPassword;
    @BindView(R.id.cb_confirm_password_eye)
    CheckBox cbConfirmPasswordEye;
    @BindView(R.id.tv_exchange_now)
    TextView tvExchangeNow;
    @BindView(R.id.ll_insert_password)
    LinearLayout llInsertPassword;
    @BindView(R.id.tv_enter_community)
    TextView tvEnterCommunity;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.ll_insert_commmunity)
    LinearLayout llInsertCommmunity;
    @BindView(R.id.tv_cellphone)
    TextView tvCellphone;
    @BindView(R.id.tv_invitor_phone)
    TextView tvInvitorPhone;
    @BindView(R.id.iv_community_bg)
    ImageView ivCommunityBg;
    private ActivePresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new ActivePresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.home_page_color));
        ivHeadTitle.setText("激活");
        mPresenter.searchIsInvalid();
        if (LoginUtils.isLogin) {
            if (!TextUtils.isEmpty(LoginUtils.getUser().getPhone())) {
                tvCellphone.setText("手机号码:" + LoginUtils.getUser().getPhone());
            }

            if (!TextUtils.isEmpty(LoginUtils.getUser().getInvitorPhone())) {
                tvInvitorPhone.setText("推荐人:" + LoginUtils.getUser().getInvitorPhone());
            }
        }
    }

    @Override
    protected int getContentViewID() {

        return R.layout.activity_my_community;
    }


    @Override
    public void showErrorMessage(Throwable throwable) {

    }

    @Override
    public void handleResponse(IsBookPayResponse response) {
        if (100 == response.getIsPay()) {
            //已经购买过
            //未激活
            llInvalid.setVisibility(View.GONE);
            if (0 == response.getIsSocial()) {
                llInsertPassword.setVisibility(View.VISIBLE);
                llInsertCommmunity.setVisibility(View.GONE);
                ivHeadTitle.setText("激活");
                ivCommunityBg.setImageResource(R.drawable.community_bg);

            } else if (1 == response.getIsSocial()) {
                llInsertPassword.setVisibility(View.GONE);
                llInsertCommmunity.setVisibility(View.VISIBLE);
                ivHeadTitle.setText("进入社区");
            }
        } else {
            //没有购买过
            llInvalid.setVisibility(View.VISIBLE);
            llInsertPassword.setVisibility(View.GONE);
            ivHeadTitle.setText("社区");
        }
//        llInsertPassword.setVisibility(View.VISIBLE);
//        llInsertCommmunity.setVisibility(View.GONE);
//        llInsertPassword.setVisibility(View.GONE);
//        llInsertCommmunity.setVisibility(View.VISIBLE);

    }

    @Override
    public void activeResponse(BaseResponse response) {
        ToastUtils.show(this, response.getMsg());
//        llInsertPassword.setVisibility(View.GONE);
//        llInsertCommmunity.setVisibility(View.VISIBLE);
        if (response.getCode() == 100) {
            mPresenter.searchIsInvalid();
            ShareUtils.launchMini(this);
        }

    }


    @OnClick({R.id.iv_back, R.id.tv_back_home, R.id.tv_exchange_now,
            R.id.tv_forget_password, R.id.tv_enter_community})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_back_home:
                finish();
                break;
            case R.id.tv_exchange_now:
                String password = edPassword.getText().toString().trim();
                String confirmPassword = edConfirmPassword.getText().toString().trim();
                if (!RegexUtils.isCommunityPassWord(password)) {
                    ToastUtils.show(this, "密码格式不正确");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    ToastUtils.show(this, "密码和确认密码需要一致");
                    return;
                }
                if (!RegexUtils.isCommunityPassWord(confirmPassword)) {
                    ToastUtils.show(this, "确认密码格式不正确");
                    return;
                }
                mPresenter.activeCommunity(CommonUtils.md5(password));
                break;
            case R.id.tv_forget_password:
                ActivityUtils.startActivity(MyCommunityForgetPassWordActivity.class);
                break;
            case R.id.tv_enter_community:
                //跳转小程序
                ShareUtils.launchMini(this);
                break;
        }
    }

    @OnCheckedChanged({R.id.cb_password_eye, R.id.cb_confirm_password_eye})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        EditText editText = null;
        switch (buttonView.getId()) {
            case R.id.cb_password_eye:
                editText = edPassword;
                break;
            case R.id.cb_confirm_password_eye:
                editText = edConfirmPassword;
                break;
        }
        if (editText == null) {
            return;
        }
        if (isChecked) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @Override
    protected void onRestart() {
        mPresenter.searchIsInvalid();
        super.onRestart();
    }
}
