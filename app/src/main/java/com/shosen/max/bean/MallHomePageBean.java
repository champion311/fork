package com.shosen.max.bean;

import com.shosen.max.bean.mall.GoodsBean;

import java.util.List;

public class MallHomePageBean {

    private List<MallBannerBean> banner;

    private List<FloorGoodsList> floorGoodsList;

    public List<MallBannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<MallBannerBean> banner) {
        this.banner = banner;
    }

    public List<FloorGoodsList> getFloorGoodsList() {
        return floorGoodsList;
    }

    public void setFloorGoodsList(List<FloorGoodsList> floorGoodsList) {
        this.floorGoodsList = floorGoodsList;
    }

    public class FloorGoodsList {

        private String name;

        private String id;

        private List<GoodsBean> goodsList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<GoodsBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsBean> goodsList) {
            this.goodsList = goodsList;
        }
    }


    public class MallBannerBean extends BaseBannerBean {
        private String id;//1,
        private String name;// 合作 谁是你的菜;,
        private String link;//
        //private String url;// http://yanxuan.nosdn.127.net/65091eebc48899298171c2eb6696fe27.jpg;,
        private String position;//1,
        private String content;// 合作 谁是你的菜;,
        private boolean enabled;//true,
        private String addTime;// 2018-02-01 00:00:00;,
        private String updateTime;// 2018-02-01 00:00:00;,
        private boolean deleted;//false

        public MallBannerBean(String url) {
            super(url);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
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
