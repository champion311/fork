package com.shosen.max.utils;

import android.app.Activity;
import android.content.Context;

import com.shosen.max.R;
import com.shosen.max.constant.Contstants;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

public class ShareUtils {
    /**
     * 分享小程序
     *
     * @param activity
     * @param umImage
     * @param url
     * @param title
     * @param umShareListener
     */
    public static void shareMin(
            Activity activity, UMImage umImage, String url, String title, UMShareListener umShareListener) {
        UMMin umMin = new UMMin(url);
        //兼容低版本的网页链接
        umMin.setThumb(umImage);
        // 小程序消息封面图片
        umMin.setTitle(title);
        // 小程序消息title
        umMin.setDescription(title);
        // 小程序消息描述
        umMin.setPath("pages/login/login?invitorPhone=" + LoginUtils.getUser().getPhone());
        //小程序页面路径
        umMin.setUserName("gh_ca9acc95304d");
        Config.setMiniPreView();//体验版
        //Config.setMiniTest();//测试版
        // 小程序原始id,在微信平台查询
        new ShareAction(activity)
                .withMedia(umMin)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener).share();

    }

    /**
     * 运行小程序
     */
    public static void launchMini(Context mContext) {
        //Config.setMiniPreView();
        Config.setMiniPreView();//体验版
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(mContext, Contstants.WEI_XIN_APP_ID);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_ca9acc95304d";
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
        req.path = "pages/loginPL/index";
        iwxapi.sendReq(req);
    }


    public static void shareUrl(Activity activity, String url, SHARE_MEDIA shareMedia,
                                UMImage umImage, UMShareListener umShareListener) {
        UMWeb umWeb = new UMWeb(url);
        umWeb.setThumb(umImage);
        new ShareAction(activity).
                withMedia(umWeb).
                setPlatform(shareMedia).setCallback(umShareListener).share();
    }


}
