package com.uptune.Helper;

import com.google.android.material.textfield.TextInputLayout;

import java.net.URL;

public class SellHelper {
    public String name, label, artist, description, price, manuf, img, user;

    public SellHelper(String name, String label, String artist, String description, String manuf, String price, String img, String user) {
        this.name = name;
        this.label = label;
        this.artist = artist;
        this.description = description;
        this.price = price;
        this.manuf = manuf;
        this.img = img;
        this.user = user;
    }
}
