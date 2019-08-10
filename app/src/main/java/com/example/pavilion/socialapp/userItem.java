package com.example.pavilion.socialapp;

public class userItem {
    String back_pic;
    String display_name;
    String phone;
     String status;

 public userItem() {
    }

    public userItem(String back_pic, String display_name, String phone, String status) {
        this.back_pic = back_pic;
        this.display_name = display_name;
        this.phone = phone;
        this.status = status;
    }


    public String getBack_pic() {
        return back_pic;
    }


    public String getDisplay_name() {
        return display_name;
    }


    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }


}
