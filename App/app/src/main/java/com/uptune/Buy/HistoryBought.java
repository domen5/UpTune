package com.uptune.Buy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryBought {

    public String name, price, type, img, date;

    public HistoryBought(String name, String price, String img, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.img = img;
        this.date= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }


    @Override
    public String toString() {
        return "HistoryBought{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", type='" + type + '\'' +
                ", img='" + img + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
