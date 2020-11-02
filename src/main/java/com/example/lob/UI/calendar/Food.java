package com.example.lob.UI.calendar;

import android.media.Image;

import java.util.Date;

public class Food {
    int food_id;
    String user_email;
    String food_name;
    Date eDate;
    int rDate;
    int info;
    Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Food(String food_name){
        this.food_name = food_name;
    }

    public Food(String food_name, Date eDate) {
        this.food_name = food_name;
        this.eDate = eDate;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public Date getEDate() {
        return eDate;
    }

    public void setEDate(Date eDate) {
        this.eDate = eDate;
    }

    public int getRDate() {
        return rDate;
    }

    public void setRDate(int rDate) {
        this.rDate = rDate;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }


}
