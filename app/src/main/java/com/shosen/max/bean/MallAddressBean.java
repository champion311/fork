package com.shosen.max.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MallAddressBean implements Parcelable {

    private boolean isDefault;
    private String detailedAddress;
    private String name;
    private String mobile;
    private String id;

    private String province;
    private String city;
    private String area;
    private String address;


    public MallAddressBean() {

    }

    protected MallAddressBean(Parcel in) {
        isDefault = in.readByte() != 0;
        detailedAddress = in.readString();
        province = in.readString();
        city = in.readString();
        area = in.readString();
        address = in.readString();
        name = in.readString();
        mobile = in.readString();
        id = in.readString();
    }

    public static final Creator<MallAddressBean> CREATOR = new Creator<MallAddressBean>() {
        @Override
        public MallAddressBean createFromParcel(Parcel in) {
            return new MallAddressBean(in);
        }

        @Override
        public MallAddressBean[] newArray(int size) {
            return new MallAddressBean[size];
        }
    };

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isDefault ? 1 : 0));
        dest.writeString(detailedAddress);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(area);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeString(id);
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Creator<MallAddressBean> getCREATOR() {
        return CREATOR;
    }
}
