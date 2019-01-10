package com.shosen.max.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shosen.max.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoosePayMethodDialog extends Dialog {

    @BindView(R.id.tv_top)
    TextView tvTop;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.tv_cash)
    TextView tvCash;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    public ChoosePayMethodDialog(Context context) {
        super(context);
        initParams();
    }

    public ChoosePayMethodDialog(Context context, int themeResId) {
        super(context, themeResId);
        initParams();
    }

    private void initParams() {
        Window window = getWindow();
        window.setContentView(R.layout.dialog_choose_pay_method);
        ButterKnife.bind(this);
        window.setGravity(Gravity.BOTTOM);
        // 添加动画
        window.setWindowAnimations(R.style.DialogAnimMine);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //lp.width = ScreenUtils.getScreenWidth(getContext());
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        setCanceledOnTouchOutside(true);
        getWindow().setAttributes(lp);
    }

    @OnClick({R.id.tv_jifen, R.id.tv_cash})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_jifen:
                break;
            case R.id.tv_cash:
                break;
        }
    }
}
