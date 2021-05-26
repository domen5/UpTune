package com.uptune.Artist;

import java.net.URL;

public class ArtistStuff {

    private String title, id;
    private URL img;
    private final int type;
    public final static int ALBUM = 0;
    public final static int TRACK = 1;

    public ArtistStuff(String title, String id, URL img) {
        this(title, id, img, ALBUM);
    }

    public ArtistStuff(String title, String id, URL img, int type) {
        this.title = title;
        this.img = img;
        this.id = id;
        this.type = type;
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

    public int getType() { return this.type; }
}
