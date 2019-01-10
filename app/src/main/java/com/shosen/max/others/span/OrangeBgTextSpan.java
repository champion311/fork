package com.shosen.max.others.span;

import android.content.Context;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;

import com.shosen.max.R;
import com.shosen.max.utils.DensityUtils;

public class OrangeBgTextSpan extends BackgroundColorSpan {

    private Context mContext;

    public OrangeBgTextSpan(int color, Context mContext) {
        super(color);
        this.mContext = mContext;
    }

    public OrangeBgTextSpan(Parcel src, Context mContext) {
        super(src);
        this.mContext = mContext;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setTextSize(DensityUtils.sp2px(mContext, 13));
        textPaint.setColor(ContextCompat.getColor(mContext, R.color.default_address_text_color));
    }
}
