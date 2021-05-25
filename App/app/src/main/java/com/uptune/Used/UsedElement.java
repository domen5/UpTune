package com.uptune.Used;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;

public class UsedElement implements Comparable<UsedElement> {
    String artist, description, label, manuf, name, price, user, id;
    URL img;

    public UsedElement() {
    }

    public UsedElement(String artist, String description, String img, String label, String manuf, String name, String price, String user) throws MalformedURLException {
        this.artist = artist;
        this.description = description;
        this.label = label;
        this.manuf = manuf;
        this.name = name;
        this.price = price;
        this.img = new URL(img);
        this.user = user;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getManuf() {
        return manuf;
    }

    public void setManuf(String manuf) {
        this.manuf = manuf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public URL getImg() {
        return img;
    }

    public void setImg(String img) throws MalformedURLException {
        this.img = new URL(img);
    }

    @Override
    public int compareTo(UsedElement e) {
        return (int) Math.round(Double.parseDouble(this.price) - Double.parseDouble(e.getPrice()));
    }

    public static Comparator<UsedElement> comparator = (o1, o2) -> o1.getName().compareTo(o2.getName());
    public static Comparator<UsedElement> comparatorZ = (o1, o2) -> o2.getName().compareTo(o1.getName());

    public static Comparator<UsedElement> usercomparator = (o1, o2) -> o1.getUser().compareTo(o2.getUser());

}
