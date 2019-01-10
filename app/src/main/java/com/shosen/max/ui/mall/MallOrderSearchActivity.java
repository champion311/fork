package com.shosen.max.ui.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.mall.GoodsOrderList;
import com.shosen.max.others.span.SpaceItemDecoration;
import com.shosen.max.presenter.GoodsSearchPresenter;
import com.shosen.max.presenter.MallOrderDetailsPresenter;
import com.shosen.max.presenter.contract.GoodsListSearchContract;
import com.shosen.max.ui.activity.PayActivity;
import com.shosen.max.ui.mall.adapter.GoodsOrderListAdapter;
import com.shosen.max.utils.ClipboardUtils;
import com.shosen.max.utils.CommonUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.dialog.CancelButtonClick;
import com.shosen.max.widget.dialog.CancelOrderDialog;
import com.shosen.max.widget.dialog.CommonConfirmDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MallOrderSearchActivity extends BaseActivity
        implements TextWatcher, EditText.OnEditorActionListener,
        GoodsListSearchContract.View, GoodsOrderListAdapter.OnItemClickListener {


    @BindView(R.id.ed_search_box)
    EditText edSearchBox;
    @BindView(R.id.tv_clear_content)
    ImageView tvClearContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.rc_search_list)
    RecyclerView rcSearchList;
    @BindView(R.id.iv_no_goods_order)
    ImageView ivNoGoodsOrder;
    @BindView(R.id.ll_top)
    LinearLayout llTop;

    private GoodsSearchPresenter mPresenter;

    private List<GoodsOrderList> mData = new ArrayList<>();

    private GoodsOrderListAdapter mAdapter;

    private String showType;

    private String goodsName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new GoodsSearchPresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    public void initTopllHeader() {

        if (llTop != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) llTop.getLayoutParams();
            layoutParams.topMargin =
                    StatusBarUtil.getStatusBarHeight(this);
            llTop.setLayoutParams(layoutParams);
        }
    }


    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.home_page_color));
        initTopllHeader();
        rcSearchList.setLayoutManager(new LinearLayoutManager(this));
        edSearchBox.addTextChangedListener(this);
        edSearchBox.setOnEditorActionListener(this);

        mAdapter = new GoodsOrderListAdapter(this, mData);
        mAdapter.setMclicK(this);
        rcSearchList.setLayoutManager(new LinearLayoutManager(this));
        rcSearchList.setAdapter(mAdapter);
        rcSearchList.addItemDecoration(new SpaceItemDecoration
                (15, 15, 15, 0));

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_mall_order_search;
    }


    @OnClick({R.id.tv_clear_content, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear_content:
                edSearchBox.setText("");
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String text = edSearchBox.getText().toString().trim();
            goodsName = text;
            mPresenter.searchGoodsList(0, text);
            ClipboardUtils.hideSoftKeyBoard(this, edSearchBox);
        }
        return false;
    }

    @Override
    public void showErrorMessage(Throwable throwable) {

    }

    @Override
    public void showData(List<GoodsOrderList> mData) {
        if (mData == null || mData.size() == 0) {
            ivNoGoodsOrder.setVisibility(View.VISIBLE);
            return;
        }
        ivNoGoodsOrder.setVisibility(View.GONE);
        this.mData.clear();
        this.mData.addAll(mData);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void commonCallBack(int requestCode) {
        switch (requestCode) {
            case MallOrderDetailsPresenter.DELETE_ORDER:
                ToastUtils.showAlertToast(this, "删除订单成功");
                break;
            case MallOrderDetailsPresenter.CANCEL_ORDER:
                ToastUtils.showAlertToast(this, "取消订单成功");
                break;
            case MallOrderDetailsPresenter.CONFIRM_ORDER:
                ToastUtils.showAlertToast(this, "确认收货成功");
                //确认支付回调
                break;
            default:
                break;
        }
        mPresenter.searchGoodsList(0, goodsName);
    }


    @Override
    public void delClick(View view, int position, GoodsOrderList bean) {
        showDelOrderDialog(bean.getId());
    }

    @Override
    public void cancelClick(View view, int position, GoodsOrderList bean) {
        if (bean.getOrderStatus() == 101) {
            showCancelDialog(bean.getId());
        }
    }

    @Override
    public void confirmClick(View view, int position, GoodsOrderList bean) {
        if (bean.getOrderStatus() == 301) {
            //确认收货
            showConfirmOrderDialog(bean.getId());
        } else if (bean.getOrderStatus() == 101) {
            //TODO 继续支付
            Intent intent = new Intent(this, PayActivity.class);
            intent.putExtra("orderId", bean.getId());
            intent.putExtra("totalFee", String.valueOf(bean.getActualPrice()));
            intent.putExtra(PayActivity.REQUEST_TYPE, PayActivity.MALL_REQUEST);
            startActivity(intent);
        }
    }

    @Override
    public void allClick(View view, int position, GoodsOrderList bean) {
        Intent intent = new Intent(this, MallOrderDetailActivity.class);
        intent.putExtra("orderId", bean.getId());
        startActivity(intent);
    }


    private CancelOrderDialog cancelOrderDialog;

    private CommonConfirmDialog delOrderDialog;

    private CommonConfirmDialog confirmDialog;

    public void showCancelDialog(String orderId) {
        if (cancelOrderDialog == null) {
            cancelOrderDialog = new CancelOrderDialog(this, R.style.CancelDialog);
            cancelOrderDialog.setText(getResources().getStringArray(R.array.mall_order_cancel));
        }
        cancelOrderDialog.setCancelClick(new CancelButtonClick() {
            @Override
            public void cancelClick(String reason) {
                mPresenter.commonAction(MallOrderDetailsPresenter.CANCEL_ORDER, orderId, reason);
            }
        });
        cancelOrderDialog.show();
    }

    public void showDelOrderDialog(String orderId) {
        if (delOrderDialog == null) {
            delOrderDialog = new CommonConfirmDialog(this, R.style.CancelDialog);
            delOrderDialog.setText("确认删除订单吗？", "取消", "确认");
        }
        delOrderDialog.setOnConfirmListener(new CommonConfirmDialog.OnConfirmListener() {
            @Override
            public void onConfirmClickListener() {
                mPresenter.commonAction(MallOrderDetailsPresenter.DELETE_ORDER, orderId, "");
            }
        });
        delOrderDialog.show();
    }


    public void showConfirmOrderDialog(String orderId) {
        if (confirmDialog == null) {
            confirmDialog = new CommonConfirmDialog(this, R.style.CancelDialog);
            confirmDialog.setText("确认收货吗？", "取消", "确认");
        }
        confirmDialog.setOnConfirmListener(new CommonConfirmDialog.OnConfirmListener() {
            @Override
            public void onConfirmClickListener() {
                mPresenter.commonAction(MallOrderDetailsPresenter.CONFIRM_ORDER, orderId, "");
            }
        });
        confirmDialog.show();
    }
}
