package com.shosen.max.ui.others;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.constant.Contstants;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_head_title)
    TextView ivHeadTitle;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.iv_help_image)
    ImageView ivHelpImage;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                ContextCompat.getColor(this, R.color.home_page_color));
        RequestOptions options = new RequestOptions();
        options.priority(Priority.NORMAL);
        GlideUtils.loadImage(this, Contstants.HELP_IMAGE_URL,
                options, GlideUtils.defaultTransOptions, ivHelpImage);
        ivHeadTitle.setText("帮助");
    }

    @Override
    protected int getContentViewID() {

        return R.layout.activity_help;
    }
}
