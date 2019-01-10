package com.shosen.max.ui.activity;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.TableBean;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.ScreenUtils;
import com.shosen.max.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyAllowanceDetailActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.table)
    SmartTable table;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.fl_top_wrapper)
    FrameLayout flTopWrapper;

    private boolean isShowTable = true;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                ContextCompat.getColor(this, R.color.home_page_color));
        tvHeadTitle.setText("邀请有礼");
        initTableData();
        setContentData();
        if (getIntent() != null) {
            isShowTable = getIntent().getBooleanExtra("isShowTable", true);
        }
        if (isShowTable) {
            flTopWrapper.setVisibility(View.VISIBLE);
        } else {
            flTopWrapper.setVisibility(View.GONE);
        }

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_allowance_detail;
    }

    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void initTableData() {
        int textSizePx = getResources().getDimensionPixelSize(R.dimen.table_text_size);
        int tablePadding = getResources().getDimensionPixelSize(R.dimen.table_padding);
        tablePadding = 0;
        FontStyle fontStyle = new FontStyle(textSizePx, ContextCompat.getColor(this, R.color.black_text_color));
        table.getConfig().setContentStyle(fontStyle);
        table.getConfig().setColumnTitleStyle
                (new FontStyle(textSizePx, getResources().getColor(R.color.seleted_text_color)));
        table.getConfig().setHorizontalPadding(tablePadding);
        //普通列
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
        table.getConfig().setShowTableTitle(false);
        table.getConfig().setColumnTitleHorizontalPadding(tablePadding);
        //table.getConfig().setHorizontalPadding(tablePadding);

        Column<String> column1 = new Column<>("日期", "date");
        column1.setWidth(calculateTableWidth(71));

        Column<String> column2 = new Column<>("好友名称", "friendName");
        column2.setWidth(calculateTableWidth(89));

        //Column<String> column3 = new Column<>("联系方式", "contactMethod");
        //column3.setWidth(calculateTableWidth(16));

        Column<String> column4 = new Column<>("已支付金额 ", "payMoney");
        column4.setWidth(calculateTableWidth(86));

        Column<String> column5 = new Column<>("奖励 资产 ", "property");
        column5.setWidth(calculateTableWidth(93));

        List<TableBean> tableBeanList = new ArrayList<>();
        TableBean bean = new TableBean("2018-08",
                "迈凯思", "联系方式", "支付金额", "Y25000");
        tableBeanList.add(bean);
        TableData<TableBean> tableData = new TableData<TableBean>("allowance_table", tableBeanList, column1, column2, column4, column5);
        table.setTableData(tableData);
    }

    public int totalValue = 334;

    public int calculateTableWidth(int weight) {
        int width = ScreenUtils.getScreenWidth(this) - DensityUtils.dip2px(this, 43);
        return width * weight / totalValue;
    }


    /**
     * 设置文字内容
     */
    public void setContentData() {
        if (tvContent == null) {
            return;
        }
        String[] counts = getResources().getStringArray(R.array.counts);
        String[] moneys = getResources().getStringArray(R.array.moneys);
        String[] months = getResources().getStringArray(R.array.months);
        SpannableString spannableString;
        int textSize = (int) getResources().getDimension(R.dimen.share_detail_text);
        MineSpan mineSpan;
        for (int i = 0; i < counts.length; i++) {
            String text
                    = "分享" + counts[i] + "台，车补" + moneys[i] + "元/月，最高可领" + months[i] + "\n";
            spannableString = new SpannableString(text);
            mineSpan = new MineSpan((int) (textSize + DensityUtils.getDensity(this) * i));
            spannableString.setSpan(mineSpan, 7, 7 + moneys.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvContent.append(spannableString);
        }
    }


    public class MineSpan extends AbsoluteSizeSpan {

        public MineSpan(int size) {
            super(size);
        }

        public MineSpan(int size, boolean dip) {
            super(size, dip);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.seleted_text_color));
        }
    }


}
