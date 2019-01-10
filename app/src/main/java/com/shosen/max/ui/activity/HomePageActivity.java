package com.shosen.max.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.LocationBean;
import com.shosen.max.bean.eventbusevent.HomePageRefreshEvent;
import com.shosen.max.presenter.HomePageActivityPresenter;
import com.shosen.max.presenter.contract.HomePageContract;
import com.shosen.max.serivce.LocationService;
import com.shosen.max.ui.DataGenerator;
import com.shosen.max.ui.circle.SubmitActivity;
import com.shosen.max.ui.fragment.CommonFragment;
import com.shosen.max.ui.fragment.HomePageFragment;
import com.shosen.max.ui.fragment.MineFragment;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.LongitudeUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.dialog.LuckPanLoseDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class HomePageActivity extends BaseActivity
        implements TabLayout.OnTabSelectedListener,
        LocationService.LocationCallBack, HomePageContract.HomePageActivityView {

    @BindView(R.id.home_container)
    FrameLayout homeContainer;
    @BindView(R.id.bottom_tab)
    TabLayout bottomTab;


    private Fragment[] mFragmensts;

    private int currentFragmentPos = 0;

    private RxPermissions rxPermissions;

    private LocationServiceConnection connection;

    private HomePageActivityPresenter mPresenter;

    private List<LocationBean> locations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new HomePageActivityPresenter();
        setPresenter(mPresenter);
        mPresenter.getLocations();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_home_page;
    }


    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.home_page_color));
        LoginUtils.getUser();//初始化
        mFragmensts = DataGenerator.getFragments("test");
        loadFragments();
        bottomTab.addOnTabSelectedListener(this);
        for (int i = 0; i < 5; i++) {
            bottomTab.addTab(bottomTab.newTab().setCustomView(DataGenerator.getTabView(this, i)));
        }
        rxPermissions = new RxPermissions(this);
        //定位权限动态申请
        rxPermissions.request(SubmitActivity.permissions).subscribe(grant -> {
            connection = new LocationServiceConnection();
            Intent intent = new Intent(this, LocationService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);

        }, throwable -> {
            //权限申请失败

        });
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        onTabItemSelected(tab.getPosition());
        tab.getCustomView().setSelected(true);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        tab.getCustomView().setSelected(false);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void onTabItemSelected(int position) {
        if (currentFragmentPos != position && mFragmensts[currentFragmentPos] != null) {
            getSupportFragmentManager().beginTransaction().
                    hide(mFragmensts[currentFragmentPos]).show(mFragmensts[position]).commitAllowingStateLoss();

        }
        currentFragmentPos = position;
    }


    /**
     * 初始化fragments
     */
    public void loadFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : mFragmensts) {
            transaction.add(R.id.home_container, fragment);
            if (fragment instanceof HomePageFragment) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }
        //处理Stateloss异常
        transaction.commitAllowingStateLoss();
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
                ToastUtils.show(this, "再按一次退出");
                return true;
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private LuckPanLoseDialog loseDialog;

    /**
     * 轮询定位经纬度接口回调
     *
     * @param latitude
     * @param longitude
     */
    @Override
    public void showLocation(double latitude, double longitude) {
        if (locations == null || locations.size() == 0) {
            return;
        }
        double meters;
        for (LocationBean bean : locations) {
            meters = LongitudeUtils.
                    GetDistance(longitude, latitude, Double.valueOf(bean.getLongitude()),
                            Double.valueOf(bean.getLatitude()));
            if (meters <= 100) {
                //TODO发送短信
                if (loseDialog == null) {
                    loseDialog = new LuckPanLoseDialog(this);
                }
                loseDialog.setText("", "", "距离" + bean.getPosiChinese() + " " + meters + "米");
                if (!loseDialog.isShowing()) {
                    loseDialog.show();
                }
                mPresenter.sendSms(bean.getPosiChinese());
            }
        }
    }

    /**
     * 获取地址成功
     *
     * @param mData
     */
    @Override
    public void showLocations(List<LocationBean> mData) {
        this.locations = mData;
    }

    @Override
    public void showErrorMessage(Throwable throwable) {

    }

    /**
     * 短信回调
     *
     * @param msg
     */
    @Override
    public void sendSmsBack(String msg) {

    }

    public class LocationServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //设置回调
            LocationService.LocationBinder binder = (LocationService.LocationBinder) service;
            binder.getService().setCallBack(HomePageActivity.this);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onDestroy() {
        if (connection != null) {
            unbindService(connection);
        }
        super.onDestroy();
    }

    /**
     * 调转到指定fragment
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void handleFragmentPos(HomePageRefreshEvent event) {
        Log.d("HomePageActivity", "stick event");
        EventBus.getDefault().removeStickyEvent(event);
//        if (mFragmensts == null) {
//            return;
//        }
//        if (event.getPos() < mFragmensts.length) {
//            changeToHomePage(event.getPos());
//        }
//        bottomTab.getTabAt(2).getCustomView().setSelected(false);
    }


    /**
     * 转到首页
     */
    public void changeToHomePage(int position) {
        onTabItemSelected(position);
        bottomTab.getTabAt(position).getCustomView().setSelected(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (EventBus.getDefault().isRegistered(this)) {
            //ToastUtils.show(this, "eventbus has register");
            return;
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


}
