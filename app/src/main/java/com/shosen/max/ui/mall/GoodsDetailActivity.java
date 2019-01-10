package com.shosen.max.ui.mall;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bin.david.form.data.form.IForm;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.GoodsDetailBean;
import com.shosen.max.bean.mall.CartList;
import com.shosen.max.presenter.GoodsDetailPresenter;
import com.shosen.max.presenter.contract.MallHomeContract;
import com.shosen.max.ui.activity.LoginActivity;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.CommonUtils;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.dialog.CommonConfirmDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.shosen.max.ui.mall.SubmitOrderActivity.FROM_GOODS_DETAILS;

public class GoodsDetailActivity extends BaseActivity implements MallHomeContract.GoodsDetailsView {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_head_title)
    TextView ivHeadTitle;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.iv_goods_image)
    ImageView ivGoodsImage;
    @BindView(R.id.tv_commodity_text)
    TextView tvCommodityText;
    @BindView(R.id.tv_commodity_price)
    TextView tvCommodityPrice;
    @BindView(R.id.iv_commondity_long_image)
    ImageView ivCommondityLongImage;
    @BindView(R.id.tv_mall_text)
    TextView tvMallText;
    @BindView(R.id.rl_mall)
    RelativeLayout rlMall;
    @BindView(R.id.tv_shop_cart)
    TextView tvShopCart;
    @BindView(R.id.iv_shop_cart)
    ImageView ivShopCart;
    @BindView(R.id.tv_shop_cart_count)
    TextView tvShopCartCount;
    @BindView(R.id.rl_shop_cart)
    RelativeLayout rlShopCart;
    @BindView(R.id.tv_add_shopping)
    TextView tvAddShopping;
    @BindView(R.id.tv_buy_now)
    TextView tvBuyNow;
    @BindView(R.id.rl_goods_bottom_details)
    LinearLayout rlGoodsBottomDetails;
    @BindView(R.id.tv_exchange_now)
    TextView tvExchangeNow;
    private String id;

    private GoodsDetailPresenter mPresenter;

    private GoodsDetailBean.Info info;

    private List<GoodsDetailBean.ProductList> productList;

    private boolean isPointGoods;

    private String goodsId;

    private String productId;

    private int number;

