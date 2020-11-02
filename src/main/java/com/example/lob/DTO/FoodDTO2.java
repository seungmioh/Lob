package com.example.lob.DTO;

import java.util.Date;

public class FoodDTO2 {
    private int food_id;
    private String user_email;
    private String food_recordDate;
    private Date food_expirationDate;
    private String food_name;
    private long food_remainDate;

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

    public String getFood_recordDate() {
        return food_recordDate;
    }

    public void setFood_recordDate(String food_recordDate) {
        this.food_recordDate = food_recordDate;
    }

    public Date getFood_expirationDate() {
        return food_expirationDate;
    }

    public void setFood_expirationDate(Date food_expirationDate) {
        this.food_expirationDate = food_expirationDate;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public long getFood_remainDate() {
        return food_remainDate;
    }

    public void setFood_remainDate(long food_remainDate) {
        this.food_remainDate = food_remainDate;
    }


}
