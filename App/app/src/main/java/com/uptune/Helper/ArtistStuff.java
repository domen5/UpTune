package com.uptune.Helper;

import java.net.URL;

public class ArtistStuff {

    String title, id;
    URL img;

    public ArtistStuff(String title, String id, URL img) {
        this.title = title;
        this.img = img;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getID() {
        return id;
    }

    public void setID(String title) {
        this.id = id;
    }

    public URL getImage() {
        return img;
    }

    public void setImage(URL image) {
        this.img = image;
    }


}
