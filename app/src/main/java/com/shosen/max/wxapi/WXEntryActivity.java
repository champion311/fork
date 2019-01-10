package com.shosen.max.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;

import com.bin.david.form.data.form.IForm;
import com.shosen.max.constant.Contstants;
import com.shosen.max.ui.activity.HomePageActivity;
import com.shosen.max.utils.ActivityUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXEmojiObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
import com.umeng.weixin.umengwx.a;
import com.umeng.weixin.umengwx.b;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WXEntryActivity extends WXCallbackActivity {

    //
//    private static String TAG = "WXEntryActivity";
//
//
//    private IWXAPI api;

//    @Override
//    protected void onCreate(Bundle bundle) {
//        api = WXAPIFactory.createWXAPI(this, Contstants.WEI_XIN_APP_ID);
//        api.handleIntent(getIntent(), this);
//        super.onCreate(bundle);
//
//    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//    }


//    @Override
//    public void onReq(BaseReq baseReq) {
//        ActivityUtils.startActivity(HomePageActivity.class);
//        switch (baseReq.getType()){
//
//        }
//        Log.d(TAG, baseReq.toString());
//
//    }
//
//    @Override
//    public void onResp(BaseResp baseResp) {
//        Log.d(TAG, baseResp.toString());
//    }

//    @Override
//    protected void a(Intent intent) {
//        Log.d(TAG, a.toString());
//        this.a.getWXApi().handleIntent(intent, this);
//        int type = intent.getIntExtra("_wxapi_command_type", 0);
//        Log.d(TAG, "type=" + type);
//        if (type == 4) {
//            //小程序调用
//            //_wxobject_message_ext
//            Bundle bundle = intent.getExtras();
//            for (String key : bundle.keySet()) {
//                Log.i(TAG, "Key=" + key + ", content=" + bundle.get(key));
//                if ("_mmessage_checksum".equals(key)) {
//                    Object object = bundle.get(key);
//                }
//            }
//        }
//        super.a(intent);
//    }


}
