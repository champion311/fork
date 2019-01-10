package com.shosen.max.ui.mall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bin.david.form.data.form.IForm;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.MallAddressBean;
import com.shosen.max.presenter.AddressEditPresenter;
import com.shosen.max.presenter.contract.AddressContract;
import com.shosen.max.utils.ClipboardUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressEditActivity extends BaseActivity implements AddressContract.EditView {


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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.tv_cellphone)
    TextView tvCellphone;
    @BindView(R.id.ed_cellphone)
    EditText edCellphone;
    @BindView(R.id.tv_zone)
    TextView tvZone;
    @BindView(R.id.ed_zone)
    TextView edZone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ed_address)
    EditText edAddress;
    @BindView(R.id.cb_is_default)
    CheckBox cbIsDefault;
    @BindView(R.id.tv_del_address)
    TextView tvDelAddress;
    private MallAddressBean bean;

    private boolean isAdd = true;

    private AddressEditPresenter mPresenter;

    private CityPickerView mPicker;

    private String provinceText;

    private String cityText;

    private String zoneText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new AddressEditPresenter();
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.home_page_color));
        initCityDialog();
        ivHeadTitle.setText("地址修改");
        rightText.setVisibility(View.VISIBLE);
        rightText.setText("保存");
        rightText.setTextColor(ContextCompat.getColor(mContext, R.color.right_text_color));
        if (getIntent() != null) {
            bean = (MallAddressBean) getIntent().getParcelableExtra("address");
            isAdd = bean == null;

            if (isAdd) {
                tvDelAddress.setVisibility(View.GONE);
            } else {
                provinceText = bean.getProvince();
                cityText = bean.getCity();
                zoneText = bean.getArea();
                edName.setText(bean.getName());
                edCellphone.setText(bean.getMobile());
                edZone.setText(bean.getProvince() + " " + bean.getCity() + " " + bean.getArea());
                edAddress.setText(bean.getAddress());
                cbIsDefault.setChecked(bean.isDefault());
            }
        }
        String title = isAdd ? "添加新地址" : "修改地址";
        ivHeadTitle.setText(title);

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_address_edit;
    }


    @OnClick({R.id.iv_back, R.id.right_text, R.id.tv_del_address, R.id.ed_zone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.right_text:
                //提交
                addOrModifyAddress();
                break;
            case R.id.tv_del_address:
                if (bean == null) {
                    return;
                }
                mPresenter.delAddress(bean.getId());
                break;
            case R.id.ed_zone:
                ClipboardUtils.hideSoftKeyBoard(this);
                if (mPicker != null) {
                    mPicker.showCityPicker();
                }
                break;
        }
    }


    //    id,
//    String name,
//    String mobile,
//    String provinceId,
//    String cityId, String areaId, String address, boolean isDefault
    public void addOrModifyAddress() {
        String id = isAdd ? "0" : bean.getId();
        String name = edName.getText().toString().trim();
        String mobile = edCellphone.getText().toString().trim();
        String address = edAddress.getText().toString().trim();
        boolean isDefault = cbIsDefault.isChecked();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.show(this, "请输入收货人姓名");
            return;
        }

        if (!RegexUtils.isMobileExact(mobile)) {
            ToastUtils.show(this, "请输入正确的手机号");
            return;
        }

        if (TextUtils.isEmpty(provinceText) || TextUtils.isEmpty(cityText) || TextUtils.isEmpty(zoneText)) {
            ToastUtils.show(this, "请选择地址");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            ToastUtils.show(this, "请输入详细地址");
            return;
        }
        mPresenter.addOrModifyAddress(id, name, mobile, provinceText, cityText, zoneText, address, isDefault);

    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        ToastUtils.show(this, throwable.toString());
    }

    @Override
    public void addOrModifyBack() {
        String text = isAdd ? "添加" : "修改";
        ToastUtils.show(this, text + "成功");
        finish();
    }

    @Override
    public void delAddressBack() {
        ToastUtils.show(this, "删除成功");
        finish();
    }


    /**
     * 初始化城市弹窗
     */
    public void initCityDialog() {
        mPicker = new CityPickerView();
        mPicker.init(this);
        @SuppressLint("ResourceType") CityConfig cityConfig = new
                CityConfig.Builder().province("北京市").city("北京市").district("朝阳区").
                setLineColor(getResources().getString(R.color.diver_color)).
                title("选择省市").titleBackgroundColor(getResources().getString(R.color.white)).
                drawShadows(false).
                build();
        mPicker.setConfig(cityConfig);
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                super.onSelected(province, city, district);
                if (province != null && city.getName() != null) {
                    edZone.setText(province.getName() + " " + city.getName() + " " + district.getName());
                    provinceText = province.getName();
                    cityText = city.getName();
                    zoneText = district.getName();
                }
            }
        });
    }
}
