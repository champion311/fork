package com.shosen.max.ui.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.MallAddressBean;
import com.shosen.max.bean.OrderSubmitResponse;
import com.shosen.max.bean.User;
import com.shosen.max.bean.mall.CartList;
import com.shosen.max.others.span.OrangeBgTextSpan;
import com.shosen.max.presenter.SubmitOrderPresenter;
import com.shosen.max.presenter.contract.SubmitOrderContract;
import com.shosen.max.ui.activity.ExChangeSuccessActivity;
import com.shosen.max.ui.activity.PayActivity;
import com.shosen.max.ui.mall.adapter.ShopCartAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.CommonUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.SpanUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.dialog.CommonConfirmDialog;
import com.shosen.max.widget.dialog.LuckPanLoseDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class
SubmitOrderActivity extends BaseActivity implements
        SubmitOrderContract.View, CheckBox.OnCheckedChangeListener {


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
    @BindView(R.id.iv_choose_address)
    ImageView ivChooseAddress;
    @BindView(R.id.rl_choose_address)
    RelativeLayout rlChooseAddress;
    @BindView(R.id.rc_goods_list)
    RecyclerView rcGoodsList;
    @BindView(R.id.cb_user_balance)
    CheckBox cbUserBalance;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.cb_select_all)
    TextView cbSelectAll;
    @BindView(R.id.tv_buy_now)
    TextView tvBuyNow;
    @BindView(R.id.tv_user_balance)
    TextView tvUserBalance;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_cellphone)
    TextView tvCellphone;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.cl_address)
    ConstraintLayout clAddress;
    @BindView(R.id.tv_to_buy_cash)
    TextView tvToBuyCash;
    @BindView(R.id.rl_balance)
    RelativeLayout rlBalance;
    @BindView(R.id.tv_total_point)
    TextView tvTotalPoint;
    @BindView(R.id.rl_goods_order_bottom_left)
    RelativeLayout rlGoodsOrderBottomLeft;
    @BindView(R.id.tv_freight_fee)
    TextView tvFreightFee;
    private ArrayList<CartList> mData;
    //cartList

    private ShopCartAdapter mAdapter;

    private SubmitOrderPresenter mPresenter;

    /**
     * 地址Id
     */
    private String addressId;

    public static final int FROM_SHOP_CART = 0x10;//从购物车跳转

    public static final int FROM_GOODS_DETAILS = 0x10 + 1;//从商品详情跳转

    public static final String SUBMIT_FROM_PAGE = "submit_from_page";

    private int fromPage = FROM_SHOP_CART;

    private boolean isPointGoods;

    private MallAddressBean defaultAddress = null;

    private MallAddressBean selectedAddress = null;

    private ArrayList<Integer> unSelectedIds;

    private String goodsId;

    private String productId;

    private int number;

