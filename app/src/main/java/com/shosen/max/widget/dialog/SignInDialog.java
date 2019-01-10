package com.shosen.max.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInDialog extends Dialog {

    @BindView(R.id.star_icon_image)
    ImageView starIconImage;
    @BindView(R.id.tv_des)
    TextView tvDes;

    public SignInDialog(Context context) {
        super(context);
        initParams();
    }

    public SignInDialog(Context context, int themeResId) {
        super(context, themeResId);
        initParams();
    }

    public void initParams() {
        Window window = getWindow();
        window.setContentView(R.layout.dialog_sign_in);
        ButterKnife.bind(this);
        window.setGravity(Gravity.CENTER);
        // 添加动画
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //lp.width = ScreenUtils.getScreenWidth(getContext());
        lp.width = ScreenUtils.getScreenWidth(getContext()) - 2 * DensityUtils.dip2px(getContext(), 26);
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }

    public void setText(CharSequence text) {
        tvDes.setText(text);
    }
}
