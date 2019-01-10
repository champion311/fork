package com.shosen.max.ui.mall.fragement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shosen.max.R;
import com.shosen.max.base.BaseFragment;
import com.shosen.max.bean.mall.GoodsOrderList;
import com.shosen.max.others.span.SpaceItemDecoration;
import com.shosen.max.presenter.MallGoodsPresenter;
import com.shosen.max.presenter.MallOrderDetailsPresenter;
import com.shosen.max.presenter.contract.MallGoodsOrderContract;
import com.shosen.max.ui.DataGenerator;
import com.shosen.max.ui.activity.PayActivity;
import com.shosen.max.ui.mall.MallOrderDetailActivity;
import com.shosen.max.ui.mall.SubmitOrderActivity;
import com.shosen.max.ui.mall.adapter.GoodsOrderListAdapter;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.dialog.CancelButtonClick;
import com.shosen.max.widget.dialog.CancelOrderDialog;
import com.shosen.max.widget.dialog.CommonConfirmDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MallGoodListFragment extends BaseFragment
        implements MallGoodsOrderContract.View,
        GoodsOrderListAdapter.OnItemClickListener {


    Unbinder unbinder;
    @BindView(R.id.rc_mall_order_list)
    RecyclerView rcMallOrderList;

    private MallGoodsPresenter mPresenter;

    private int showType = 0;

    private CancelOrderDialog cancelOrderDialog;

    private CommonConfirmDialog delOrderDialog;

    private CommonConfirmDialog confirmDialog;

    private List<GoodsOrderList> mData = new ArrayList<>();

    private GoodsOrderListAdapter mAdapter;

    /**
     *
     * @param showType
     * @return
     */
    public static MallGoodListFragment newInstance(int showType) {
        Bundle bundle = new Bundle();
        bundle.putInt("showType", showType);
        MallGoodListFragment fragment = new MallGoodListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        Log.d("mallfragment", "attach showType" + showType);
        mPresenter = new MallGoodsPresenter();
        setPresenter(mPresenter);
        super.onAttach(context);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isHidden()) {
            StatusBarUtil.setLightMode(getActivity());
            StatusBarUtil.setColorNoTranslucent(getActivity(), Color.WHITE);
        } else {
            StatusBarUtil.setDarkMode(getActivity());
            StatusBarUtil.setColorNoTranslucent(getActivity(), getActivity().getResources().getColor(R.color.home_page_color));
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_mall_goods_list, null);
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEventAndData();
    }


    @Override
    protected void initEventAndData() {
        if (getArguments() != null) {
            showType = getArguments().getInt("showType", 0);
        }
        rcMallOrderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcMallOrderList.addItemDecoration(new SpaceItemDecoration
                (15, 15, 15, 0));
        mAdapter = new GoodsOrderListAdapter(getActivity(), mData);
        mAdapter.setMclicK(this);
        rcMallOrderList.setAdapter(mAdapter);
        Log.d("mallfragment", " create showType =" + showType);
        mPresenter.getMallOrderList(showType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("mallfragment", " destoryView showType =" + showType);
        unbinder.unbind();
    }

    /**
     * 获取数据
     *
     * @param mData
     */
    @Override
    public void showOrderListData(List<GoodsOrderList> mData) {
        if (mData == null) {
            return;
        }
        this.mData.clear();
        this.mData.addAll(mData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        ToastUtils.show(getActivity(), throwable.toString());
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
            Intent intent = new Intent(getActivity(), PayActivity.class);
            intent.putExtra("orderId", bean.getId());
            intent.putExtra("totalFee", String.valueOf(bean.getActualPrice()));
            intent.putExtra(PayActivity.REQUEST_TYPE, PayActivity.MALL_REQUEST);
            startActivity(intent);
        }
    }

    @Override
    public void allClick(View view, int position, GoodsOrderList bean) {
        Intent intent = new Intent(getActivity(), MallOrderDetailActivity.class);
        intent.putExtra("orderId", bean.getId());
        startActivity(intent);
    }


    @Override
    public void commonCallBack(int requestCode) {
        switch (requestCode) {
            case MallOrderDetailsPresenter.DELETE_ORDER:
                ToastUtils.showAlertToast(getActivity(), "删除订单成功");
                break;
            case MallOrderDetailsPresenter.CANCEL_ORDER:
                ToastUtils.showAlertToast(getActivity(), "取消订单成功");
                break;
            case MallOrderDetailsPresenter.CONFIRM_ORDER:
                ToastUtils.showAlertToast(getActivity(), "确认收货成功");
                //确认支付回调
                break;
            default:
                break;
        }
        mPresenter.getMallOrderList(showType);
        if (showType == 0) {
            //TODO 全部修改数据时通知相应页面
        } else {
//            if (DataGenerator.getMallOrderListFragments()[1] != null) {
//                ((MallGoodListFragment) DataGenerator.getMallOrderListFragments()[1]).updateData();
//            }
        }
    }


    public void showCancelDialog(String orderId) {
        if (cancelOrderDialog == null) {
            cancelOrderDialog = new CancelOrderDialog(getActivity(), R.style.CancelDialog);
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
            delOrderDialog = new CommonConfirmDialog(getActivity(), R.style.CancelDialog);
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
            confirmDialog = new CommonConfirmDialog(getActivity(), R.style.CancelDialog);
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

    @Override
    public void onResume() {
        Log.d("mallfragment", "onResume showType=" + showType);
        mPresenter.getMallOrderList(showType);
        super.onResume();
    }
}
