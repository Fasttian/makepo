package com.catikco.makepo.admin.banner.model;

/**
 * Author: Cai Rong fei
 * Date: 2018-08-17 10:29
 * Description：
 */
public class BannerListPageModel {

    private Integer id;
    private String title;
    private String description;
    private String bannerCreateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBannerCreateTime() {
        return bannerCreateTime;
    }

    public void setBannerCreateTime(String bannerCreateTime) {
        this.bannerCreateTime = bannerCreateTime;
    }
}
