package com.shosen.max.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsListActivity extends BaseActivity {
    @BindView(R.id.rc_news_list)
    RecyclerView rcNewsList;

    @Override
    protected void initViewAndEvents() {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_news_list;
    }


}
