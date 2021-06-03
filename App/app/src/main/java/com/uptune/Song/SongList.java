package com.uptune.Song;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.Comparator;

public class SongList implements Comparable<SongList> {

    String title, id, artists;
    URL img;
    Bitmap bitmap;

    public SongList(String title, String id, URL img, String artists) {
        this.title = title;
        this.id = id;
        this.img = img;
        this.artists = artists;
    }

    public SongList(String title, String id, URL img, Bitmap bitmap) {
        this.title = title;
        this.id = id;
        this.img = img;
        this.bitmap = bitmap;
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public int compareTo(SongList o) {
        return 0;
    }

    public static Comparator<SongList> sort = (o1, o2) -> o1.getTitle().compareTo(o2.getTitle());
}
