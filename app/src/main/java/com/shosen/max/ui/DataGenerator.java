package com.shosen.max.ui;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.presenter.MyWalletPresenter;
import com.shosen.max.ui.fragment.AutoMobileFragment;
import com.shosen.max.ui.mall.fragement.MallGoodListFragment;
import com.shosen.max.ui.mall.fragement.MallHomeFragment;
import com.shosen.max.ui.circle.fragment.CircleFragment;
import com.shosen.max.ui.circle.fragment.CircleWrapperFragment;
import com.shosen.max.ui.circle.fragment.FindingFragment;
import com.shosen.max.ui.fragment.CommonFragment;
import com.shosen.max.ui.fragment.ContributeListFragment;
import com.shosen.max.ui.fragment.HomePageFragment;
import com.shosen.max.ui.fragment.MineFragment;
import com.shosen.max.ui.mall.fragement.MyWalletFragment;

public class DataGenerator {

    public static final int[] mTabRes = {R.drawable.home_icon,
            R.drawable.message_icon, R.drawable.nine_s_icon, R.drawable.mall_icon, R.drawable.mine_icon};

    public static final int[] mTabsPresssed = {R.drawable.home_select,
            R.drawable.message_select, R.drawable.charity_select, R.drawable.mine_select};

    public static final String[] mTabTitle = {"首页", "圈子", "汽车馆", "商城", "我的"};

    public static final String[] MALL_TEXTS = {"全部", "待支付", "待收货", "已完成", "已取消"};

    public static final String[] WALLET_TEXT = {"余额明细", "积分明细"};


    /**
     * 首页fragments
     * @param from
     * @return
     */
    public static Fragment[] getFragments(String from) {
        Fragment fragments[] = new Fragment[5];
        fragments[0] = HomePageFragment.newInstance();
        fragments[1] = CircleWrapperFragment.newInstance();
        fragments[2] = AutoMobileFragment.newInstance();
        fragments[3] = MallHomeFragment.newInstance();
        fragments[4] = MineFragment.newInstance();
        return fragments;
    }

    public static Fragment[] getCircleFragments() {
        Fragment fragments[] = new Fragment[2];
        fragments[0] = CircleFragment.newInstance();
        fragments[1] = FindingFragment.newInstance();
        return fragments;
    }

    public static Fragment[] getContributeListFragments() {
        Fragment[] fragments = new Fragment[2];
        fragments[0] = ContributeListFragment.getInstance(0);
        fragments[1] = ContributeListFragment.getInstance(1);
        return fragments;
    }

    public static Fragment[] getMallOrderListFragments() {
        Fragment[] fragments = new Fragment[5];
        fragments[0] = MallGoodListFragment.newInstance(0);
        fragments[1] = MallGoodListFragment.newInstance(1);
        fragments[2] = MallGoodListFragment.newInstance(3);
        fragments[3] = MallGoodListFragment.newInstance(5);
        fragments[4] = MallGoodListFragment.newInstance(6);
        return fragments;
    }


    public static Fragment[] getWalletFragments() {
        Fragment[] fragments = new Fragment[2];
        fragments[0] = MyWalletFragment.getInstance(MyWalletPresenter.GET_BALANCE_LIST);
        fragments[1] = MyWalletFragment.getInstance(MyWalletPresenter.GET_PROPERTY_LIST);
        return fragments;
    }


    /**
     * 获取Tab 显示的内容
     *
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_content, null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }

    /**
     * 获取Tab 显示的内容
     *
     * @param position
     * @return
     */
    public static TabLayout.Tab getCircleTabView(TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.newTab();
        switch (position) {
            case 0:
                tab.setText("圈子");
                break;
            case 1:
                tab.setText("发现");
                break;
            case 2:
                tab.setText("群组");
                break;
            default:
                break;
        }
        return tab;
    }

}
