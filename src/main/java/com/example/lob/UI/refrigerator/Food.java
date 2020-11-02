package com.example.lob.UI.refrigerator;

import android.media.Image;

import java.util.Date;

public class Food {
    public Food(int food_id, String user_email, String food_name, Date food_expirationDate, long food_remainDate) {
        this.food_id = food_id;
        this.user_email = user_email;
        this.food_name = food_name;
        this.food_expirationDate = food_expirationDate;
        this.food_remainDate = food_remainDate;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public Date getFood_expirationDate() {
        return food_expirationDate;
    }

    public void setFood_expirationDate(Date food_expirationDate) {
        this.food_expirationDate = food_expirationDate;
    }

    public long getFood_remainDate() {
        return food_remainDate;
    }

    public void setFood_remainDate(long food_remainDate) {
        this.food_remainDate = food_remainDate;
    }

    public int getFood_info() {
        return food_info;
    }

    public void setFood_info(int food_info) {
        this.food_info = food_info;
    }

    public Image getFood_image() {
        return food_image;
    }

    public void setFood_image(Image food_image) {
        this.food_image = food_image;
    }


    private int food_id;
    private String user_email;
    private String food_name;
    private Date food_expirationDate;
    private long food_remainDate;
    private int food_info;
    private Image food_image;


}
