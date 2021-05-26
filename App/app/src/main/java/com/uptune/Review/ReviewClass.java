package com.uptune.Review;

import com.uptune.SessionAccount;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReviewClass {
    public String name, rate, desc, date, img;

    public ReviewClass(String username, String rate, String desc) {
        this.name = username;
        this.rate = rate;
        this.desc = desc;
        this.img = SessionAccount.geImg();
        this.date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    @Override
    public String toString() {
        return "ReviewClass{" +
                "name='" + name + '\'' +
                ", rate='" + rate + '\'' +
                ", desc='" + desc + '\'' +
                ", date='" + date + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}