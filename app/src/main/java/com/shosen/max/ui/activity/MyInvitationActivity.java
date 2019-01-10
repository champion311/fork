package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.IsBookPayResponse;
import com.shosen.max.constant.Contstants;
import com.shosen.max.presenter.ActivePresenter;
import com.shosen.max.presenter.MyCommunityPresenter;
import com.shosen.max.presenter.contract.ActivityCommunityContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.ShareUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.widget.dialog.CommonConfirmDialog;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.weixin.handler.UmengWXHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyInvitationActivity extends BaseActivity implements UMShareListener
        , ActivityCommunityContract.View {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.rl_invite_code_wrapper)
    RelativeLayout rlInviteCodeWrapper;
    @BindView(R.id.tv_invite)
    TextView tvInvite;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.tv_more_rules)
    TextView tvMoreRules;

    UmengWXHandler umengWXHandler;

    private ActivePresenter mPresenter;

    private boolean hasBookedCar = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new ActivePresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.home_page_color));
        tvHeadTitle.setText("邀请有礼");
        umengWXHandler = (UmengWXHandler) UMShareAPI.get(this).getHandler(SHARE_MEDIA.WEIXIN);
        mPresenter.searchIsInvalid();

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_my_invitation;
    }

    @OnClick({R.id.iv_back, R.id.tv_invite, R.id.tv_more_rules})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_invite:
//                if (hasBookedCar) {
//                    //Test Only
//
//                    //ShareUtils.launchMini(this);
//                } else {
//                    showConfirmDialog();
//                }
                //ShareUtils.launchMini(this);
                if (LoginUtils.isLogin && hasBookedCar) {
                    ShareUtils.shareMin(this, new UMImage(this,
                                    Contstants.SHARE_MINI_UMIMAGE), Contstants.OFFICE_WEB_SITE,
                            getString(R.string.share_title), this);
                } else {
                    showConfirmDialog();
                }
                break;
            case R.id.tv_more_rules:
                Intent intent = new Intent(this, MyAllowanceDetailActivity.class);
                intent.putExtra("isShowTable", false);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //UM使用
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void showErrorMessage(Throwable throwable) {

    }

    @Override
    public void handleResponse(IsBookPayResponse response) {
        hasBookedCar = 100 == response.getIsPay();

    }

    @Override
    public void activeResponse(BaseResponse response) {

    }

    private CommonConfirmDialog dialog;

    public void showConfirmDialog() {
        if (dialog == null) {
            dialog = new CommonConfirmDialog(this, R.style.CancelDialog);
        }
        dialog.showSingle(getString(R.string.not_buy_car_alert), "确认");
        dialog.show();
    }

}
