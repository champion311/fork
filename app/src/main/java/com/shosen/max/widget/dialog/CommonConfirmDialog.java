package com.shosen.max.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonConfirmDialog extends Dialog {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;

    public CommonConfirmDialog(@NonNull Context context) {
        super(context);
        initParams();
    }

    public CommonConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initParams();
    }

    protected CommonConfirmDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initParams();
    }

    private OnConfirmListener onConfirmListener;

    private OnCancelConfirmClick onCancelConfirmClick;

    public void setOnCancelConfirmClick(OnCancelConfirmClick onCancelConfirmClick) {
        this.onCancelConfirmClick = onCancelConfirmClick;
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    private void initParams() {
        Window window = getWindow();
        //View view = View.inflate(getContext(), R.layout.dialog_cancel_order, null);
        window.setContentView(R.layout.dialog_common_confirm);
        ButterKnife.bind(this);
        window.setGravity(Gravity.CENTER);
        // 添加动画
        //window.setWindowAnimations(R.style.DialogAnimMine);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //lp.width = ScreenUtils.getScreenWidth(getContext());
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }

    public void setText(String title, String cancelText, String confirmText) {
        tvTitle.setText(title);
        tvCancel.setText(cancelText);
        tvConfirm.setText(confirmText);
    }

    public void showSingle(String title, String confirmText) {
        tvTitle.setText(title);
        tvConfirm.setText(confirmText);
        tvCancel.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvConfirm.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvConfirm.setLayoutParams(params);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (onCancelConfirmClick != null) {
                    onCancelConfirmClick.onCancelClickListener();
                }
                dismiss();
                break;
            case R.id.tv_confirm:
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirmClickListener();
                }
                dismiss();
                break;
            default:
                break;
        }
    }


    public interface OnConfirmListener {
        void onConfirmClickListener();

    }

    public interface OnCancelConfirmClick {
        void onCancelClickListener();
    }
}
