package com.example.lob.DTO;

import java.util.Date;

public class FoodDTO {
    private int no;
    private String user;
    private String recordDate;
    private String expirationDate;
    private String food_name;
    private long reaminDate;

    public FoodDTO(String user, String food_name, String expirationDate) {
        this.user = user;
        this.food_name = food_name;
        this.expirationDate = expirationDate;
    }
    public FoodDTO(String user, String food_name, String expirationDate, long reaminDate) {
        this.user = user;
        this.food_name = food_name;
        this.expirationDate = expirationDate;
        this.reaminDate = reaminDate;
    }


    public FoodDTO(String food_name, String expirationDate) {
        this.food_name = food_name;
        this.expirationDate = expirationDate;
    }

    public FoodDTO() {

    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getReaminDate() {
        return reaminDate;
    }

    public void setReaminDate(long reaminDate) {
        this.reaminDate = reaminDate;
    }
}
