package com.shosen.max.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.shosen.max.utils.LoginUtils;

import java.util.List;

public class QuestionnaireBean {

    private String title;
    private int spanCount;
    private int type;
    private int questionId;

    @SerializedName("answerList")
    private List<Answer> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public List<Answer> getData() {
        return data;
    }

    public void setData(List<Answer> data) {
        this.data = data;
    }

    public void updateAnswer(int position, boolean isCheck) {
        if (data == null) {
            return;
        }
        if (position < 0 || position > data.size()) {
            return;
        }
        data.get(position).setChecked(isCheck);
    }

    public AnswerResponse generateAnswerResponse() {
        AnswerResponse ret = new AnswerResponse();
        StringBuilder sb = new StringBuilder();
        for (Answer answer : data) {
            if (answer.isChecked()) {
                if (!TextUtils.isEmpty(sb)) {
                    sb.append(",");
                }
                sb.append(answer.getId());
            }
        }
        ret.setAnswerId(sb.toString());
        ret.setSubjectId(String.valueOf(questionId));
        if (LoginUtils.isLogin) {
            ret.setUserId(LoginUtils.getUser().getUid());
        } else {
            ret.setUserId("-1");
        }
        return ret;
    }


    public class AnswerResponse {

        private String answerId;

        private String subjectId;

        private String userId;

        public String getAnswerId() {
            return answerId;
        }

        public void setAnswerId(String answerId) {
            this.answerId = answerId;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
