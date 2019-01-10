package com.shosen.max;

import android.content.Intent;

import com.shosen.max.receiver.JPushReceiver;
import com.shosen.max.receiver.MaxPushReceiver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class BoardCastReceiverTest {

//                 <action android:name="cn.jpush.android.intent.REGISTRATION" />
//                <!--Required 用户接收 SDK 消息的 intent-->
//                <action android:name="cn.jpush.android.intent.MESSAGE_RECEIVED" />
//                <!--Required 用户接收 SDK 通知栏信息的 intent-->
//                <action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED" />
//                <!--Required 用户打开自定义通知栏的 intent-->
//                <action android:name="cn.jpush.android.intent.NOTIFICATION_OPENED" />
//                <!-- 接收网络变化 连接/断开 since 1.6.3 -->
//                <action android:name="cn.jpush.android.intent.CONNECTION" />

    @Test
    public void testReceiver() {
        ShadowApplication shadowApplication = new ShadowApplication();
    }
}
