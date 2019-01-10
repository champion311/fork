package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bin.david.form.data.form.IForm;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.AlipayReqBean;
import com.shosen.max.bean.OrderPayResponseOutter;
import com.shosen.max.bean.PayResult;
import com.shosen.max.bean.WxPayBean;
import com.shosen.max.bean.eventbusevent.HomePageRefreshEvent;
import com.shosen.max.bean.eventbusevent.WeChatPayEvent;
import com.shosen.max.constant.Contstants;
import com.shosen.max.presenter.PayPresenter;
import com.shosen.max.presenter.contract.PayContract;
import com.shosen.max.ui.mall.GoodsDetailActivity;
import com.shosen.max.ui.mall.MallOrderDetailActivity;
import com.shosen.max.ui.mall.MallOrderListActivity;
import com.shosen.max.ui.mall.ShoppingCartActivity;
import com.shosen.max.ui.mall.SubmitOrderActivity;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.dialog.CommonConfirmDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class PayActivity extends BaseActivity implements PayContract.View {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.ra_wechat)
    RadioButton raWechat;
    @BindView(R.id.ra_alipay)
    RadioButton raAlipay;
    @BindView(R.id.rg_pay)
    RadioGroup rgPay;
    @BindView(R.id.tv_pay)
    TextView tvPay;

    private static final int ALIPAY = 0x100;
    private static final int WE_CHAT_PAY = 0x101;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.tv_total_fee)
    TextView tvTotalFee;

    private String orderId;

    private String totalFee;

    //金额
    private String amount;

    public static final String REQUEST_TYPE = "requestType";


    //充值
    public static final int CHARGE_REQUEST = 103;

    //订车
    public static final int BOOK_CAR = 101;

    //商城
    public static final int MALL_REQUEST = 102;

    private PayPresenter mPresenter;
    /**
     * 订单id
     */
    private String bookId;

    private String chargeOrderId;

    public static final int ALIPAY_SDK_PAY_FLAG = 101;

    private int requestType;

    public static int payStatus;


    private IWXAPI iwxapi;
    /**
     * 处理支付宝支付结果
     */
    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALIPAY_SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //if (TextUtils.isEmpty(orderId)) {
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
                        intent.putExtra("bookId", bookId);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra(REQUEST_TYPE, requestType);
                        startActivity(intent);
                        payStatus = Contstants.PAY_SUCCESS;
                        finish();
                        //}


                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if (TextUtils.isEmpty(orderId)) {
                        } else {
                            Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(PayActivity.this, PayFailedActivity.class);
////                        intent.putExtra("bookId", bookId);
////                        startActivity(intent);
                            payStatus = Contstants.PAY_FAILED;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private AlipayHandler alipayHandler;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new PayPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initViewAndEvents() {
        payStatus = Contstants.PAY_NOT_YET;
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.home_page_color));
        tvHeadTitle.setText("确认支付");
        if (getIntent() != null) {
            bookId = getIntent().getStringExtra("bookId");
            orderId = getIntent().getStringExtra("orderId");
            totalFee = getIntent().getStringExtra("totalFee");
            chargeOrderId = getIntent().getStringExtra("charge_order_id");
            requestType = getIntent().getIntExtra(REQUEST_TYPE, 99);
            amount = getIntent().getStringExtra("amount");
        }
        iwxapi = WXAPIFactory.createWXAPI(this, Contstants.WEI_XIN_APP_ID, true);
        iwxapi.registerApp(Contstants.WEI_XIN_APP_ID);
        if (!TextUtils.isEmpty(totalFee)) {
            tvTotalFee.setText("¥ " + totalFee);
        }
        if (!TextUtils.isEmpty(amount)) {
            tvTotalFee.setText("¥ " + amount);
        }
        alipayHandler = new AlipayHandler(new WeakReference<>(this), requestType, bookId, orderId);

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_confirm_pay;
    }


    @OnClick({R.id.iv_back, R.id.tv_pay})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                payFailedAction();
                break;
            case R.id.tv_pay:
                //TODO 支付方式
                if (raAlipay.isChecked()) {
                    if (requestType == CHARGE_REQUEST) {
                        mPresenter.invokeAliPayChargePay(chargeOrderId);
                    } else {
                        if (TextUtils.isEmpty(orderId)) {
                            mPresenter.invokeAliPay(bookId);
                        } else {
                            mPresenter.invokeOrderPay(orderId, "aliPay", totalFee);
                        }
                    }
                } else if (raWechat.isChecked()) {
                    if (requestType == CHARGE_REQUEST) {
                        mPresenter.invokeWxChargePay(chargeOrderId);
                    } else {
                        if (TextUtils.isEmpty(orderId)) {
                            mPresenter.invokeWxPay(bookId);
                        } else {
                            mPresenter.invokeOrderPay(orderId, "wxAppPay", totalFee);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private ExecutorService singleThreadPool;

    public void invokeAlipay(String orderInfo) {
        // 修改为线程池模式
        singleThreadPool = new ThreadPoolExecutor(
                1, 1, 0,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = ALIPAY_SDK_PAY_FLAG;
                msg.obj = result;
                alipayHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        singleThreadPool.execute(payRunnable);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alipayHandler != null) {
            alipayHandler.removeCallbacksAndMessages(null);
        }
        if (singleThreadPool != null) {
            singleThreadPool.shutdown();
        }

    }

    @Override
    public void invokeAliPayReqBack(AlipayReqBean reqResponse) {
        //调用支付宝支付
        invokeAlipay(reqResponse.getPayStr());
    }

    @Override
    public void invokeWxPayReqBack(WxPayBean wxBean) {
        //调用微信支付 在WXPayEntryActivity 处理回调
        PayReq payReq = new PayReq();
        payReq.appId = wxBean.getAppId();
        payReq.partnerId = wxBean.getPartnerId();
        payReq.prepayId = wxBean.getPrepayId();
        payReq.packageValue = wxBean.getPackAge();
        payReq.nonceStr = wxBean.getNonceStr();
        payReq.timeStamp = wxBean.getTimeStamp();
        payReq.sign = wxBean.getSign();
        iwxapi.sendReq(payReq);
    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
    }

    @Override
    public void invokeOrderPayResponse(OrderPayResponseOutter response) {
        if (response == null) {
            return;
        }
        if (!TextUtils.isEmpty(response.getData().getPayStr())) {
            invokeAlipay(response.getData().getPayStr());
        } else {
            invokeWxPayReqBack(response.getData());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeChatPayEvent event) {
        //微信支付结果
        payStatus = event.getPayResStatus();
        if (event.getPayResStatus() == Contstants.PAY_SUCCESS) {
            Intent intent = new Intent(this, PaySuccessActivity.class);
            intent.putExtra("bookId", bookId);
            intent.putExtra("orderId", orderId);
            intent.putExtra(REQUEST_TYPE, requestType);
            startActivity(intent);
        } else {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        payFailedAction();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            payFailedAction();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void payFailedAction() {
        CommonConfirmDialog confirmDialog = new CommonConfirmDialog(this, R.style.CancelDialog);
        confirmDialog.setOnCancelConfirmClick(new CommonConfirmDialog.OnCancelConfirmClick() {
            @Override
            public void onCancelClickListener() {
                handleActions();
            }
        });
        confirmDialog.setText("是否离开支付页面？", "确认离开", "继续支付");
        confirmDialog.show();

    }

    public void handleActions() {
        if (requestType == CHARGE_REQUEST) {
            ActivityUtils.startActivity(MyWalletActivity.class);
            return;
        }
        EventBus.getDefault().postSticky(new HomePageRefreshEvent(4));
        if (TextUtils.isEmpty(orderId)) {
            //max订单的列表
            ActivityUtils.finishActivity(OrderConfirmActivity.class);
            ActivityUtils.finishActivity(OrderCarActivity.class);
            ActivityUtils.finishActivity(QustionnaireActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("bookId", bookId);
            ActivityUtils.startActivity(MyOrderActivity.class, bundle);
            ActivityUtils.finishActivity(PayActivity.class);
        }
        if (!TextUtils.isEmpty(orderId)) {
            //调转商品订单列表
            ActivityUtils.finishActivity(ShoppingCartActivity.class);
            ActivityUtils.finishActivity(SubmitOrderActivity.class);
            ActivityUtils.finishActivity(MallOrderDetailActivity.class);
            ActivityUtils.finishActivity(GoodsDetailActivity.class);
            ActivityUtils.startActivity(MallOrderListActivity.class);
            ActivityUtils.finishActivity(PayActivity.class);
        }
        finish();
    }


    public static class AlipayHandler extends Handler {

        private WeakReference<PayActivity> weakReference;

        private int requestType;

        private String bookId;

        private String orderId;

        public AlipayHandler(WeakReference<PayActivity> weakReference, int requestType, String bookId, String orderId) {
            this.weakReference = weakReference;
            this.requestType = requestType;
            this.bookId = bookId;
            this.orderId = orderId;
        }

        @Override

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALIPAY_SDK_PAY_FLAG:
                    PayActivity payActivity = weakReference.get();
                    if (payActivity == null) {
                        return;
                    }
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功 6001为取消
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //if (TextUtils.isEmpty(orderId)) {
                        Toast.makeText(weakReference.get(), "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(payActivity, PaySuccessActivity.class);
                        intent.putExtra("bookId", bookId);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra(REQUEST_TYPE, requestType);
                        payActivity.startActivity(intent);
                        payStatus = Contstants.PAY_SUCCESS;
                        payActivity.finish();
                        //}
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if (TextUtils.isEmpty(orderId)) {                        // 该笔订单真实的支付结果，需要依赖服务端的异
                        } else {
                            Toast.makeText(payActivity, "支付失败", Toast.LENGTH_SHORT).show();
//                          Intent intent = new Intent(PayActivity.this, PayFailedActivity.class);
////                        intent.putExtra("bookId", bookId);
////                        startActivity(intent);
                            payStatus = Contstants.PAY_FAILED;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
