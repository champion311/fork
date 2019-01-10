package com.shosen.max.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.LoginResponse;
import com.shosen.max.constant.RegexConstants;
import com.shosen.max.presenter.LoginPresenter;
import com.shosen.max.presenter.MyCommunityPresenter;
import com.shosen.max.presenter.contract.LoginContract;
import com.shosen.max.presenter.contract.MyCommunityContract;
import com.shosen.max.utils.CommonUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.RxUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class MyCommunityForgetPassWordActivity extends BaseActivity implements
        MyCommunityContract.View {
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
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.ed_password)
    EditText edPassword;

    private MyCommunityPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new MyCommunityPresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.home_page_color));
        ivHeadTitle.setText("忘记密码");
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_my_community_forget_password;
    }


    @Override
    public void showErrorMessage(Throwable throwable) {

    }

    @Override
    public void updateSuccess(BaseResponse baseResponse) {
        ToastUtils.show(this, baseResponse.getMsg());
        if (baseResponse.getCode() == 100) {
            finish();
        }

    }

    @Override
    public void verifyCodeBack(String phoneNumber) {
        //开始倒计时
        startCountMethod();
    }


    @OnClick({R.id.iv_back, R.id.tv_get_verify, R.id.tv_next_step})
    public void onClick(View view) {
        String phone = edCellphone.getText().toString().trim();
        String verifyCode = edVerifyCode.getText().toString().trim();
        String passWord = edPassword.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_get_verify:
                if (!RegexUtils.isMobileExact(phone)) {
                    ToastUtils.show(this, "请输入正确的手机号");
                    return;
                }
                mPresenter.getVerifyCode(phone);
                break;
            case R.id.tv_next_step:
                if (!RegexUtils.isMobileExact(phone)) {
                    ToastUtils.show(this, "请输入正确的手机号");
                    return;
                }
                if (!RegexUtils.isMatch(RegexConstants.REGEX_VERIFY_CODE, verifyCode)) {
                    ToastUtils.show(this, "请输入正确的验证码");
                    return;
                }
                if (!RegexUtils.isCommunityPassWord(passWord)) {
                    ToastUtils.show(this, "请输入正确的密码");
                    return;
                }
                mPresenter.confirmVerifyCode(phone, verifyCode, CommonUtils.md5(passWord));
                break;
        }
    }

    /**
     * 开始验证码倒计时
     */
    public void startCountMethod() {
        tvGetVerify.setClickable(false);
        Disposable disposable = RxUtils.countdown(60).subscribe(integer -> {
                    if (integer > 0) {
                        tvGetVerify.setText("剩余" + integer + "秒再次获取");
                    } else {
                        integer = 0;
                        tvGetVerify.setText("获取验证码");
                        tvGetVerify.setClickable(true);
                    }
                }
                , throwable -> {
                });
        mPresenter.addSubscribe(disposable);
    }
}
