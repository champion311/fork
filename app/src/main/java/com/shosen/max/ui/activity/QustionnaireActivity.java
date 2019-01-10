package com.shosen.max.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.Answer;
import com.shosen.max.bean.QuestionnaireBean;
import com.shosen.max.presenter.QuestionnairePresenter;
import com.shosen.max.presenter.contract.QuestionnaireContract;
import com.shosen.max.ui.adapter.QuestionnaireAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class QustionnaireActivity extends BaseActivity implements QuestionnaireContract.View {
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
    @BindView(R.id.rc_questions)
    RecyclerView rcQuestions;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.tv_next)
    TextView tvNext;

    private QuestionnairePresenter mPresenter;

    private QuestionnaireAdapter mAdapter;

    private List<QuestionnaireBean> mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new QuestionnairePresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.home_page_color));
        tvHeadTitle.setText("问卷调查");
        mData = new ArrayList<>();
        mAdapter = new QuestionnaireAdapter(this, mData);
        rcQuestions.setLayoutManager(new LinearLayoutManager(this));
        rcQuestions.setAdapter(mAdapter);
        mPresenter.getData();
    }

    @OnClick({R.id.iv_back, R.id.tv_next, R.id.tv_skip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_skip:
                ActivityUtils.startActivity(OrderConfirmActivity.class);
                break;
            case R.id.tv_next:
                if (mAdapter.getResponse() == null) {
                    ToastUtils.show(this, "请填写所有问题");
                } else {
                    //
                    mPresenter.submitAnswer(mAdapter.getResponse());
                    ActivityUtils.startActivity(OrderConfirmActivity.class);
                }
                break;
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_questionnaire;
    }

    @Override
    public void showQuestionData(List<QuestionnaireBean> mData) {
        if (mData != null) {
            this.mData.addAll(mData);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showErrorMessage(String message) {
        //ToastUtils.show(this, message);
    }
}
