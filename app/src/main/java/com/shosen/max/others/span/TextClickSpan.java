package com.shosen.max.others.span;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.shosen.max.R;
import com.shosen.max.utils.ToastUtils;

import io.reactivex.annotations.NonNull;

public class TextClickSpan extends ClickableSpan {

    private Context mContext;

    private String mUserName;
    private boolean mPressed;
    @ColorRes
    private int colorRes;

    private TextClick mClick;

    public void setClick(TextClick mClick) {
        this.mClick = mClick;
    }

    public TextClickSpan(Context context, String userName) {
        this.mContext = context;
        this.mUserName = userName;
    }

    public TextClickSpan(Context mContext, String mUserName, int colorRes) {
        this.mContext = mContext;
        this.mUserName = mUserName;
        this.colorRes = colorRes;
    }

    public void setPressed(boolean isPressed) {
        this.mPressed = isPressed;
    }

    @Override
    public void onClick(@NonNull View widget) {
        if (mClick != null) {
            mClick.textClick(widget);
        }
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.bgColor = mPressed ? ContextCompat.getColor(mContext, R.color.base_B5B5B5) : Color.TRANSPARENT;
        if (colorRes == 0) {
            ds.setColor(ContextCompat.getColor(mContext, R.color.c666));
        } else {
            ds.setColor(ContextCompat.getColor(mContext, colorRes));
        }
        ds.setUnderlineText(false);
    }

    public interface TextClick {
        void textClick(View view);
    }
}
