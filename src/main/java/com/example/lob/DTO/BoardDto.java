package com.example.lob.DTO;

import java.util.Date;

public class BoardDto {
    private String board_title;
    private String board_contents;
    private String board_writer;
    private int board_id;
    private Date board_date;

    public String getBoard_title() {
        return board_title;
    }

    public void setBoard_title(String board_title) {
        this.board_title = board_title;
    }

    public String getBoard_contents() {
        return board_contents;
    }

    public void setBoard_contents(String board_contents) {
        this.board_contents = board_contents;
    }

    public String getBoard_writer() {
        return board_writer;
    }

    public void setBoard_writer(String board_writer) {
        this.board_writer = board_writer;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public Date getBoard_date() {
        return board_date;
    }

    public void setBoard_date(Date board_date) {
        this.board_date = board_date;
    }
}
