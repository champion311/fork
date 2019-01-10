package com.shosen.max.ui.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bin.david.form.data.form.IForm;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.MallAddressBean;
import com.shosen.max.presenter.AddressListPresenter;
import com.shosen.max.presenter.contract.AddressContract;
import com.shosen.max.ui.mall.adapter.AddressListAdapter;
import com.shosen.max.utils.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressListActivity extends BaseActivity
        implements AddressContract.View, AddressListAdapter.OnAddressClick {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_head_title)
    TextView ivHeadTitle;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.tv_no_goods_alert)
    TextView tvNoGoodsAlert;
    @BindView(R.id.rl_no_address_wrapper)
    RelativeLayout rlNoAddressWrapper;
    @BindView(R.id.rc_address_list)
    RecyclerView rcAddressList;
    @BindView(R.id.tv_add_address)
    TextView tvAddAddress;
    private AddressListPresenter mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new AddressListPresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.home_page_color));
        ivHeadTitle.setText("地址管理");
        rcAddressList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_address_manager;
    }

    @Override
    public void addressBack(List<MallAddressBean> mData) {
        if (mData == null || mData.size() == 0) {
            rlNoAddressWrapper.setVisibility(View.VISIBLE);
            rcAddressList.setVisibility(View.GONE);
            return;
        }
        rcAddressList.setVisibility(View.VISIBLE);
        rlNoAddressWrapper.setVisibility(View.GONE);
        AddressListAdapter adapter = new AddressListAdapter(this, mData);
        adapter.setOnAddressClick(this);
        rcAddressList.setAdapter(adapter);
        //不存在默认地址的时候 ，默认集合第一个变成默认的
//        for (MallAddressBean bean : mData) {
//            if (bean.isDefault()) {
//                return;
//            }
//        }
//        if (mData.size() > 0) {
//            mPresenter.modifyAddress(0, mData.get(0), true);
//        }
    }

    @Override
    public void showErrorMessage(Throwable throwable) {

    }

    @Override
    public void editAddressBack(int position, boolean isDefault) {
        finish();
        //mPresenter.getAddressList();
    }

    @Override
    public void onAddressClick(View view, int position, MallAddressBean bean) {
        //mPresenter.modifyAddress(position, bean, true);
        Intent intent = new Intent();
        intent.putExtra("address", bean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onEditClick(View view, int position, MallAddressBean bean) {
        Intent intent = new Intent(this, AddressEditActivity.class);
        intent.putExtra("address", bean);
        startActivity(intent);
    }

    @OnClick({R.id.iv_back, R.id.tv_add_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_address:
                Intent intent = new Intent(this, AddressEditActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void initTopHeader() {
        RelativeLayout rlTop = (RelativeLayout) findViewById(R.id.rl_top_header);
        if (rlTop != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rlTop.getLayoutParams();
            layoutParams.topMargin =
                    StatusBarUtil.getStatusBarHeight(this);
            rlTop.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onResume() {
        mPresenter.getAddressList();
        super.onResume();
    }
}
