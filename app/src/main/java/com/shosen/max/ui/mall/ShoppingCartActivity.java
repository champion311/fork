package com.shosen.max.ui.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.mall.CartList;
import com.shosen.max.bean.ShoppingCartBean;
import com.shosen.max.presenter.ShoppingCartPresenter;
import com.shosen.max.presenter.contract.ShoppingCartContract;
import com.shosen.max.ui.mall.adapter.ShopCartAdapter;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.widget.dialog.CommonConfirmDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.shosen.max.ui.mall.SubmitOrderActivity.FROM_SHOP_CART;

public class ShoppingCartActivity extends BaseActivity
        implements ShoppingCartContract.View,
        ShopCartAdapter.NumberClick, CheckBox.OnCheckedChangeListener
        , CommonConfirmDialog.OnConfirmListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_head_title)
    TextView ivHeadTitle;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.tv_no_goods_alert)
    TextView tvNoGoodsAlert;
    @BindView(R.id.rl_nogoods_wrapper)
    RelativeLayout rlNogoodsWrapper;
    @BindView(R.id.rc_goods_list)
    RecyclerView rcGoodsList;
    @BindView(R.id.cb_select_all)
    TextView cbSelectAll;
    @BindView(R.id.tv_buy_now)
    TextView tvBuyNow;
    @BindView(R.id.tv_property)
    TextView tvProperty;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.rl_shop_cart_bottom)
    RelativeLayout rlShopCartBottom;
    @BindView(R.id.tv_back_mall)
    TextView tvBackMall;

    private boolean isEditMode = false;

    private ShoppingCartPresenter mPresenter;

    private ShopCartAdapter mAdapter;

    private ArrayList<CartList> mData;

    private CommonConfirmDialog confirmDialog;

    private List<CartList> mDataCopy;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new ShoppingCartPresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.home_page_color));
        rightText.setVisibility(View.VISIBLE);
        rightText.setText("编辑");
        rcGoodsList.setLayoutManager(new LinearLayoutManager(this));
        ivHeadTitle.setText("购物车");
        mPresenter.getShoppingCartData();
        mData = new ArrayList<>();
        mAdapter = new ShopCartAdapter(this, mData);
        mAdapter.setClick(this);
        rcGoodsList.setAdapter(mAdapter);
        if (LoginUtils.isLogin) {
            //积分
            tvProperty.setText(LoginUtils.getUser().getxProperty());
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    public void showErrorMessage(Throwable throwable) {

    }

    @Override
    public void showCartIndex(ShoppingCartBean bean) {
        Log.d("tag", bean.toString());
        if (bean == null) {
            return;
        }
        for (CartList cartList : bean.getCartList()) {
            cartList.setCount(cartList.getNumber());
            //cartList.setSelected(false);
        }
        mData.clear();
        mData.addAll(bean.getCartList());
        mDataCopy = new ArrayList<>();
        mDataCopy.addAll(bean.getCartList());
        mAdapter.notifyDataSetChanged();
        updateView();
    }


    @Override
    public void addClick(View view, int position, CartList bean) {
        int count = bean.getCount();
        mData.get(position).setCount(count + 1);
        mAdapter.notifyItemChanged(position, new Integer(count + 1));
        updateTotalPrice();
    }

    @Override
    public void minusClick(View view, int position, CartList bean) {
        int count = bean.getCount();
        if (count <= 1) {
            return;
        }
        mData.get(position).setCount(count - 1);
        mAdapter.notifyItemChanged(position, new Integer(count - 1));
        updateTotalPrice();
    }

    @Override
    public void markClick(View view, int position) {
        Log.d("ShoppingCart", "position=" + position);
        mData.get(position).setSelected(!mData.get(position).isSelected());
        mAdapter.notifyItemChanged(position, new Boolean(!mData.get(position).isSelected()));
        updateTotalPrice();

    }

    /**
     * 更新总价
     */
    public void updateTotalPrice() {
        if (mAdapter == null) {
            return;
        }
        if (mAdapter.getSelectedGoodsCount() != 0) {
            tvTotalPrice.setText(mAdapter.getTotalPriceStr());
            tvTotalPrice.setVisibility(View.VISIBLE);
            tvBuyNow.setEnabled(true);
            cbSelectAll.setText("所选（" + mAdapter.getSelectedGoodsCount() + "）");
            cbSelectAll.setSelected(true);
        } else {
            tvTotalPrice.setVisibility(View.GONE);
            tvBuyNow.setEnabled(false);
            cbSelectAll.setText("全选");
            cbSelectAll.setSelected(false);
        }
    }

    private boolean isToBuy = false;

    @OnClick({R.id.iv_back, R.id.right_text,
            R.id.tv_buy_now, R.id.tv_total_price, R.id.tv_back_mall, R.id.cb_select_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                updateCertCount();
                finish();
                break;
            case R.id.right_text:
                isEditMode = !isEditMode;
                rightText.setText(isEditMode ? "完成" : " 编辑");
                tvBuyNow.setText(isEditMode ? "删除所选" : "去结算");
                //if (isEditMode) {
                mAdapter.setAllState(false);
                updateTotalPrice();
                //}
                break;
            case R.id.tv_total_price:
                //TODO弹出Dialog
                break;
            case R.id.tv_buy_now:
                if (tvBuyNow.isEnabled()) {
                    if (!isEditMode) {
                        //TODO
                        updateCertCount();
                        isToBuy = true;
                        Intent intent = new Intent(this, SubmitOrderActivity.class);
                        intent.putExtra(SubmitOrderActivity.SUBMIT_FROM_PAGE, FROM_SHOP_CART);
                        //TODO 删选集合
                        intent.putParcelableArrayListExtra("cartList", mAdapter.getSelectedItems());
                        intent.putIntegerArrayListExtra("unSelectedIds",
                                mAdapter.getUnSelectedIdsArray());
                        startActivity(intent);
                    } else {
                        showDelConfirmDialog();
                    }
                }
                break;
            case R.id.tv_back_mall:
                finish();
            case R.id.cb_select_all:
                if (mData == null) {
                    return;
                }
                for (CartList bean : mData) {
                    bean.setSelected(!cbSelectAll.isSelected());
                }
                mAdapter.notifyDataSetChanged();
                updateTotalPrice();
                break;
        }
    }

    /**
     * 全选按钮监听
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mData == null) {
            return;
        }
        for (CartList bean : mData) {
            bean.setSelected(isChecked);
        }
        mAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    public void showDelConfirmDialog() {
        if (confirmDialog == null) {
            confirmDialog = new CommonConfirmDialog(this);
            confirmDialog.setText(getString(R.string.shop_cart_title), getString(R.string.shop_cart_cancel), getString(R.string.shop_cart_confirm));
            confirmDialog.setOnConfirmListener(this);
        }
        confirmDialog.show();
    }


    /**
     * 删除条目回调
     */
    @Override
    public void delCartItemBack() {
        if (mAdapter != null) {
            mAdapter.delSelectedItem();
        }
        updateTotalPrice();
        updateView();
    }

    /**
     * 更新数量
     * TODO 在离开页面的时候进行比较再提交
     */
    public void updateCertCount() {
//        if (mData.size() == 0) {
//            //数量无变化直接跳转
//            updateCartBack();
//            return;
//        }
        for (CartList cartList : mData) {
            if (cartList.getCount() != cartList.getNumber()) {
                mPresenter.cartUpdate(cartList.getId(),
                        cartList.getGoodsId(), cartList.getProductId(),
                        cartList.getCount());
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //离开页面时更新数量
            updateCertCount();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 更新购物车数量监听
     */
    @Override
    public void updateCartBack() {
        if (!isToBuy) {
            return;
        }

    }

    /**
     * 删除所选监听
     */
    @Override
    public void onConfirmClickListener() {
        if (mAdapter != null) {
            mPresenter.delCartItem(mAdapter.getSelectedIds());
        }
    }

    /**
     * 更新view
     */
    public void updateView() {
        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            rlNogoodsWrapper.setVisibility(View.VISIBLE);
            rlShopCartBottom.setVisibility(View.GONE);
            rightText.setVisibility(View.GONE);
        } else {
            rlNogoodsWrapper.setVisibility(View.GONE);
            rlShopCartBottom.setVisibility(View.VISIBLE);
            rightText.setVisibility(View.VISIBLE);
        }
    }
}
