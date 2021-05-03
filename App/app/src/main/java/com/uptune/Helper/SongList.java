package com.uptune.Helper;

import java.net.URL;

public class SongList {
    URL image;
    String title, id;

    public SongList(String title, URL image, String id) {
        this.image = image;
        this.title = title;
        this.id = id;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
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

}
