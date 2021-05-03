package com.uptune.Helper;

import java.net.URL;

public class CardContainer {
    URL image;
    String title, id, popularity;

    public CardContainer(String title, URL image, String id) {
        this.image = image;
        this.title = title;
        this.id = id;
    }

    public CardContainer(String title, URL image, String id,String popularity) {
        this.image = image;
        this.title = title;
        this.id = id;
        this.popularity= popularity;
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

    public String getPopularity() {
        return id;
    }

    public void setPopularity(String title) {
        this.id = id;
    }

}
