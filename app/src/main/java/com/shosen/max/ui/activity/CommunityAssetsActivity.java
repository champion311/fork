package com.shosen.max.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.TableBean;
import com.shosen.max.presenter.CommunityAssetsPresenter;
import com.shosen.max.presenter.contract.CommunityAssertsContract;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.ScreenUtils;
import com.shosen.max.utils.StatusBarUtil;

import java.util.List;

import butterknife.BindView;

public class CommunityAssetsActivity extends BaseActivity implements CommunityAssertsContract.View {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_top_wrapper)
    FrameLayout flTopWrapper;
    @BindView(R.id.tv_community_assets_money)
    TextView tvCommunityAssetsMoney;
    @BindView(R.id.table)
    SmartTable table;
    private CommunityAssetsPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new CommunityAssetsPresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.seleted_text_color));
        initTableData();
        initTopHeader();
        mPresenter.getOrdersList();
        if (LoginUtils.isLogin) {
            tvCommunityAssetsMoney.setText("￥"+LoginUtils.getUser().getxProperty());
        }

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_community_assets;
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

    }

    public int totalValue = 334;

    public int calculateTableWidth(int weight) {
        int width = ScreenUtils.getScreenWidth(this) - DensityUtils.dip2px(this, 43);
        return width * weight / totalValue;
    }

    @Override
    public void showProList(List<TableBean> mData) {
        for (TableBean bean : mData) {
            if (bean != null) {
                bean.init();
            }
        }
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

        TableData<TableBean> tableData = new TableData<TableBean>("allowance_table", mData, column1, column2, column4, column5);
        table.setTableData(tableData);
    }

    @Override
    public void showErrorMessage(Throwable throwable) {

    }

    @Override
    public void initTopHeader() {
        FrameLayout flTop = (FrameLayout) findViewById(R.id.fl_top_wrapper);
        if (flTop != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) flTop.getLayoutParams();
            layoutParams.topMargin =
                    StatusBarUtil.getStatusBarHeight(this);
            flTop.setLayoutParams(layoutParams);
        }
    }
}
