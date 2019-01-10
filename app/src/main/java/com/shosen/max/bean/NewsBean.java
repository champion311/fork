package com.shosen.max.bean;

public class NewsBean {

    private String id;//2,
    private String news_Title;// 首信天赐MaxMaker新能源汽车创新发展模式;,
    private String news_Picture;// http://api.shosen.cn:8080/file/download/1d605b6f-2356-4e1c-bc2d-442daa37d8aa.png;,
    private String news_ReadNum;//1438,
    private String linkUrl;// www.baidu.com;,
    private String createTime;// 1542095921375;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNews_Title() {
        return news_Title;
    }

    public void setNews_Title(String news_Title) {
        this.news_Title = news_Title;
    }

    public String getNews_Picture() {
        return news_Picture;
    }

    public void setNews_Picture(String news_Picture) {
        this.news_Picture = news_Picture;
    }

    public String getNews_ReadNum() {
        return news_ReadNum;
    }

    public void setNews_ReadNum(String news_ReadNum) {
        this.news_ReadNum = news_ReadNum;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
