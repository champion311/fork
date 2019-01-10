package com.shosen.max.ui.fragment;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bin.david.form.data.form.IForm;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;
import com.shosen.max.base.BaseFragment;
import com.shosen.max.bean.BaseBannerBean;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.NewsBean;
import com.shosen.max.constant.Contstants;
import com.shosen.max.presenter.HomePageFragmentPresenter;
import com.shosen.max.presenter.contract.HomePageContract;
import com.shosen.max.ui.activity.CommonWebViewActivity;
import com.shosen.max.ui.activity.ContributionValueActivity;
import com.shosen.max.ui.activity.LoginActivity;
import com.shosen.max.ui.activity.LuckPanActivity;
import com.shosen.max.ui.activity.MyInvitationActivity;
import com.shosen.max.ui.activity.OrderCarActivity;
import com.shosen.max.ui.circle.adapter.ImagePagerAdapter;
import com.shosen.max.ui.adapter.HomePageGridAdapter;
import com.shosen.max.ui.adapter.NewsAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.dialog.LuckPanLoseDialog;
import com.shosen.max.widget.dialog.SignInDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class HomePageFragment extends BaseFragment implements
        HomePageGridAdapter.HomePageGridOnClick, HomePageContract.View,
        NewsAdapter.OnItemClickInterface, ViewPager.OnPageChangeListener {


    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.iv_head_title)
    ImageView ivHeadTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.vp_image_grid)
    ViewPager vpImageGrid;
    @BindView(R.id.mGridView)
    RecyclerView mGridView;
    @BindView(R.id.v_to_order)
    View vToOrder;
    @BindView(R.id.tv_more_news)
    TextView tvMoreNews;
    @BindView(R.id.rc_news)
    RecyclerView rcNews;
    @BindView(R.id.ll_line_wrapper)
    LinearLayout llLineWrapper;
    private HomePageGridAdapter mAdapter;

    private Unbinder unbinder;

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    private HomePageFragmentPresenter mPresenter;

    private LuckPanLoseDialog loseDialog;

    private View[] linesViews;

    private int curPos;

    @Override
    public void onAttach(Context context) {
        mPresenter = new HomePageFragmentPresenter(getActivity());
        setPresenter(mPresenter);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_home_page, null);
        unbinder = ButterKnife.bind(this, contentView);
        initEventAndData();
        initTopHeader(contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected void initEventAndData() {
        StatusBarUtil.setDarkMode(getActivity());
        StatusBarUtil.setColorNoTranslucent(getActivity(), getActivity().getResources().getColor(R.color.home_page_color));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        mAdapter = new HomePageGridAdapter(getActivity());
        mAdapter.setInterface(this);
        mGridView.setLayoutManager(gridLayoutManager);
        mGridView.setAdapter(mAdapter);
        //mGridView.addItemDecoration(new SpaceItemDecoration());
        //取消滑动
        mGridView.setHasFixedSize(false);
        mGridView.setNestedScrollingEnabled(false);
        mPresenter.getBannerList(2);
        mPresenter.getNews();
        rcNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcNews.setHasFixedSize(true);
        //rcNews.setNestedScrollingEnabled(false);
        vpImageGrid.addOnPageChangeListener(this);
        RequestOptions reqOptions = new RequestOptions().
                diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .priority(Priority.HIGH);
        GlideUtils.loadImage(getActivity(), Contstants.HOME_TITLE_URL, reqOptions,
                GlideUtils.defaultTransOptions, ivHeadTitle);

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

    @Override
    public void onResume() {
        StatusBarUtil.setDarkMode(getActivity());
        StatusBarUtil.setColorNoTranslucent(getActivity(), getActivity().getResources().getColor(R.color.home_page_color));
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void OnGridClick(View view, int position) {
        switch (position) {
            case 0:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                ActivityUtils.startActivity(LuckPanActivity.class);
                break;
            case 1:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                mPresenter.signIn();
                break;
            case 2:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                ActivityUtils.startActivity(MyInvitationActivity.class);
                break;
            case 3:
//                if (loseDialog == null) {
//                    loseDialog = new LuckPanLoseDialog(getActivity());
//                    loseDialog.setText("", "", getString(R.string.mall_text));
//                }
//                loseDialog.show();
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), ContributionValueActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void showBannerList(List<String> data) {
        if (data != null) {
            ArrayList<BaseBannerBean> mData = new ArrayList<>();
            for (String url : data) {
                mData.add(new BaseBannerBean(url));
            }
            vpImageGrid.setAdapter(new ImagePagerAdapter(mData, getActivity()));
            vpImageGrid.setCurrentItem(0);
            initViewPagerLines(data.size());
            loadBannerRunnable();
        }
    }

    @Override
    public void showErrorMessage(String message) {

    }

    private SignInDialog signInDialog;

    @Override
    public void singSuccess(BaseResponse baseResponse) {
        if (signInDialog == null) {
            signInDialog = new SignInDialog(getActivity(), R.style.CancelDialog);
        }
        signInDialog.setText(getSpanStr());
        if (baseResponse.getCode() == 100) {
            signInDialog.setText(getSpanStr());
        } else if (baseResponse.getCode() == 998) {
            ToastUtils.show(getActivity(), baseResponse.getMsg());
        } else {
            //
            signInDialog.setText("已进行过签到");
            //signInDialog.setText(baseResponse.getMsg());
        }
        signInDialog.show();
    }

    public SpannableString getSpanStr() {
        String t1 = getString(R.string.sign_in_text);
        String t2 = getString(R.string.sign_in_text2);
        SpannableString spannableString = new SpannableString(t1 + t2);
        MineSpan mineSpan = new MineSpan(17, true);
        spannableString.setSpan(mineSpan, t1.length(), t1.length() + t2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public class MineSpan extends AbsoluteSizeSpan {

        public MineSpan(int size) {
            super(size);
        }

        public MineSpan(int size, boolean dip) {
            super(size, dip);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.seleted_text_color));
        }
    }


    @Override
    public void showNews(List<NewsBean> mData) {
        NewsAdapter adapter = new NewsAdapter(getActivity(), mData);
        adapter.setItemClickInterface(this);
        rcNews.setAdapter(adapter);
    }

    /**
     * 发现点击
     *
     * @param view
     * @param position
     * @param bean
     */
    @Override
    public void onItemClick(View view, int position, NewsBean bean) {
        Intent intent = new Intent(getActivity(), CommonWebViewActivity.class);
        intent.putExtra("url", bean.getLinkUrl());
        intent.putExtra("title", "资讯");
        startActivity(intent);

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
        curPos = vpImageGrid.getCurrentItem();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public synchronized void onPageSelected(int i) {
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


    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = DensityUtils.dip2px(getActivity(), 14);
        }
    }

    @OnClick({R.id.v_to_order, R.id.tv_more_news})
    public void OnClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.v_to_order:
//                if (!LoginUtils.isLogin) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                    return;
//                }
                startActivity(new Intent(getActivity(), OrderCarActivity.class));
                break;
            case R.id.tv_more_news:
                intent = new Intent(getActivity(), CommonWebViewActivity.class);
                intent.putExtra("url", Contstants.MAX_URL);
                intent.putExtra("title", "资讯");
                startActivity(intent);
                break;
        }
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
            vpImageGrid.setCurrentItem(pos);
            Log.d("HomePageFragment", (curPos + 1) + " " + (linesViews.length));
        }, throwable -> {

        });
        mPresenter.addSubscribe(di);
    }
}
