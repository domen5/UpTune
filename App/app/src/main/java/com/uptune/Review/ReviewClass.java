package com.uptune.Review;

import com.uptune.SessionAccount;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ReviewClass implements Comparable<ReviewClass>{
    public String name, rate, desc, date, img, key, imgProduct, productName, artists;

    public ReviewClass() {
    }

    public ReviewClass(String username, String rate, String desc, String imgProduct, String productName, String artists) {
        this.name = username;
        this.rate = rate;
        this.desc = desc;
        this.img = SessionAccount.geImg();
        this.date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        this.productName = productName;
        this.imgProduct = imgProduct;
        this.artists = artists;
    }

    public String getKey() {
        return key;
    }

    public String getArtists() {
        return artists;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public String getImg() {
        return img;
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

    public void setId(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(ReviewClass e) {
        return (int) Math.round(Double.parseDouble(this.rate) - Double.parseDouble(e.getRate()));
    }
    public static Comparator<ReviewClass> dateComparatorNewest = (o1, o2) -> {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date d1 = df.parse(o1.getDate());
            Date d2 = df.parse(o2.getDate());
            return d2.compareTo(d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    };

    public static Comparator<ReviewClass> dateComparatorOldest = (o1, o2) -> {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date d1 = df.parse(o1.getDate());
            Date d2 = df.parse(o2.getDate());
            return d1.compareTo(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    };
}