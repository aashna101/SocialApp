package com.example.pavilion.socialapp;

public class StatusView {

    public String image;
    public String background_color;
    public String tag;
    public String text;
    public String user_id;
    public String day_date_time;


    public StatusView(String img, String background_color, String tag, String text, String day_date_time, String user_id) {

        this.image = img;
        this.background_color = background_color;
        this.tag = tag;
        this.text = text;
        this.day_date_time=day_date_time;
        this.user_id=user_id;
       // this.user_id=uid;

    }

    public StatusView() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDay_date_time() {
        return day_date_time;
    }

    public void setDay_date_time(String day_date_time) {
        this.day_date_time = day_date_time;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
