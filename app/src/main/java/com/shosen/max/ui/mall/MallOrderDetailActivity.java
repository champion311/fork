package com.shosen.max.ui.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.mall.CartList;
import com.shosen.max.bean.mall.MallOrderDetailsResponse;
import com.shosen.max.presenter.MallOrderDetailsPresenter;
import com.shosen.max.presenter.contract.MallOrderDetailContract;
import com.shosen.max.ui.activity.PayActivity;
import com.shosen.max.ui.mall.adapter.ShopCartAdapter;
import com.shosen.max.utils.CommonUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.utils.Utils;
import com.shosen.max.widget.dialog.CancelButtonClick;
import com.shosen.max.widget.dialog.CancelOrderDialog;
import com.shosen.max.widget.dialog.CommonConfirmDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.OnClick;

import static com.shosen.max.ui.mall.SubmitOrderActivity.FROM_GOODS_DETAILS;

public class MallOrderDetailActivity extends BaseActivity
        implements MallOrderDetailContract.View, CancelButtonClick {
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
    @BindView(R.id.tv_order_state)
    TextView tvOrderState;
    @BindView(R.id.tv_left_time)
    TextView tvLeftTime;
    @BindView(R.id.fl_order_state)
    FrameLayout flOrderState;
    @BindView(R.id.tv_logistic_text)
    TextView tvLogisticText;
    @BindView(R.id.iv_logistic_icon)
    ImageView ivLogisticIcon;
    @BindView(R.id.rl_logistic)
    RelativeLayout rlLogistic;
    @BindView(R.id.tv_address_text)
    TextView tvAddressText;
    @BindView(R.id.iv_address_icon)
    ImageView ivAddressIcon;
    @BindView(R.id.tv_address_name)
    TextView tvAddressName;
    @BindView(R.id.tv_address_default)
    TextView tvAddressDefault;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.rc_goods_list)
    RecyclerView rcGoodsList;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_goods_value)
    TextView tvGoodsValue;
    @BindView(R.id.tv_freight)
    TextView tvFreight;
    @BindView(R.id.tv_freight_value)
    TextView tvFreightValue;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_balance_value)
    TextView tvBalanceValue;
    @BindView(R.id.tv_pay_value)
    TextView tvPayValue;
    @BindView(R.id.tv_pay_state)
    TextView tvPayState;
    @BindView(R.id.tv_cancel_order)
    TextView tvCancelOrder;
    @BindView(R.id.tv_order_confirm_buy)
    TextView tvOrderConfirmBuy;
    @BindView(R.id.tv_address_consigneen_phone)
    TextView tvAddressConsigneenPhone;
    @BindView(R.id.tv_address_text_value)
    TextView tvAddressTextValue;
    @BindView(R.id.order_sn)
    TextView orderSn;
    @BindView(R.id.order_add_time)
    TextView orderAddTime;

    private String orderId;

    private MallOrderDetailsPresenter mPresenter;

    private int orderStatus;

    private CancelOrderDialog cancelOrderDialog;

    private CommonConfirmDialog delOrderDialog;

    private CommonConfirmDialog confirmDialog;

    private MallOrderDetailsResponse response;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new MallOrderDetailsPresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.home_page_color));
        ivHeadTitle.setText("订单详情");
        rlLogistic.setVisibility(View.GONE);
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");
        }
        initTopHeader();
        rcGoodsList.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.getMallOrderDetails(orderId);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_mall_order_detail;
    }


    @Override
    public void initTopHeader() {
        RelativeLayout rlTop = (RelativeLayout) findViewById(R.id.rl_top_header);
        if (rlTop != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rlTop.getLayoutParams();
            layoutParams.topMargin =
                    StatusBarUtil.getStatusBarHeight(this);
            rlTop.setLayoutParams(layoutParams);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel_order, R.id.tv_order_confirm_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_cancel_order:
                handleActionsLeft();
                break;
            case R.id.tv_order_confirm_buy:
                handleActionRight();
                break;
        }
    }

    public void handleActionsLeft() {
        switch (orderStatus) {
            case 101:
                showCancelDialog();
                break;
            case 102:
            case 103:
            case 501:
                //删除
                showDelOrderDialog();
                break;
            default:
                //查看物流
                break;
        }
    }

    public void handleActionRight() {
        switch (orderStatus) {
            case 101:
                //TODO TOPAY
                //继续支付
                Intent intent = new Intent(this, PayActivity.class);
                intent.putExtra(PayActivity.REQUEST_TYPE, PayActivity.MALL_REQUEST);
                intent.putExtra("orderId", response.getOrderInfo().getId());
                intent.putExtra("totalFee", String.valueOf(response.getOrderInfo().getActualPrice()));
                startActivity(intent);
                break;
            case 301:
                //确认收货
                showConfirmOrderDialog();
                break;
            case 102:
            case 103:
            case 201:
            case 202:
            case 203:
            case 401:
            case 402:
            case 501:
                //TODO 再次购买
//                Intent intent = new Intent(this, PayActivity.class);
//                intent.putExtra("orderId", response.getOrderInfo().getId());
//                intent.putExtra("totalFee", String.valueOf(response.getOrderInfo().getActualPrice()));
//                startActivity(intent);
                break;
        }
    }

    public void buyAgain() {
        if (response == null) {
            return;
        }
        //mPresenter.addToShoppingCart(info.getId(), productList.get(0).getId(), 1, 1)
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        ToastUtils.show(this, throwable.toString());
    }

    @Override
    public void showOrderDetails(MallOrderDetailsResponse response) {
        this.response = response;
        boolean isPoint = "integral".equals(response.getOrderInfo().getOrderType());
        //积分商品处理
        if (isPoint) {
            tvBalance.setText("积分支付");
            if (response.getOrderGoods().size() > 0) {
                response.getOrderGoods().get(0).setPointGoods(true);
            }
            tvBalanceValue.setText(CommonUtils.getFormatPrice(response.getOrderInfo().getBalancePrice(), isPoint));
            tvPayValue.setText(CommonUtils.getFormatPrice(response.getOrderInfo().getActualPrice(), isPoint));
        } else {
            tvPayValue.setText(CommonUtils.getFormatPrice(getPayValue(), isPoint));
            tvBalanceValue.setText(CommonUtils.getFormatPrice(response.getOrderInfo().getBalancePrice(), isPoint));
        }
        tvGoodsValue.setText(CommonUtils.getFormatPrice(response.getOrderInfo().getGoodsPrice(), isPoint));
        tvFreightValue.setText(CommonUtils.getFormatPrice(response.getOrderInfo().getFreightPrice(), isPoint));
        tvAddressName.setText(response.getOrderInfo().getConsignee());
        tvAddressConsigneenPhone.setText(response.getOrderInfo().getMobile());
        tvAddressTextValue.setText(response.getOrderInfo().getAddress());
        tvOrderState.setText(response.getOrderInfo().getOrderStatusText());
        orderSn.setText("订单编号: " + response.getOrderInfo().getOrderSn());
        orderAddTime.setText("订单时间: " + response.getOrderInfo().getAddTime());
        //设置价格
        ShopCartAdapter mAdapter = new ShopCartAdapter(this, response.getOrderGoods());
        mAdapter.setType(ShopCartAdapter.ORDER_DETAILS_TYPE);
        rcGoodsList.setAdapter(mAdapter);

        String cancelText = "";
        String confirmText = "";
        orderStatus = response.getOrderInfo().getOrderStatus();
        switch (orderStatus) {
            case 101:
                tvPayState.setText("待付款");
                break;
            case 102:
            case 103:
                tvPayState.setText("应付款");
                break;
            default:
                tvPayState.setText("实付款：");
                break;
        }
        switch (response.getOrderInfo().getOrderStatus()) {
            case 101:
                //return "未付款";
                cancelText = "取消订单";
                confirmText = "继续支付";
                break;
            case 102:
                //return "已取消";
            case 103:
                //return "已取消(系统)";
                cancelText = "删除订单";
                confirmText = "再次购买";
                tvOrderConfirmBuy.setVisibility(View.GONE);
                break;
            case 201:
                //return "已付款";
                confirmText = "再次购买";
                tvCancelOrder.setVisibility(View.GONE);
                tvOrderConfirmBuy.setVisibility(View.GONE);
                break;
            case 202:
                //return "订单取消，退款中";
                confirmText = "再次购买";
                tvCancelOrder.setVisibility(View.GONE);
                tvOrderConfirmBuy.setVisibility(View.GONE);
                break;
            case 203:
                //return "已退款";
                //TODO 暂时不使用
                cancelText = "删除订单";
                confirmText = "再次购买";
                tvOrderConfirmBuy.setVisibility(View.GONE);
                break;
            case 301:
                //return "已发货";
                cancelText = "查看物流";
                confirmText = "确认收货";
                tvCancelOrder.setVisibility(View.GONE);
                break;
            case 401:
                //return "已收货";
            case 402:
                //return "已收货(系统)";
            case 501:
                //return "已完成";
                cancelText = "查看物流";
                tvCancelOrder.setVisibility(View.GONE);
                confirmText = "再次购买";
                tvOrderConfirmBuy.setVisibility(View.GONE);
                break;
        }
        tvCancelOrder.setText(cancelText);
        tvOrderConfirmBuy.setText(confirmText);
        if (response.getOrderInfo().getOrderStatus() == 101) {
            long countDownStamp = response.getOrderInfo().getCountDownStamp();
            //剩余23小时59分钟自动关闭
            tvLeftTime.setText("剩余" + Utils.millis2FitTimeSpan(countDownStamp, 4)
                    + "自动关闭");
        } else if (response.getOrderInfo().getOrderStatus() == 301) {
            tvLeftTime.setText("剩余" + response.getOrderInfo().getDay() + "自动确认");
        } else {
            tvLeftTime.setVisibility(View.GONE);
            flOrderState.removeView(tvOrderState);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            flOrderState.addView(tvOrderState, params);
        }
    }

    public double getPayValue() {
        if (response == null) {
            return 0.0f;
        }
        return response.getOrderInfo().getGoodsPrice() +
                response.getOrderInfo().getFreightPrice() - response.getOrderInfo().getBalancePrice();

    }


    /**
     * 实付价格
     */
    public void showCancelDialog() {
        if (cancelOrderDialog == null) {
            cancelOrderDialog = new CancelOrderDialog(this, R.style.CancelDialog);
            cancelOrderDialog.setCancelClick(this);
            cancelOrderDialog.setText(getResources().getStringArray(R.array.mall_order_cancel));
        }
        cancelOrderDialog.show();
    }

    public void showDelOrderDialog() {
        if (delOrderDialog == null) {
            delOrderDialog = new CommonConfirmDialog(this, R.style.CancelDialog);
            delOrderDialog.setOnConfirmListener(new CommonConfirmDialog.OnConfirmListener() {
                @Override
                public void onConfirmClickListener() {
                    mPresenter.commonAction(MallOrderDetailsPresenter.DELETE_ORDER, orderId, "");
                }
            });
            delOrderDialog.setText("确认删除订单吗？", "取消", "确认");
        }
        delOrderDialog.show();
    }


    public void showConfirmOrderDialog() {
        if (confirmDialog == null) {
            confirmDialog = new CommonConfirmDialog(this, R.style.CancelDialog);
            confirmDialog.setOnConfirmListener(new CommonConfirmDialog.OnConfirmListener() {
                @Override
                public void onConfirmClickListener() {
                    mPresenter.commonAction(MallOrderDetailsPresenter.CONFIRM_ORDER, orderId, "");
                }
            });
            confirmDialog.setText("确认收货吗？", "取消", "确认");
        }
        confirmDialog.show();
    }


    /**
     * 取消dialog
     *
     * @param
     */
    @Override
    public void cancelClick(String reason) {
        mPresenter.commonAction(MallOrderDetailsPresenter.CANCEL_ORDER, orderId, reason);
    }

    /**
     * 通用回调
     *
     * @param requestCode
     */
    @Override
    public void commonCallBack(int requestCode) {
        switch (requestCode) {
            case MallOrderDetailsPresenter.DELETE_ORDER:
                ToastUtils.showAlertToast(mContext, "删除订单成功");
                //finish();
                mPresenter.getMallOrderDetails(orderId);
                finish();
                break;
            case MallOrderDetailsPresenter.CANCEL_ORDER:
                ToastUtils.showAlertToast(mContext, "取消订单成功");
                mPresenter.getMallOrderDetails(orderId);
                //finish();
                break;
            case MallOrderDetailsPresenter.CONFIRM_ORDER:
                ToastUtils.showAlertToast(mContext, "确认收货成功");
                mPresenter.getMallOrderDetails(orderId);
                //确认支付回调
                break;

        }
    }

    @Override
    public void addShopCartCallBack(BaseResponse baseResponse, int type) {
        if (baseResponse.getCode() != 100) {
            ToastUtils.show(this, baseResponse.getMsg());
            return;
        }
        for (CartList cartList : response.getOrderGoods()) {
            cartList.setSelected(true);
        }
        Intent intent = new Intent(this, SubmitOrderActivity.class);
        intent.putExtra(SubmitOrderActivity.SUBMIT_FROM_PAGE, FROM_GOODS_DETAILS);
        intent.putParcelableArrayListExtra("cartList", response.getOrderGoods());
        startActivity(intent);
    }
}