//     intent.putExtra("goodsId",goodsId);
//     intent.putExtra("productId",productId);
//     intent.putExtra("number",number);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new SubmitOrderPresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.home_page_color));
        initTopHeader();
        ivHeadTitle.setText("提交订单");
        rcGoodsList.setLayoutManager(new LinearLayoutManager(this));
        rcGoodsList.setHasFixedSize(true);
        if (getIntent() != null) {
            fromPage = getIntent().getIntExtra(SUBMIT_FROM_PAGE, FROM_SHOP_CART);
            mData = getIntent().getParcelableArrayListExtra("cartList");
            unSelectedIds = getIntent().getIntegerArrayListExtra("unSelectedIds");
            mAdapter = new ShopCartAdapter(this, mData);
            mAdapter.setType(ShopCartAdapter.SUBMIT_ORDER_TYPE);
            //从商品详情跳转过来
            if (fromPage == FROM_GOODS_DETAILS) {
                goodsId = getIntent().getStringExtra("goodsId");
                productId = getIntent().getStringExtra("productId");
                number = getIntent().getIntExtra("number", 1);
            }
        }

        if (mAdapter != null) {
            rcGoodsList.setAdapter(mAdapter);
        }
        initViews();
    }

    public void initViews() {
        if (mData == null || mData.size() == 0 || !LoginUtils.isLogin) {
            return;
        }
        //是否是积分商品
        isPointGoods = mData.get(0).isPointGoods();
        if (!isPointGoods) {
            //非积分商品
            boolean isEnabled = LoginUtils.getUser().getMoney() > 0.0f;
            tvUserBalance.setEnabled(isEnabled);
            tvUserBalance.setText("我的余额:" +
                    CommonUtils.getFormatPrice(LoginUtils.getUser().getMoney(), isPointGoods));
            cbUserBalance.setEnabled(isEnabled);
            tvUserBalance.setEnabled(isEnabled);
            if (isEnabled) {
                cbUserBalance.setText("使用:" + CommonUtils.getFormatPrice(getPayMoney(), isPointGoods));
                cbUserBalance.setOnCheckedChangeListener(this);
            } else {
                cbUserBalance.setText("无可用");
                cbUserBalance.setCompoundDrawables(null, null, null, null);
                cbUserBalance.setChecked(false);
                cbUserBalance.setEnabled(false);
            }
            double cash = (double) mAdapter.getTotalPrice();
            if (cbUserBalance.isChecked()) {
                cash -= LoginUtils.getUser().getMoney();
                if (cash < 0.0f) {
                    cash = 0.0f;
                }
            }
            tvToBuyCash.setText(CommonUtils.getFormatPrice(cash, isPointGoods));
            tvTotalPrice.setText(CommonUtils.getFormatPrice(mAdapter.getTotalPrice(), isPointGoods));
            rlBalance.setVisibility(View.VISIBLE);

            //tvTotalPoint.setText(SpanUtils.makeTotalPriceSpan(this, "实付款：", CommonUtils.getFormatPrice(mAdapter.getTotalPrice(), isPointGoods)));
            //没有运费的时候
        } else if (mData.get(0).isPointGoods()) {
            //积分商品
            rlBalance.setVisibility(View.GONE);
            tvTotalPoint.setVisibility(View.GONE);
            //tvTotalPoint.setText(CommonUtils.getFormatPrice(mAdapter.getTotalPrice(), isPointGoods));
            tvTotalPrice.setText(CommonUtils.getFormatPrice(mAdapter.getTotalPrice(), isPointGoods));
            tvBuyNow.setText("立即兑换");
            rlGoodsOrderBottomLeft.setVisibility(View.GONE);
        }
        //运费
        tvFreightFee.setText("¥ 0");
        //总计
        tvTotalPoint.setVisibility(View.GONE);

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


    @Override
    protected int getContentViewID() {
        return R.layout.activity_goods_order;
    }

    @OnClick({R.id.iv_back, R.id.rl_choose_address, R.id.cl_address, R.id.tv_buy_now})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_choose_address:
                intent = new Intent(this, AddressListActivity.class);
                startActivityForResult(intent, GET_ADDRESS_REQUEST);
                selectedAddress = null;
                break;
            case R.id.cl_address:
                intent = new Intent(this, AddressListActivity.class);
                startActivityForResult(intent, GET_ADDRESS_REQUEST);
                selectedAddress = null;
                break;
            case R.id.tv_buy_now:
                if (!isPointGoods) {
                    //不是积分商品
                    if (BLEND.equals(getOrderType()) || BALANCE.equals(getOrderType())) {
                        showConfirmUseBalanceDialog();
                    } else if (ACTUAL.equals(getOrderType())) {
                        //现金支付
                        submitOrder();
                    }
                } else {
                    //积分商品
                    showConfirmExPointDialog();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 获取地址回调
     *
     * @param mData
     */
    @Override
    public void addressBack(List<MallAddressBean> mData) {
        if (mData == null) {
            return;
        }
        if (selectedAddress != null) {
            return;
        }
        for (MallAddressBean bean : mData) {
            if (bean.isDefault()) {
                defaultAddress = bean;
                setAddress(bean);
                break;
            }
        }
        if (TextUtils.isEmpty(addressId)) {
            rlChooseAddress.setVisibility(View.VISIBLE);
            clAddress.setVisibility(View.GONE);
        }
    }

    /**
     * 处理地址返回
     *
     * @param bean
     */
    public void setAddress(MallAddressBean bean) {
        addressId = bean.getId();
        rlChooseAddress.setVisibility(View.GONE);
        clAddress.setVisibility(View.VISIBLE);
        tvName.setText(bean.getName());
        tvCellphone.setText(bean.getMobile());
        if (bean.isDefault()) {
            tvAddress.setText(getDefaultText("默认",
                    bean.getProvince() + bean.getCity() + bean.getArea() + bean.getAddress()));
        } else {
            tvAddress.setText(bean.getProvince() + bean.getCity() + bean.getArea() + bean.getAddress());
        }
    }


    public SpannableString getDefaultText(String text1, String text2) {
        text1 = " " + text1 + " ";
        String text = text1 + " " + text2;
        SpannableString spannableString = new SpannableString(text);
        OrangeBgTextSpan orangeBgTextSpan = new OrangeBgTextSpan(
                ContextCompat.getColor(mContext, R.color.default_address_bg_color), mContext);
        spannableString.setSpan(orangeBgTextSpan, 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public void showErrorMessage(Throwable throwable) {

    }

    @Override
    public void submitOrderBack(
            BaseResponse<OrderSubmitResponse> response) {
        if (response.getCode() == 100) {
            if (BLEND.equals(getOrderType()) || ACTUAL.equals(getOrderType())) {
                //使用现金支付和混合支付
                ToastUtils.show(this, "提交订单成功");
                Intent intent = new Intent(this, PayActivity.class);
                intent.putExtra(PayActivity.REQUEST_TYPE, PayActivity.MALL_REQUEST);
                intent.putExtra("orderId", response.getData().getOrderId());
                intent.putExtra("totalFee", String.valueOf(getPayCash()));
                startActivity(intent);
            } else if (INTERGRAL.equals(getOrderType())) {
                Intent intent = new Intent(this, ExChangeSuccessActivity.class);
                startActivity(intent);

            } else if (BALANCE.equals(getOrderType())) {
                //TODO 只使用余额的时候不需要跳转支付
                ActivityUtils.finishActivity(SubmitOrderActivity.class);
                ActivityUtils.finishActivity(GoodsDetailActivity.class);
                ActivityUtils.finishActivity(ShoppingCartActivity.class);
                ActivityUtils.startActivity(MallOrderListActivity.class);
            }
        } else {
            //提交订单失败
            LuckPanLoseDialog loseDialog = new LuckPanLoseDialog(this, R.style.CancelDialog);
            loseDialog.setOnClick(new LuckPanLoseDialog.onConfirmClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            loseDialog.setText(response.getMsg(), "");
            loseDialog.show();
        }
    }

    @Deprecated
    @Override
    public void cartCheckBack() {

    }

    @Override
    public void showBalanceMoney() {

    }

    /**
     * 更新余额数据
     *
     * @param mUser
     */
    @Override
    public void updateUser(User mUser) {
        if (mUser == null) {
            return;
        }
        tvUserBalance.setText("我的余额：" + CommonUtils.getFormatPrice(mUser.getMoney(), false));
        //
        if (LoginUtils.isLogin) {
            User user = LoginUtils.getUser();
            user.setMoney(mUser.getMoney());
            LoginUtils.putUser(user);
        }
    }

    private CommonConfirmDialog confirmDialog;

    private CommonConfirmDialog confirmUseBalanceDialog;

    public void showConfirmExPointDialog() {
        if (confirmDialog == null) {
            confirmDialog = new CommonConfirmDialog(this, R.style.CancelDialog);
            confirmDialog.setText(getString(R.string.point_confirm_title),
                    getString(R.string.point_confirm_cancel), getString(R.string.point_confirm_confirm));
        }
        confirmDialog.setOnConfirmListener(new CommonConfirmDialog.OnConfirmListener() {
            @Override
            public void onConfirmClickListener() {
                //TODO 确认兑换
                submitOrder();
            }
        });
        confirmDialog.show();
    }

    public void showConfirmUseBalanceDialog() {
        if (confirmUseBalanceDialog == null) {
            confirmUseBalanceDialog = new CommonConfirmDialog(this, R.style.CancelDialog);
            confirmUseBalanceDialog.setText(getString(R.string.balance_confirm_title),
                    getString(R.string.balance_confirm_cancel), getString(R.string.balance_confirm_confirm));
        }
        confirmUseBalanceDialog.setOnConfirmListener(new CommonConfirmDialog.OnConfirmListener() {
            @Override
            public void onConfirmClickListener() {
                //TODO 确认使用余额支付
                submitOrder();
            }
        });
        confirmUseBalanceDialog.show();
    }

    /**
     * 提交订单
     */
    public void submitOrder() {
        if (!LoginUtils.isLogin) {
            return;
        }
        if (TextUtils.isEmpty(addressId)) {
            ToastUtils.show(this, getString(R.string.no_address_dialog_alert));
            return;
        }
        if (mAdapter == null) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("userPhone", LoginUtils.getUser().getPhone());
        map.put("addressId", addressId);
        map.put("cartId", 0);
        map.put("couponId", "0");
        map.put("message", "0");
        map.put("grouponRulesId", "0");
        map.put("grouponLinkId", "0");
        //积分
        map.put("integralPrice", getIntegralPrice());
        map.put("money", (int) (getPayMoney()));
        map.put("actualPrice", String.valueOf(getPayCash()));
        map.put("orderType", getOrderType());

        //从购物车来
        if (fromPage == FROM_SHOP_CART) {
            map.put("isBuy", 0);
            if (unSelectedIds != null && unSelectedIds.size() > 0) {
                mPresenter.subMitOrder(map, mAdapter.getSelectedIds(),
                        unSelectedIds.toArray(new Integer[unSelectedIds.size()]));
            } else {
                mPresenter.subMitOrder(map, mAdapter.getSelectedIds());
            }
        } else if (fromPage == FROM_GOODS_DETAILS) {
            //从商品详情中过来
            map.put("isBuy", 1);
            mPresenter.subMitOrder(map, mAdapter.getSelectedIds(), goodsId, productId, number);
            //mPresenter.fastBuy(goodsId, productId, number);
        }
    }

    /**
     * 消耗的积分额
     *
     * @return
     */
    public double getIntegralPrice() {
        if (!LoginUtils.isLogin) {
            return 0.0f;
        }
        if (!isPointGoods) {
            return 0.0f;
        }
        if (mData.get(0).getPrice() > Integer.valueOf(LoginUtils.getUser().getxProperty())) {
            return 0.0f;
        }
        return mData.get(0).getPrice();
    }


    /**
     * 获取花费的余额
     *
     * @return
     */
    public double getPayMoney() {
        if (!LoginUtils.isLogin) {
            return 0.0f;
        }
        if (INTERGRAL.equals(isPointGoods)) {
            return 0.0f;
        }
        if (mAdapter != null && cbUserBalance.isChecked()) {
            if (LoginUtils.getUser().getMoney() >= mAdapter.getTotalPrice()) {
                return mAdapter.getTotalPrice();
            } else {
                return (double) LoginUtils.getUser().getMoney();
            }
        }
        //不使用余额
        return 0.0f;
    }


    /**
     * 获取花费现金
     *
     * @return
     */
    public double getPayCash() {
        if (!LoginUtils.isLogin) {
            return 0;
        }
        if (isPointGoods) {
            return 0.0f;
        }
        if (mAdapter != null) {
            return mAdapter.getTotalPrice() - getPayMoney();
        }
        return 0;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        double cash = mAdapter.getTotalPrice();
        if (isChecked) {
            cash -= LoginUtils.getUser().getMoney();
            if (cash < 0.0f) {
                cash = 0.0f;
            }
        }
        //非积分商品有效
        tvToBuyCash.setText(CommonUtils.getFormatPrice(cash, isPointGoods));
    }

    /**
     * 处理地址问题
     */
    @Override
    protected void onResume() {
        mPresenter.getAddressList();
        super.onResume();
    }

    public String getOrderType() {
        if (isPointGoods) {
            return INTERGRAL;
        }
        if (getPayMoney() > 0 && getPayCash() > 0) {
            return BLEND;
        } else if (getPayMoney() > 0) {
            return BALANCE;
        } else if (getPayCash() > 0) {
            return ACTUAL;
        }
        return BLEND;
    }


    //积分
    public static final String INTERGRAL = "integral";
    //余额
    public static final String BALANCE = "balance";
    //现金
    public static final String ACTUAL = "actual";
    //余额+现金
    public static final String BLEND = "blend";

    public static final int GET_ADDRESS_REQUEST = 101;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case GET_ADDRESS_REQUEST:
                MallAddressBean address = (MallAddressBean) data.getParcelableExtra("address");
                selectedAddress = address;
                setAddress(address);
                break;
        }
    }
}