//        map.put("goodsId", goodsId);
//        map.put("productId", productId);
//        map.put("number", number)


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new GoodsDetailPresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.home_page_color));
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            isPointGoods = getIntent().getBooleanExtra("isPointGoods", false);
            //if ("积分兑换专区".equals(name)) {
            if (!TextUtils.isEmpty(id)) {
                mPresenter.loadDetailsData(id);
                mPresenter.getCartCount();
            }

        }
        if (LoginUtils.isLogin) {
            tvShopCartCount.setVisibility(View.VISIBLE);
        } else {
            tvShopCartCount.setVisibility(View.GONE);
        }

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_goods_details;
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        ToastUtils.show(this, throwable.toString());
    }

    @Override
    public void showData(GoodsDetailBean mData) {
        info = mData.getInfo();
        productList = mData.getProductList();
        tvCommodityText.setText(mData.getInfo().getName());
        tvCommodityPrice.setText(CommonUtils.getFormatPrice(mData.getInfo().getRetailPrice(), isPointGoods));
        RequestOptions requestOptions = new RequestOptions()
                .fitCenter()
                .error(R.drawable.default_long_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        GlideUtils.loadImage(this, mData.getInfo().getPicUrl(), requestOptions,
                GlideUtils.defaultTransOptions, ivGoodsImage);
        if (isPointGoods) {
            rlGoodsBottomDetails.setVisibility(View.GONE);
            tvExchangeNow.setVisibility(View.VISIBLE);
        } else {
            rlGoodsBottomDetails.setVisibility(View.VISIBLE);
            tvExchangeNow.setVisibility(View.GONE);
        }
        goodsId = info.getId();
        productId = productList.get(0).getId();
        number = 1;
        GlideUtils.loadImage(this, mData.getInfo().getDetail(), requestOptions,
                GlideUtils.defaultTransOptions, ivCommondityLongImage);
    }

    /**
     * @param baseResponse
     * @param type         0，直接加入购物车 1，跳转到生成订单界面
     */
    @Override
    public void addShopCartCallBack(BaseResponse baseResponse, int type) {
        if (baseResponse.getCode() != 100) {
            ToastUtils.show(this, baseResponse.getMsg());
            return;
        }
        if (type == 0) {
            ToastUtils.show(this, "添加购物车成功");
            mPresenter.getCartCount();
        } else if (type == 1) {
            if (info == null) {
                return;
            }
            Intent intent = new Intent(this, SubmitOrderActivity.class);
            intent.putExtra(SubmitOrderActivity.SUBMIT_FROM_PAGE, FROM_GOODS_DETAILS);
            ArrayList<CartList> mData = new ArrayList<>();
            mData.add(info.convertToCartList(productList.get(0).getId(), isPointGoods));
            intent.putParcelableArrayListExtra("cartList", mData);
            startActivity(intent);
        }
    }

    @Override
    public void payNowCallBack(String message) {
        ToastUtils.show(this, message);
    }

    /**
     * 获取购物车数量
     *
     * @param goodsCount
     */
    @Override
    public void getGoodsCount(Integer goodsCount) {
        tvShopCartCount.setText(String.valueOf(goodsCount));
    }

    @OnClick({R.id.iv_back, R.id.tv_add_shopping,
            R.id.tv_buy_now, R.id.rl_mall, R.id.rl_shop_cart, R.id.tv_exchange_now})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_shopping:
                /**
                 * 添加到购物车
                 */
                if (info == null || productList == null) {
                    return;
                }
                if (Integer.valueOf(productList.get(0).getNumber()) <= 0) {
                    ToastUtils.show(this, "库存不足");
                }
                if (!LoginUtils.isLogin) {
                    ActivityUtils.startActivity(LoginActivity.class);
                    return;
                }
                mPresenter.addToShoppingCart(info.getId(), productList.get(0).getId(), 1, 0);
                break;
            case R.id.tv_buy_now:
                /**
                 * 立即购买
                 */
                if (info == null || productList == null) {
                    return;
                }
                if (Integer.valueOf(productList.get(0).getNumber()) <= 0) {
                    ToastUtils.show(this, "库存不足");
                }
                if (!LoginUtils.isLogin) {
                    ActivityUtils.startActivity(LoginActivity.class);
                    return;
                }
                toSubmitOrder();
                //mPresenter.addToShoppingCart(info.getId(), productList.get(0).getId(), 1, 1);
                break;
            case R.id.rl_mall:
                finish();
                break;
            case R.id.rl_shop_cart:
                if (!LoginUtils.isLogin) {
                    ActivityUtils.startActivity(LoginActivity.class);
                    return;
                }
                ActivityUtils.startActivity(ShoppingCartActivity.class);
                break;
            //立即兑换
            case R.id.tv_exchange_now:
                if (!LoginUtils.isLogin) {
                    ActivityUtils.startActivity(LoginActivity.class);
                    return;
                }
                if (Integer.valueOf(productList.get(0).getNumber()) <= 0) {
                    ToastUtils.show(this, "库存不足");
                }
                //积分兑换
                if (Integer.valueOf(LoginUtils.getUser().getxProperty()) <
                        Double.valueOf(info.getRetailPrice())) {
                    showPointDialog();
                } else {
                    toSubmitOrder();
                    //mPresenter.addToShoppingCart(info.getId(), productList.get(0).getId(), 1, 1);
                }
                break;
            default:
                break;
        }
    }

    public void showPointDialog() {
        CommonConfirmDialog confirmDialog = new CommonConfirmDialog(this, R.style.CancelDialog);
        confirmDialog.showSingle("当前积分不足，暂无法兑换！", "确认");
        confirmDialog.setOnConfirmListener(new CommonConfirmDialog.OnConfirmListener() {
            @Override
            public void onConfirmClickListener() {
                //TODO 确认兑换

            }
        });
        confirmDialog.show();
        return;
    }

    //
//    private String goodsId;
//
//    private String productId;
//
//    private int number;
    public void toSubmitOrder() {
        Intent intent = new Intent(this, SubmitOrderActivity.class);
        intent.putExtra(SubmitOrderActivity.SUBMIT_FROM_PAGE, FROM_GOODS_DETAILS);
        ArrayList<CartList> mData = new ArrayList<>();
        mData.add(info.convertToCartList(productList.get(0).getId(), isPointGoods));
        intent.putParcelableArrayListExtra("cartList", mData);
        intent.putExtra("goodsId", goodsId);
        intent.putExtra("productId", productId);
        intent.putExtra("number", number);
        startActivity(intent);
    }
}
