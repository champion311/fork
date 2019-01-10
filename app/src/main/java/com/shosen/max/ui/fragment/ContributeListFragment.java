package com.shosen.max.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.data.form.IForm;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shosen.max.R;
import com.shosen.max.base.BaseFragment;
import com.shosen.max.bean.ContributeListBean;
import com.shosen.max.presenter.ContributeListPresenter;
import com.shosen.max.presenter.MineFragmentPresenter;
import com.shosen.max.presenter.contract.ContributeContract;
import com.shosen.max.ui.adapter.ContributeListAdapter;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContributeListFragment extends BaseFragment
        implements ContributeContract.ContributeListView, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.no_allowance_image)
    ImageView noAllowanceImage;
    @BindView(R.id.tv_alert_text)
    TextView tvAlertText;
    @BindView(R.id.rc_contribute_list)
    RecyclerView rcContributeList;
    @BindView(R.id.refresh_wrapper)
    SmartRefreshLayout refreshWrapper;
    Unbinder unbinder;

    private ContributeListPresenter mPresenter;

    private ContributeListAdapter mAdapter;

    private List<ContributeListBean> mData;

    private int currentPage = 1;

    private int position = 0;

    public static ContributeListFragment getInstance(int position) {
        ContributeListFragment fragment = new ContributeListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = new ContributeListPresenter();
        setPresenter(mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_contributelist, null);
        unbinder = ButterKnife.bind(this, contentView);
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
        return contentView;
    }

    @Override
    protected void initEventAndData() {
        mData = new ArrayList<>();
        mAdapter = new ContributeListAdapter(getActivity(), mData);
        rcContributeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcContributeList.setAdapter(mAdapter);
        if (LoginUtils.isLogin) {
            if (position == 0) {
                mPresenter.getContributeList(1);
            } else if (position == 1) {
                mPresenter.getContributeToday();
            }
        }
        refreshWrapper.setEnableRefresh(true);
        refreshWrapper.setEnableLoadMore(true);
        refreshWrapper.setOnRefreshListener(this);
        refreshWrapper.setOnLoadMoreListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        mPresenter.getContributeList(currentPage + 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mPresenter.getContributeList(1);
    }

    @Override
    public void showContributeList(List<ContributeListBean> mData, int pageNum) {
        if (mData == null || mData.size() == 0 && pageNum == 1) {
            noAllowanceImage.setVisibility(View.VISIBLE);
            tvAlertText.setText("暂无贡献");
            tvAlertText.setVisibility(View.VISIBLE);
            return;
        }
        tvAlertText.setVisibility(View.GONE);
        noAllowanceImage.setVisibility(View.GONE);
        if (pageNum == 1) {
            this.mData.clear();
            this.mData.addAll(mData);
            currentPage = 1;
            refreshWrapper.finishRefresh();
        } else {
            if (mData == null || mData.size() == 0) {
                ToastUtils.show(getActivity(), getString(R.string.no_more_data));
                refreshWrapper.finishLoadMore();
                return;
            }
            this.mData.addAll(mData);
            currentPage += 1;
            refreshWrapper.finishLoadMore();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        refreshWrapper.finishRefresh();
        refreshWrapper.finishLoadMore();
    }
}
