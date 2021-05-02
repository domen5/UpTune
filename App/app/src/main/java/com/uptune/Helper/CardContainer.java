package com.uptune.Helper;

import java.net.URL;

public class CardContainer {
    URL image;
    String title;

    public CardContainer(URL image, String title) {
        this.image = image;
        this.title = title;
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

}
