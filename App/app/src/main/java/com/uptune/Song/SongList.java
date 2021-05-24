package com.uptune.Song;

import java.net.URL;

public class SongList {

    String title, id, artists;
    URL img;

    public SongList(String title, String id, URL img, String artists) {
        this.title = title;
        this.id = id;
        this.img = img;
        this.artists = artists;
    }

    public SongList(String title, String id) {
        this.title = title;
        this.id = id;
        this.img = null;
        this.artists = null;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public URL getImg() {
        return img;
    }

    public void setImg(URL img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
