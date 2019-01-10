package com.shosen.max.ui.mall.fragement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shosen.max.R;
import com.shosen.max.base.BaseFragment;
import com.shosen.max.bean.MallHomePageBean;
import com.shosen.max.bean.mall.GoodsBean;
import com.shosen.max.presenter.MallHomePresenter;
import com.shosen.max.presenter.contract.MallHomeContract;
import com.shosen.max.ui.activity.LoginActivity;
import com.shosen.max.ui.adapter.GoodsItemAdapter;
import com.shosen.max.ui.circle.adapter.ImagePagerAdapter;
import com.shosen.max.ui.mall.GoodsDetailActivity;
import com.shosen.max.ui.mall.ShoppingCartActivity;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;
import com.shosen.max.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class MallHomeFragment extends BaseFragment
        implements MallHomeContract.View, GoodsItemAdapter.GoodsOnItemClick,
        ViewPager.OnPageChangeListener, OnRefreshListener {


    Unbinder unbinder;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_head_title)
    TextView ivHeadTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.vp_viewpager)
    ViewPager vpViewpager;
    @BindView(R.id.ll_line_wrapper)
    LinearLayout llLineWrapper;
    @BindView(R.id.rc_good_list)
    RecyclerView rcGoodList;
    @BindView(R.id.sr_refresh)
    SmartRefreshLayout srRefresh;


    private View[] linesViews;

    private MallHomePresenter mPresenter;

    private GoodsItemAdapter mAdapter;

    private int curPos;
    private GridLayoutManager gridLayoutManager;

    @Override
    public void onAttach(Context context) {
        mPresenter = new MallHomePresenter();
        setPresenter(mPresenter);
        super.onAttach(context);
    }

    public static MallHomeFragment newInstance() {
        Bundle bundle = new Bundle();
        MallHomeFragment fragment = new MallHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
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
        View contentView = inflater.inflate(R.layout.fragment_mall, null);
        unbinder = ButterKnife.bind(this, contentView);
        initTopHeader(contentView);
        initEventAndData();
        return contentView;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.loadData();
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rcGoodList.setLayoutManager(gridLayoutManager);
//        rcGoodList.addItemDecoration(new SpaceItemDecoration(2,
//                DensityUtils.dip2px(getActivity(), 10), false));
        rcGoodList.setHasFixedSize(true);
        ivBack.setVisibility(View.GONE);
        vpViewpager.addOnPageChangeListener(this);
        srRefresh.setEnableLoadMore(false);
        srRefresh.setEnableRefresh(true);
        srRefresh.setOnRefreshListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void initTopHeader(View view) {
        RelativeLayout rlTop = (RelativeLayout) view.findViewById(R.id.rl_top_header);
        if (rlTop != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rlTop.getLayoutParams();
            layoutParams.topMargin =
                    StatusBarUtil.getStatusBarHeight(getActivity());
            rlTop.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        srRefresh.finishRefresh();
    }

    @Override
    public void showData(MallHomePageBean mData) {
        srRefresh.finishRefresh();
        if (mData == null) {
            return;
        }
        if (mData.getBanner() != null) {
            vpViewpager.setAdapter(new ImagePagerAdapter(mData.getBanner(), getActivity()));
            initViewPagerLines(mData.getBanner().size());
            loadBannerRunnable();
        }
        if (mData.getFloorGoodsList() != null) {
            List<GoodsBean> goodsBeans = new ArrayList<>();
            for (MallHomePageBean.FloorGoodsList goodsList : mData.getFloorGoodsList()) {
                GoodsBean bean = new GoodsBean();
                bean.setHeader(true);
                bean.setHeaderName(goodsList.getName());
                goodsBeans.add(bean);
                for (GoodsBean innerData : goodsList.getGoodsList()) {
                    innerData.setHeaderName(goodsList.getName());
                }
                goodsBeans.addAll(goodsList.getGoodsList());
            }
            mAdapter = new GoodsItemAdapter(getActivity(), goodsBeans);
            mAdapter.setClick(this);
            rcGoodList.setAdapter(mAdapter);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int i) {
                    if (goodsBeans.get(i).isHeader()) {
                        return 2;
                    } else {
                        return 1;
                    }

                }
            });
        }
    }


    @Override
    public void onGoodsClickListener(View view, int position, GoodsBean bean) {
        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
        intent.putExtra("id", bean.getId());
        intent.putExtra("isPointGoods", bean.isPointGoods());
        startActivity(intent);

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (linesViews == null) {
            return;
        }
        if (i < 0 || i > linesViews.length) {
            return;
        }
        linesViews[curPos].setSelected(false);
        linesViews[i].setSelected(true);
        curPos = i;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mPresenter.loadData();
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;


        public SpaceItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


    public void initViewPagerLines(int size) {
        if (llLineWrapper == null) {
            return;
        }
        llLineWrapper.removeAllViews();
        linesViews = new View[size];
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(DensityUtils.dip2px(getActivity(), 15), DensityUtils.dip2px(getActivity(), 1));
        for (int i = 0; i < size; i++) {
            View view = new View(getActivity());
            view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.line_viewpager));
            linesViews[i] = view;
            if (i != 0) {
                layoutParams.leftMargin = DensityUtils.dip2px(getActivity(), 4);
            } else {
                layoutParams.leftMargin = 0;
                view.setSelected(true);
            }
            llLineWrapper.addView(view, layoutParams);
        }
        curPos = vpViewpager.getCurrentItem();
    }

    private boolean hasStarted = false;

    public synchronized void loadBannerRunnable() {
        if (linesViews == null || linesViews.length == 0) {
            return;
        }
        if (hasStarted) {
            //避免重复执行的情况
            return;
        }
        hasStarted = true;
        Disposable di = Observable.interval(5, TimeUnit.SECONDS).
                compose(RxUtils.ToMain()).subscribe(accept -> {
            int pos = (curPos + 1) % linesViews.length;
            vpViewpager.setCurrentItem(pos);
            Log.d("MallHomeFragment", (curPos + 1) + " " + (linesViews.length));
        }, throwable -> {

        });
        mPresenter.addSubscribe(di);
    }

    @OnClick({R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                if (!LoginUtils.isLogin) {
                    ActivityUtils.startActivity(LoginActivity.class);
                    return;
                }
                ActivityUtils.startActivity(ShoppingCartActivity.class);
                break;

        }
    }
}
