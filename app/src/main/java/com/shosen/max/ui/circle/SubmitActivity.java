package com.shosen.max.ui.circle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.FileUploadResponse;
import com.shosen.max.constant.Contstants;
import com.shosen.max.others.span.GlideImageLoader;
import com.shosen.max.presenter.MessageSubmitPresenter;
import com.shosen.max.presenter.contract.MessageContract;
import com.shosen.max.ui.circle.adapter.ImagePickerAdapter;
import com.shosen.max.utils.LongitudeUtils;
import com.shosen.max.utils.SpanUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.dialog.SelectPhotoDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class SubmitActivity extends BaseActivity
        implements ImagePickerAdapter.OnRecyclerViewItemClickListener,
        SelectPhotoDialog.SelectorDialogItemClickListener,
        MessageContract.SubmitView {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.ed_submit)
    EditText edSubmit;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_swift_location)
    TextView tvSwiftLocation;

    private String locationDes;


    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数

    private SelectPhotoDialog selectPhotoDialog;

    private MessageSubmitPresenter mPresenter;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private RxPermissions rxPermissions;
    //定位需要权限
    public static final String[] permissions = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private LongitudeUtils longitudeUtils;

    private boolean isLocateAble = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new MessageSubmitPresenter(this);
        setPresenter(mPresenter);
        initBaiduLocation();
        rxPermissions = new RxPermissions(this);
        //TEST ONLY
        longitudeUtils = new LongitudeUtils();
        longitudeUtils.initBaiduLocation(this, myListener);
        super.onCreate(savedInstanceState);
    }

    public void initBaiduLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setScanSpan(0);//只定位一次
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void initViewAndEvents() {
        //initImagePicker();
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.home_page_color));
        tvHeadTitle.setText("动态");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("发布");
        changeState(false);
        initImagePicker();
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);
        edSubmit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (hasImage()) {
                    return;
                }
                if (s.length() > 0) {
                    changeState(true);
                } else {
                    changeState(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (hasImage()) {
                    return;
                }
                if (s.length() > 0) {
                    changeState(true);
                } else {
                    changeState(false);
                }
            }
        });

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_publish;
    }

    private boolean hasImage() {
        return selImageList != null && selImageList.size() != 0;
    }

    private boolean hasText() {
        return !TextUtils.isEmpty(edSubmit.getText().toString().trim());
    }


    @OnClick({R.id.iv_back, R.id.tv_right, R.id.tv_swift_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //TODO
                String content = edSubmit.getText().toString().trim();
                if (content.length() >= 200) {
                    content = content.substring(200);
                }
                //对 emoji表情进行编码
                //content = SpanUtils.getUrlEncodeContent(content);
                String location = locationDes;
                if (hasImage()) {
                    File[] files = new File[selImageList.size()];
                    for (int i = 0; i < selImageList.size(); i++) {
                        files[i] = new File(selImageList.get(i).path);
                    }
                    //mPresenter.uploadImagesAndSubmit(content, location, files);
                    mPresenter.uploadImagesAndSubmitOneTime(content, location, files);
                } else {
                    //不传图片
                    if (TextUtils.isEmpty(content)) {
                        ToastUtils.show(this, "");
                    }
                    mPresenter.submitMessage(content, location, "");
                }
                break;
            case R.id.tv_swift_location:
                isLocateAble = !isLocateAble;
                if (isLocateAble) {
                    tvSwiftLocation.setText("隐藏位置");
                    if (mLocationClient.isStarted()) {
                        mLocationClient.restart();
                        return;
                    }
                    //定位权限动态申请
                    Disposable di = rxPermissions.request(permissions).subscribe(grant -> {

                        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
                            ToastUtils.show(SubmitActivity.this, "请打开位置开关");
                        } else {
                            mLocationClient.start();
                        }
                        //longitudeUtils.start();
                    }, throwable -> {

                    });
                    mPresenter.addSubscribe(di);

                } else {
                    tvLocation.setText("所在位置");
                    tvSwiftLocation.setText("显示位置");
                }
                break;
            default:
                break;
        }
    }


    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    /**
     * 显示头像弹窗
     */
    public void showPhotoDialog() {
        if (selectPhotoDialog == null) {
            selectPhotoDialog = new SelectPhotoDialog(this, R.style.CancelDialog);
            selectPhotoDialog.showTitle(false);
            selectPhotoDialog.setOwnerActivity(this);
            selectPhotoDialog.setItemClickListener(this);
        } else {
            selectPhotoDialog.dismiss();
        }
        selectPhotoDialog.show();
    }


    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case Contstants.IMAGE_ITEM_ADD:
                showPhotoDialog();
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, Contstants.REQUEST_CODE_PREVIEW);
                break;
        }

    }

    @Override
    public void onDialogItemClick(View view, int id) {
        switch (view.getId()) {
            case R.id.tv_take_photo:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(this, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                startActivityForResult(intent, Contstants.REQUEST_CODE_SELECT);

                break;
            case R.id.tv_album:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent1 = new Intent(this, ImageGridActivity.class);
                /* 如果需要进入选择的时候显示已经选中的图片，
                 * 详情请查看ImagePickerActivity
                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                startActivityForResult(intent1, Contstants.REQUEST_CODE_SELECT);
                break;
            default:
                break;
        }
    }

    ArrayList<ImageItem> images = null;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == Contstants.REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                    if (!hasText()) {
                        //没有文字的情况下
                        if (selImageList.size() > 0) {
                            changeState(true);
                        } else {
                            changeState(false);
                        }
                    }
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == Contstants.REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    if (!hasText()) {
                        //没有文字的情况下
                        if (selImageList.size() > 0) {
                            changeState(true);
                        } else {
                            changeState(false);
                        }
                    }
                    adapter.setImages(selImageList);
                }
            }
        }
    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
    }

    @Override
    public void upLoadFileSuccess(FileUploadResponse response) {

    }

    /**
     * 提交成功
     *
     * @param response
     */
    @Override
    public void submitSuccess(BaseResponse response) {
        if (response != null && !TextUtils.isEmpty(response.getMsg())) {
            ToastUtils.show(this, response.getMsg());
            finish();
        }
    }


    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //ToastUtils.show(SubmitActivity.this,location.getCod);
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            //ToastUtils.show(SubmitActivity.this, addr);
            if (!TextUtils.isEmpty(location.getLocationDescribe()) && isLocateAble) {
                locationDes = location.getLocationDescribe();
                tvLocation.setText(location.getLocationDescribe());
                //ToastUtils.show(SubmitActivity.this, location.getLocationDescribe());
            }
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            int errorCode = location.getLocType();
        }
    }

    /**
     * 改变发布按钮的状态
     *
     * @param isClickable
     */
    public void changeState(boolean isClickable) {
        if (tvRight == null) {
            return;
        }
        tvRight.setClickable(isClickable);
        if (isClickable) {
            tvRight.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            tvRight.setTextColor(ContextCompat.getColor(this, R.color.c999));
        }
    }
}
