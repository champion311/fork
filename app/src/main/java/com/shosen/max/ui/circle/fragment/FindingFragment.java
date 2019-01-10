package com.shosen.max.ui.circle.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shosen.max.R;
import com.shosen.max.base.BaseFragment;
import com.shosen.max.bean.BaseBannerBean;
import com.shosen.max.bean.FriendCircleBean;
import com.shosen.max.bean.FriendInvitationBean;
import com.shosen.max.bean.eventbusevent.CircleFragmentRefreshEvent;
import com.shosen.max.presenter.FindingFragmentPresenter;
import com.shosen.max.presenter.contract.CircleContract;
import com.shosen.max.ui.circle.CircleUserInfoActivity;
import com.shosen.max.ui.circle.FollowerListActivity;
import com.shosen.max.ui.circle.MessageDetailActivity;
import com.shosen.max.ui.circle.MessageListActivity;
import com.shosen.max.ui.circle.adapter.FollowerFriendsAdapter;
import com.shosen.max.ui.circle.adapter.FriendCircleAdapter;
import com.shosen.max.ui.circle.adapter.ImagePagerAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.RxUtils;
import com.shosen.max.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class FindingFragment extends BaseFragment
        implements CircleContract.FindingView,
        FollowerFriendsAdapter.FollowerFriendsItemClickListener,
        FriendCircleAdapter.CircleMessClickListener, OnRefreshListener,
        OnLoadMoreListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_image_grid)
    ViewPager vpImageGrid;
    @BindView(R.id.rl_follow_friends_text)
    RelativeLayout rlFollowFriendsText;
    @BindView(R.id.rc_follow_friends)
    RecyclerView rcFollowFriends;
    @BindView(R.id.mRecycle)
    RecyclerView mRecycle;
    @BindView(R.id.refresh_wrapper)
    SmartRefreshLayout refreshWrapper;
    Unbinder unbinder;
    @BindView(R.id.tv_more_friend_recommend)
    TextView tvMoreFriendRecommend;
    @BindView(R.id.rl_hot_topic)
    TextView rlHotTopic;
    @BindView(R.id.ll_line_wrapper)
    LinearLayout llLineWrapper;

    private FriendCircleAdapter mAdapter;

    private List<FriendCircleBean> mData;

    private View[] linesViews;

    private int currentSelectedPos = 0;

    public static FindingFragment newInstance() {
        Bundle args = new Bundle();
        FindingFragment fragment = new FindingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FindingFragmentPresenter mPresenter;

    private int currentPage;

    private int curPos;

    @Override
    public void onAttach(Context context) {
        mPresenter = new FindingFragmentPresenter(getActivity());
        setPresenter(mPresenter);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_friend_circle, null);
        unbinder = ButterKnife.bind(this, contentView);
        EventBus.getDefault().register(this);
        return contentView;
    }

    @Override
    protected void initEventAndData() {
        //loadData
        mPresenter.getBannerList(1);
        mPresenter.getFriendInvitation(1);
        mPresenter.getHotTop(1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcFollowFriends.setLayoutManager(linearLayoutManager);
        mRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycle.addItemDecoration(new SpaceItemDecoration());
        mRecycle.setAdapter(mAdapter);
        mRecycle.setHasFixedSize(true);
        mRecycle.setNestedScrollingEnabled(true);
        mRecycle.addOnScrollListener(new CircleRecycleScrollListener());
        mRecycle.setItemAnimator(new DefaultItemAnimator());
        mData = new ArrayList<>();
        mAdapter = new FriendCircleAdapter(getActivity(), mData);
        mAdapter.setFinding(true);
        mAdapter.setMessClickListener(this);
        mRecycle.setAdapter(mAdapter);
        refreshWrapper.setEnableRefresh(true);
        refreshWrapper.setEnableLoadMore(true);
        refreshWrapper.setOnRefreshListener(this);
        refreshWrapper.setOnLoadMoreListener(this);
        vpImageGrid.addOnPageChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @OnClick({R.id.tv_more_friend_recommend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_more_friend_recommend:
                Intent intent = new Intent(getActivity(), FollowerListActivity.class);
                intent.putExtra("userId", LoginUtils.getUser().getUid());
                intent.putExtra("fromPage", FollowerListActivity.FROM_FINDING);
                startActivity(intent);
                break;
        }
    }

    /**
     * banner回调
     *
     * @param data
     */
    @Override
    public void showBannerList(List<String> data) {
        if (data != null && data.size() != 0) {
            ArrayList<BaseBannerBean> mData = new ArrayList<>();
            for (String url : data) {
                mData.add(new BaseBannerBean(url));
            }
            vpImageGrid.setAdapter(new ImagePagerAdapter(mData, getActivity()));
            initViewPagerLines(data.size());
            loadBannerRunnable();
        }
    }

    /**
     * 推荐好友回调
     *
     * @param mData
     */
    @Override
    public void showFriendInvitation(List<FriendInvitationBean> mData) {
        if (mData != null) {
            FollowerFriendsAdapter adapter = new FollowerFriendsAdapter(mData, getActivity());
            adapter.setClickListener(this);
            rcFollowFriends.setAdapter(adapter);
        }

    }

    @Override
    public void showErrorMessage(String message) {
        if (!LoginUtils.isLogin) {
            return;
        }
        ToastUtils.show(getActivity(), message);
    }

    /**
     * 数据回调
     *
     * @param mData
     * @param pageNum
     */
    @Override
    public void showCircleData(List<FriendCircleBean> mData, int pageNum) {
        if (pageNum == 1) {
            if (mData == null) {
                return;
            }
            this.mData.clear();
            this.mData.addAll(mData);
            mAdapter.notifyDataSetChanged();
            currentPage = 1;
        } else {
            if (mData == null || mData.size() == 0) {
                ToastUtils.show(getActivity(), getString(R.string.no_more_data));
                return;
            }
            this.mData.addAll(mData);
            mAdapter.notifyDataSetChanged();
            currentPage += 1;
        }
    }

    /**
     * 点赞、取消点赞回调
     *
     * @param status
     * @param position
     */
    @Override
    public void updatePraiseStatus(String status, int position) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        //ToastUtils.show(getActivity(), status);
        mData.get(position).setMarkStatus(status);
        if (RegexUtils.isMatch("^[0-9]+", mData.get(position).getMarkCount())) {
            int currentMarkCount = Integer.valueOf(mData.get(position).getMarkCount());
            if ("1".equals(status)) {
                currentMarkCount += 1;
            } else if ("0".equals(status)) {
                currentMarkCount -= 1;
                if (currentMarkCount < 0) {
                    currentMarkCount = 0;
                }
            }
            mData.get(position).setMarkCount(String.valueOf(currentMarkCount));
        }
        mAdapter.notifyItemChanged(position);
    }

    /**
     * 删除消息回调
     *
     * @param position
     */
    @Override
    public void delMessageCallBack(int position) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        if (position < 0 || position > mData.size() - 1) {
            return;
        }
        mAdapter.notifyItemRemoved(position);
        mData.remove(position);
    }

    /**
     * 推荐好友点击
     *
     * @param view
     * @param position
     * @param bean
     */
    @Override
    public void onFollowerItemClick(View view, int position, FriendInvitationBean bean) {
        Intent intent = new Intent(getActivity(), CircleUserInfoActivity.class);
        intent.putExtra("userId", bean.getId());
        intent.putExtra("headImg", bean.getHeadImg());
        intent.putExtra("userName", bean.getName());
        startActivity(intent);
    }

    /**
     * 消息点击事件
     *
     * @param view
     * @param position
     * @param circleBean
     */
    @Override
    public void onItemClick(View view, int position, FriendCircleBean circleBean) {
        if (circleBean == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_message_count:
                ActivityUtils.startActivity(MessageListActivity.class);
                break;
            case R.id.fl_wrapper:
                Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra("circleBean", circleBean);
                startActivity(intent);
                break;
            case R.id.tv_praise_button:
                String status = "0";
                if ("1".equals(circleBean.getMarkStatus())) {
                    status = "0";
                } else {
                    status = "1";
                }
                mPresenter.addPraise(status, circleBean.getId(), position);
                break;
            case R.id.tv_message_del:
                //删除message
                mPresenter.delMessage(circleBean.getId(), position);
                break;
            case R.id.iv_head_image:
                Intent intent2 = new Intent(getActivity(), CircleUserInfoActivity.class);
                intent2.putExtra("userId", circleBean.getUserId());
                intent2.putExtra("headImg", circleBean.getHeadImg());
                intent2.putExtra("userName", circleBean.getName());
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore(1 * 1000);
        mPresenter.getHotTop(currentPage + 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh(1 * 1000);
        mPresenter.getBannerList(1);
        mPresenter.getFriendInvitation(1);
        mPresenter.getHotTop(1);
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = DensityUtils.dip2px(getActivity(), 4);
        }
    }

    public class CircleRecycleScrollListener extends RecyclerView.OnScrollListener {

        private boolean isScrolling;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                isScrolling = true;
                if (getActivity() != null && !getActivity().isFinishing()) {
                    Glide.with(getActivity()).pauseRequests();
                }
            } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (isScrolling == true) {
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        Glide.with(getActivity()).resumeRequests();
                    }
                }
                isScrolling = false;

            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(CircleFragmentRefreshEvent event) {
        mPresenter.getBannerList(1);
        mPresenter.getFriendInvitation(1);
        mPresenter.getHotTop(1);
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
        currentSelectedPos = 0;
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
