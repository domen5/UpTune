package com.uptune.Adapter.Card;

import android.graphics.Bitmap;

import com.uptune.Used.UsedElement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class HistoryElement extends UsedElement {
    public static final String USED_ALBUM = "used";
    public static final String DIGITAL_ALBUM = "digital";
    public static final String SONG = "song";

    private String date;
    private String type;
    private int color;
    private Bitmap imageBitmap;

    public void setDate(String date) { this.date = date; }
    public String getDate() { return this.date; }
    public void setType(String type) { this.type = type; }
    public String getType() { return this.type; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public static Comparator<HistoryElement> dateComparator = (o1, o2) -> {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
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

//
//        import java.net.MalformedURLException;
//        import java.net.URL;
//        import java.util.Comparator;
//
//public class UsedElement implements Comparable<com.uptune.Used.UsedElement> {
//    String artist, description, label, manuf, name, price, user, id;
//    URL img;
//
//    public UsedElement() {
//    }
//
//    public UsedElement(String artist, String description, String img, String label, String manuf, String name, String price, String user) throws MalformedURLException {
//        this.artist = artist;
//        this.description = description;
//        this.label = label;
//        this.manuf = manuf;
//        this.name = name;
//        this.price = price;
//        this.img = new URL(img);
//        this.user = user;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public String getUser() {
//        return user;
//    }
//
//    public void setUser(String user) {
//        this.user = user;
//    }
//
//    public String getArtist() {
//        return artist;
//    }
//
//    public void setArtist(String artist) {
//        this.artist = artist;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public void setLabel(String label) {
//        this.label = label;
//    }
//
//    public String getManuf() {
//        return manuf;
//    }
//
//    public void setManuf(String manuf) {
//        this.manuf = manuf;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public URL getImg() {
//        return img;
//    }
//
//    public void setImg(String img) throws MalformedURLException {
//        this.img = new URL(img);
//    }
//
//    @Override
//    public int compareTo(com.uptune.Used.UsedElement e) {
//        return (int) Math.round(Double.parseDouble(this.price) - Double.parseDouble(e.getPrice()));
//    }
//
//    public static Comparator<com.uptune.Used.UsedElement> comparator = (o1, o2) -> o1.getName().compareTo(o2.getName());
//    public static Comparator<com.uptune.Used.UsedElement> comparatorZ = (o1, o2) -> o2.getName().compareTo(o1.getName());
//
//    public static Comparator<com.uptune.Used.UsedElement> usercomparator = (o1, o2) -> o1.getUser().compareTo(o2.getUser());
//
//}
