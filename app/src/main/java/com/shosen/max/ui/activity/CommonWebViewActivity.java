package com.shosen.max.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.MaxApplication;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.constant.Contstants;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.StatusBarUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonWebViewActivity extends BaseActivity {
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
    @BindView(R.id.ll_webview_wrapper)
    LinearLayout llWebviewWrapper;

    private WebView mWebView;

    private String url;

    private String title;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,ContextCompat.getColor(this,R.color.home_page_color));
        mWebView = new WebView(getApplicationContext());
        mWebView.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llWebviewWrapper.addView(mWebView, layoutParams);
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
            if (!TextUtils.isEmpty(title)) {
                tvHeadTitle.setText(title);
            }
            mWebView.setWebViewClient(new WebViewClient() {


                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    mWebView.loadUrl(url);
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.getSettings().setBlockNetworkImage(false);
            mWebView.getSettings().setAllowFileAccess(true);
//          mWebView.getSettings().setUseWideViewPort(true);//让webview读取网页设置的viewport，pc版网页
//          mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setDomStorageEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            //TODO 待修改
            File cacheFile = new File(MaxApplication.getAppContext().getCacheDir(), Contstants.WEB_VIEW_CACHE_FILE_NAME);
            mWebView.getSettings().setAppCachePath(cacheFile.getPath());
            if (!TextUtils.isEmpty(url)) {
                if (!RegexUtils.isURL(url)) {
                    url = "http://" + url;
                }
                mWebView.loadUrl(url);
            }
        }
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_common;
    }

    /**
     * 处理内存泄露
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            try {
                mWebView.destroy();
            } catch (Throwable t) {
            }
            mWebView = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
