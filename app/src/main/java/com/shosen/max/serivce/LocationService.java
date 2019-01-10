package com.shosen.max.serivce;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.shosen.max.utils.LongitudeUtils;

public class LocationService extends Service {



    public LocationBinder mBinder = new LocationBinder();

    private LocationCallBack mCallBack;

    public void setCallBack(LocationCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private LongitudeUtils longitudeUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        longitudeUtils = new LongitudeUtils();
        longitudeUtils.initBaiduLocation(this, new LocationBD());
        longitudeUtils.start();

    }


    private class LocationBD extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double latitude = bdLocation.getLatitude();    //获取纬度信息
            double longitude = bdLocation.getLongitude();    //获取经度信息
            //LongitudeUtils.GetDistance()
            Log.d("tag", latitude + " " + longitude);
            if (mCallBack != null) {
                mCallBack.showLocation(latitude, longitude);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LocationBinder extends Binder {

        public LocationService getService() {
            return LocationService.this;
        }
    }

    public interface LocationCallBack {
        void showLocation(double latitude, double longitude);
    }
}
