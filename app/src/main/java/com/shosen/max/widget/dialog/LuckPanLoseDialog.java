package com.shosen.max.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LuckPanLoseDialog extends Dialog {


    @BindView(R.id.tv_luckpan_1)
    TextView tvLuckpan1;
    @BindView(R.id.tv_luckpan_2)
    TextView tvLuckpan2;
    @BindView(R.id.tv_certain_btn)
    TextView tvCertainBtn;
    @BindView(R.id.tv_luckpan_3)
    TextView tvLuckpan3;

    private onConfirmClickListener mClick;

    public void setOnClick(onConfirmClickListener mClick) {
        this.mClick = mClick;
    }

    public LuckPanLoseDialog(Context context) {
        super(context);
        initParams();
    }


    public LuckPanLoseDialog(Context context, int themeResId) {
        super(context, themeResId);
        initParams();
    }

    public void initParams() {
        Window window = getWindow();
        window.setContentView(R.layout.dialog_luckpan_lose);
        ButterKnife.bind(this);
        window.setGravity(Gravity.CENTER);
        // 添加动画
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = ScreenUtils.getScreenWidth(getContext()) - 2 * DensityUtils.dip2px(getContext(), 45);
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }

    public void setText(String text1, String text2) {
        tvLuckpan1.setText(text1);
        tvLuckpan2.setText(text2);
    }

    public void setText(String text1, String text2, String text3) {
        tvLuckpan1.setText(text1);
        tvLuckpan2.setText(text2);
        tvLuckpan3.setText(text3);
    }

    public void setConfirmButtonText(String text) {
        tvCertainBtn.setText(text);
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) tvLuckpan1.getLayoutParams();
        layoutParams.topMargin = 25;
        tvCertainBtn.setLayoutParams(layoutParams);
    }

    @OnClick({R.id.tv_certain_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_certain_btn:
                if (mClick != null) {
                    mClick.onClick(view);
                }
                dismiss();
                break;
        }
    }

    public interface onConfirmClickListener {
        void onClick(View view);
    }
}
