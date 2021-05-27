package com.uptune.Adapter.Card;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class HistoryElement implements Comparable<HistoryElement> {
    private String date;
    private String type;
    private String price;
    private String id;
    private String name;
    private int color;
    private String img;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public static Comparator<HistoryElement> dateComparatorNewest = (o1, o2) -> {
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

    public static Comparator<HistoryElement> dateComparatorOldest = (o1, o2) -> {
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

    @Override
    public int compareTo(HistoryElement e) {
        return (int) Math.round(Double.parseDouble(this.price) - Double.parseDouble(e.getPrice()));
    }



}

