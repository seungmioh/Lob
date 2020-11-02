package com.example.lob.UI.board;

public class Comment {
    int comment_id;
    int board_id;
    String comment_contents;
    String comment_writer;
    String comment_date;

    public Comment(String comment_contents, String comment_writer, String date_text,int comment_id) {
        this.comment_contents = comment_contents;
        this.comment_writer = comment_writer;
        this.comment_date = date_text;
        this.comment_id = comment_id;
    }


    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public String getComment_content() {
        return comment_contents;
    }

    public void setComment_content(String comment_contents) {
        this.comment_contents = comment_contents;
    }

    public String getComment_writer() {
        return comment_writer;
    }

    public void setComment_writer(String comment_writer) {
        this.comment_writer = comment_writer;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }
}
