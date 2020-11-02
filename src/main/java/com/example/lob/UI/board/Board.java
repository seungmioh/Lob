package com.example.lob.UI.board;

public class Board {
    String notice;
    String name;
    String date;
    int id;

    public Board(String notice, String name, String date, int id) {
        this.notice = notice;
        this.name = name;
        this.date = date;
        this.id = id;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
