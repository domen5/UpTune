package com.uptune.Used;

import java.net.MalformedURLException;
import java.net.URL;

public class UsedElement {
    String artist, description, label, manuf, name, price;
    URL img;

    public UsedElement() {
    }

    public UsedElement(String artist, String description, String img, String label, String manuf, String name, String price) throws MalformedURLException {
        this.artist = artist;
        this.description = description;
        this.label = label;
        this.manuf = manuf;
        this.name = name;
        this.price = price;
        this.img = new URL(img);
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
}
