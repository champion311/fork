package com.shosen.max.bean;//

import com.shosen.max.bean.mall.CartList;
import com.shosen.max.utils.LoginUtils;

import java.util.ArrayList;
import java.util.List;

public class GoodsDetailBean {

    private List<SpecificationList> specificationList;

    private String shareImage;

    private List<ProductList> productList;

    private Info info;

    public List<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductList> productList) {
        this.productList = productList;
    }

    public List<SpecificationList> getSpecificationList() {
        return specificationList;
    }

    public void setSpecificationList(List<SpecificationList> specificationList) {
        this.specificationList = specificationList;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public class SpecificationList {
        private String name;//:"规格",
        private List<ValueList> valueList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ValueList> getValueList() {
            return valueList;
        }

        public void setValueList(List<ValueList> valueList) {
            this.valueList = valueList;
        }
    }

    public class ValueList {
        private String id;//166,
        private String goodsId;//1116011,
        private String specification;//"规格",
        private String value;//"标准",
        private String picUrl;//"",
        private String addTime;//"2018-02-01 00:00:00",
        private String updateTime;//"2018-02-01 00:00:00",
        private boolean deleted;//false

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getSpecification() {
            return specification;
        }

        public void setSpecification(String specification) {
            this.specification = specification;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
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
    }

    public class Info {
        private String id;//1110016,
        private String goodsSn;//"1110016",
        private String name;//"天然硅胶宠物除毛按摩刷",
        private String categoryId;//1017000,
        private String brandId;//0,
        private List<String> gallery;//["http://yanxuan.nosdn.127.net/4642e54295c5beac2129f351c6dfa79e.jpg"         ],
        private String keywords;//"",
        private String brief;//"顺滑平面，猫狗通用，去除死毛",
        private boolean isOnSale;//true,
        private String sortOrder;//12,
        private String picUrl;//"http://yanxuan.nosdn.127.net/3bd73b7279a83d1cbb50c0e45778e6d6.png",
        private String shareUrl;//"",
        private boolean isNew;//false,
        private boolean isHot;//false,
        private String unit;//"件",
        private String counterPrice;//59,
        private String retailPrice;//39,
        private String addTime;//"2018-02-01 00:00:00",
        private String updateTime;//"2018-02-01 00:00:00",
        private boolean deleted;//false,
        private String detail;//"商品详情"


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsSn() {
            return goodsSn;
        }

        public void setGoodsSn(String goodsSn) {
            this.goodsSn = goodsSn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public List<String> getGallery() {
            return gallery;
        }

        public void setGallery(List<String> gallery) {
            this.gallery = gallery;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public boolean isOnSale() {
            return isOnSale;
        }

        public void setOnSale(boolean onSale) {
            isOnSale = onSale;
        }

        public String getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public boolean isNew() {
            return isNew;
        }

        public void setNew(boolean aNew) {
            isNew = aNew;
        }

        public boolean isHot() {
            return isHot;
        }

        public void setHot(boolean hot) {
            isHot = hot;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getCounterPrice() {
            return counterPrice;
        }

        public void setCounterPrice(String counterPrice) {
            this.counterPrice = counterPrice;
        }

        public String getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(String retailPrice) {
            this.retailPrice = retailPrice;
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

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }


//         dest.writeInt(id);
//        dest.writeInt(userId);
//        dest.writeInt(goodsId);
//        dest.writeInt(goodsSn);
//        dest.writeString(goodsName);
//        dest.writeInt(productId);
//        dest.writeDouble(price);
//        dest.writeInt(number);
//        dest.writeStringList(specifications);
//        dest.writeByte((byte) (checked ? 1 : 0));
//        dest.writeString(picUrl);
//        dest.writeString(addTime);
//        dest.writeString(updateTime);
//        dest.writeByte((byte) (deleted ? 1 : 0));
//        dest.writeInt(count);
//        dest.writeByte((byte) (isSelected ? 1 : 0));

        public CartList convertToCartList(String productId, boolean isPointGoods) {
            CartList cartList = new CartList();
            cartList.setId(Integer.valueOf(this.id));
            if (LoginUtils.isLogin) {
                cartList.setUserId(Integer.valueOf(LoginUtils.getUser().getUid()));
            }
            cartList.setGoodsId(0);
            cartList.setGoodsSn(this.goodsSn);
            cartList.setGoodsName(this.name);
            cartList.setProductId(Integer.valueOf(productId));
            cartList.setPrice(Double.valueOf(this.retailPrice));
            cartList.setNumber(1);
            cartList.setSpecifications(new ArrayList<>());
            cartList.setChecked(true);
            cartList.setSelected(true);
            cartList.setChecked(true);
            cartList.setPicUrl(this.picUrl);
            cartList.setAddTime(this.addTime);
            cartList.setUpdateTime(this.updateTime);
            cartList.setDeleted(this.deleted);
            cartList.setCount(1);
            cartList.setSelected(true);
            cartList.setPointGoods(isPointGoods);
            return cartList;
        }
    }

    public class ProductList {
        private String id;
        private String goodsId;
        private List<String> specifications;
        private String price;
        private String number;  //库存
        private String url;
        private String addTime;
        private String updateTime;
        private boolean deleted;
//
//        "id":150,
//                "goodsId":1110016,
//                "specifications":[
//                "标准"
//                ],
//                "price":39.00,
//                "number":100,
//                "url":"http://yanxuan.nosdn.127.net/3bd73b7279a83d1cbb50c0e45778e6d6.png",
//                "addTime":"2018-02-01 00:00:00",
//                "updateTime":"2018-12-10 14:07:40",
//                "deleted":false


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public List<String> getSpecifications() {
            return specifications;
        }

        public void setSpecifications(List<String> specifications) {
            this.specifications = specifications;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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
    }

}
