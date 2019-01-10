package com.shosen.max.bean.mall;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CartList  implements Parcelable{
    private int id;///16,
    private int userId;//1,
    private int goodsId;//1110016,
    private String goodsSn;//"1110016",
    private String goodsName;//"天然硅胶宠物除毛按摩刷",
    private int productId;//150,
    private double price;//39,
    private int number;//1,
    private List<String> specifications;//["标准"
    //],
    private boolean checked;//,
    private String picUrl;//"http://yanxuan.nosdn.127.net/3bd73b7279a83d1cbb50c0e45778e6d6.png",
    private String addTime;//"2018-12-11 13:45:43",
    private String updateTime;//"2018-12-11 13:45:43",
    private boolean deleted;//false;
    private int count = 1;
    private boolean isSelected = false;
    private boolean isPointGoods = false;

    public CartList() {
    }


    protected CartList(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        goodsId = in.readInt();
        goodsSn = in.readString();
        goodsName = in.readString();
        productId = in.readInt();
        price = in.readDouble();
        number = in.readInt();
        specifications = in.createStringArrayList();
        checked = in.readByte() != 0;
        picUrl = in.readString();
        addTime = in.readString();
        updateTime = in.readString();
        deleted = in.readByte() != 0;
        count = in.readInt();
        isSelected = in.readByte() != 0;
        isPointGoods = in.readByte() != 0;
    }

    public static final Creator<CartList> CREATOR = new Creator<CartList>() {
        @Override
        public CartList createFromParcel(Parcel in) {
            return new CartList(in);
        }

        @Override
        public CartList[] newArray(int size) {
            return new CartList[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<String> specifications) {
        this.specifications = specifications;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isPointGoods() {
        return isPointGoods;
    }

    public void setPointGoods(boolean pointGoods) {
        isPointGoods = pointGoods;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(goodsId);
        dest.writeString(goodsSn);
        dest.writeString(goodsName);
        dest.writeInt(productId);
        dest.writeDouble(price);
        dest.writeInt(number);
        dest.writeStringList(specifications);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeString(picUrl);
        dest.writeString(addTime);
        dest.writeString(updateTime);
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeInt(count);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isPointGoods ? 1 : 0));
    }
}
