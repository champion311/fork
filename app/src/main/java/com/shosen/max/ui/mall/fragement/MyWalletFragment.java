package com.shosen.max.ui.mall.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shosen.max.R;
import com.shosen.max.base.BaseFragment;
import com.shosen.max.bean.MoneyListResponse;
import com.shosen.max.presenter.MyWalletPresenter;
import com.shosen.max.presenter.contract.MyWalletContract;
import com.shosen.max.ui.adapter.MyWalletListAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyWalletFragment extends BaseFragment implements
        MyWalletContract.View, OnRefreshListener, OnLoadMoreListener {

    Unbinder unbinder;
    @BindView(R.id.rc_wallet_list)
    RecyclerView rcWalletList;
    @BindView(R.id.sr_refresn)
    SmartRefreshLayout srRefresh;
    private MyWalletPresenter mPresenter;

    private int requestCode;

    private int currentPage = 1;

    private String type;

    private MyWalletListAdapter adapter;

    private List<MoneyListResponse> data = new ArrayList<>();

    public static MyWalletFragment getInstance(int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putInt("requestCode", requestCode);
        MyWalletFragment fragment = new MyWalletFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        mPresenter = new MyWalletPresenter();
        setPresenter(mPresenter);
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_my_wallet_list, null);
        unbinder = ButterKnife.bind(this, contentView);
        initEventAndData();
        return contentView;
    }

    @Override
    protected void initEventAndData() {
        if (getArguments() != null) {
            requestCode = getArguments().getInt("requestCode", MyWalletPresenter.GET_BALANCE_LIST);
            type = null;
            if (requestCode == MyWalletPresenter.GET_BALANCE_LIST) {
                type = "1,2,3,4";
            } else if (requestCode == MyWalletPresenter.GET_PROPERTY_LIST) {
                type = "3,5";
            }
            if (type != null) {
                mPresenter.getData(type, requestCode, 1);
                srRefresh.setEnableLoadMore(true);
                srRefresh.setEnableRefresh(true);
                srRefresh.setOnRefreshListener(this);
                srRefresh.setOnLoadMoreListener(this);
            }
        }
        rcWalletList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyWalletListAdapter(getContext(), data);
        adapter.setRequestCode(requestCode);
        rcWalletList.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        srRefresh.finishLoadMore();
        srRefresh.finishRefresh();
    }

    @Override
    public void showDataBack(List<MoneyListResponse> responses, int requestType, int pageNum) {
        if (responses == null) {
            return;
        }
        if (pageNum == 1) {
            data.clear();
            data.addAll(responses);
            adapter.notifyDataSetChanged();
            currentPage = 1;
            srRefresh.finishRefresh();
        } else {
            if (responses.size() == 0) {
                ToastUtils.show(getActivity(), "没有更多数据了");
            } else {
                data.addAll(responses);
                adapter.notifyDataSetChanged();
                currentPage += 1;
            }
            srRefresh.finishLoadMore();
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        mPresenter.getData(type, requestCode, currentPage + 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mPresenter.getData(type, requestCode, 1);
    }
}
