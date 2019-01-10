package com.shosen.max.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseFragment;
import com.shosen.max.constant.Contstants;
import com.shosen.max.ui.activity.CommonWebViewActivity;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AutoMobileFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.view_left)
    View viewLeft;
    @BindView(R.id.view_right)
    View viewRight;

    @Override
    public void onHiddenChanged(boolean hidden) {
        StatusBarUtil.setDarkMode(getActivity());
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.home_page_color));
        super.onHiddenChanged(hidden);
    }

    public static AutoMobileFragment newInstance() {
        return new AutoMobileFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_automobile_hall, null);
        initTopHeader(contentView);
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    public void initTopHeader(View view) {
        ViewGroup rlTop = (ViewGroup) view.findViewById(R.id.rl_top_header);
        if (rlTop != null) {
            ViewGroup.LayoutParams layoutParams = rlTop.getLayoutParams();
            if (layoutParams instanceof RelativeLayout.LayoutParams) {
                ((RelativeLayout.LayoutParams) layoutParams).topMargin =
                        StatusBarUtil.getStatusBarHeight(getActivity());
            } else if (layoutParams instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) layoutParams).topMargin =
                        StatusBarUtil.getStatusBarHeight(getActivity());
            } else if (layoutParams instanceof FrameLayout.LayoutParams) {
                ((FrameLayout.LayoutParams) layoutParams).topMargin =
                        StatusBarUtil.getStatusBarHeight(getActivity());
            } else if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                ((CoordinatorLayout.LayoutParams) layoutParams).topMargin =
                        StatusBarUtil.getStatusBarHeight(getActivity());
            }
        }
    }


    @OnClick({R.id.view_left, R.id.view_right})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.view_left:
                intent = new Intent(getActivity(), CommonWebViewActivity.class);
                intent.putExtra("url", Contstants.URL_1);
                intent.putExtra("title", "让天下没有难务之商");
                startActivity(intent);

                break;
            case R.id.view_right:
                intent = new Intent(getActivity(), CommonWebViewActivity.class);
                intent.putExtra("url", Contstants.URL_2);
                intent.putExtra("title", "6s馆汽车功能");
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void initEventAndData() {
        ivBack.setVisibility(View.GONE);
        tvHeadTitle.setText("汽车馆");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
