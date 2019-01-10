package com.shosen.max.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.bean.QuestionnaireBean;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.widget.MultiRadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionnaireAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;

    private List<QuestionnaireBean> mData;

    public static final int SINGLE = 0;

    public static final int MULTI = 1;

    private static LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    public QuestionnaireAdapter(Context mContext, List<QuestionnaireBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = null;
        if (viewType == SINGLE) {
            view = View.inflate(mContext, R.layout.item_questionnaire, null);
            return new QuestionnaireSingleHolder(view);
        } else if (viewType == MULTI) {
            view = View.inflate(mContext, R.layout.item_question_muti, null);
            return new QuestionnaireMultiHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QuestionnaireBean bean = mData.get(position);
        if (holder instanceof QuestionnaireSingleHolder) {
            QuestionnaireSingleHolder singleHolder = (QuestionnaireSingleHolder) holder;
            singleHolder.tvTitle.setText(bean.getTitle());
            addSingleType(singleHolder.raMuti, bean, position);
        } else if (holder instanceof QuestionnaireMultiHolder) {
            QuestionnaireMultiHolder multiHolder = (QuestionnaireMultiHolder) holder;
            multiHolder.tvTitle.setText(bean.getTitle());
            addMultiLine(multiHolder.llCheckBox, bean, position);
        }
    }

    public void addMultiLine(LinearLayout parent, QuestionnaireBean bean, int position) {
        int spanCount = bean.getSpanCount();
        LinearLayout linearLayout = null;
        for (int i = 0; i < bean.getData().size(); i++) {
            if ((i) % spanCount == 0) {
                if (linearLayout != null) {
                    parent.addView(linearLayout, params);
                }
                linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(params);
            }
            if (linearLayout != null) {
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams
                        (0, ViewGroup.LayoutParams.WRAP_CONTENT);
                param2.weight = 1;
                linearLayout.addView(getCheckBox(bean.getData().get(i).getText(), position, i), param2);
            }
        }
        if (linearLayout != null) {
            if (linearLayout.getChildCount() < spanCount) {
                for (int i = 0; i < spanCount - linearLayout.getChildCount(); i++) {
                    LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams
                            (0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    param2.weight = 1;
                    linearLayout.addView(new View(mContext), param2);
                }
            }
            parent.addView(linearLayout, params);
        }
    }


    public void addSingleType(MultiRadioGroup radioGroup, QuestionnaireBean bean, int position) {
        int spanCount = bean.getSpanCount();
        LinearLayout linearLayout = null;
        for (int i = 0; i < bean.getData().size(); i++) {
            if ((i) % spanCount == 0) {
                if (linearLayout != null) {
                    radioGroup.addView(linearLayout, params);
                }
                linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(params);
            }
            if (linearLayout != null) {
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams
                        (0, ViewGroup.LayoutParams.WRAP_CONTENT);
                param2.weight = 1;
                linearLayout.addView(getRadioButton(bean.getData().get(i).getText(), position, i), param2);
            }
        }
        if (linearLayout != null) {
            if (linearLayout.getChildCount() < spanCount) {
                for (int i = 0; i < spanCount - linearLayout.getChildCount(); i++) {
                    LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams
                            (0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    param2.weight = 1;
                    linearLayout.addView(new View(mContext), param2);
                }
            }
            radioGroup.addView(linearLayout, params);
        }

    }

    public CheckBox getCheckBox(String text, int subjectPos, int answerPos) {
        CheckBox checkBox = new CheckBox(mContext);
        checkBox.setButtonDrawable(null);
        checkBox.setId(subjectPos * 10 + answerPos);
        checkBox.setTextColor(ContextCompat.getColor(mContext, R.color.black_text_color));
        checkBox.setTextSize(15);
        checkBox.setText(text);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.questionaire_muti);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        checkBox.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 7));
        checkBox.setCompoundDrawables(drawable, null, null, null);
        checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            mData.get(subjectPos).updateAnswer(answerPos, isChecked);
        }));
        return checkBox;
    }

    public RadioButton getRadioButton(String text, int subjectPos, int answerPos) {
        RadioButton radioButton = new RadioButton(mContext);
        radioButton.setChecked(false);
        radioButton.setButtonDrawable(null);
        radioButton.setId(subjectPos * 10 + answerPos);
        radioButton.setTextColor(ContextCompat.getColor(mContext, R.color.black_text_color));
        radioButton.setTextSize(15);
        radioButton.setText(text);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.questionaire_single);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        radioButton.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 7));
        radioButton.setCompoundDrawables(drawable, null, null, null);
        radioButton.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            mData.get(subjectPos).updateAnswer(answerPos, isChecked);
        }));
        return radioButton;
    }


    @Override
    public int getItemViewType(int position) {
        QuestionnaireBean bean = mData.get(position);
        if (bean.getType() == 0) {
            return SINGLE;
        } else if (bean.getType() == 1) {
            return MULTI;
        }
        return SINGLE;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    public class QuestionnaireSingleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ra_muti)
        MultiRadioGroup raMuti;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        public QuestionnaireSingleHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class QuestionnaireMultiHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_check_box)
        LinearLayout llCheckBox;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        public QuestionnaireMultiHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * @return 返回为 null的时候说明有问题没有答
     */
    public List<QuestionnaireBean.AnswerResponse> getResponse() {
        List<QuestionnaireBean.AnswerResponse> ret = new ArrayList<>();
        for (QuestionnaireBean bean : mData) {
            QuestionnaireBean.AnswerResponse response = bean.generateAnswerResponse();
            if (TextUtils.isEmpty(response.getAnswerId())) {
                return null;
            } else {
                ret.add(response);
            }
        }
        return ret;
    }
}
