package com.example.lob.UI.cooking;

public class CookingObject {
    private String title;
    private String img_url;
    private String detail_link;

    public CookingObject(String title, String img_url, String detail_link) {
        this.title = title;
        this.img_url = img_url;
        this.detail_link = detail_link;
    }

    public String getTitle() {
        return title;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getDetail_link() {
        return detail_link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setDetail_link(String detail_link) {
        this.detail_link = detail_link;
    }
}
