package com.uptune.Helper;

import java.net.URL;

public class CardContainer {
    URL image;
    String title, id, popularity, date, artist, tracks;

    public CardContainer(String title, URL image, String id) {
        this.image = image;
        this.title = title;
        this.id = id;
    }

    public CardContainer(String title, URL image, String id, String popularity) {
        this.image = image;
        this.title = title;
        this.id = id;
        this.popularity = popularity;
    }

    public CardContainer(String title, URL image, String id, String artist, String date, String tracks) {
        this.image = image;
        this.title = title;
        this.id = id;
        this.artist = artist;
        this.date = date;
        this.tracks = tracks;
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
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTracks() {
        return tracks;
    }

    public void setTracks(String tracks) {
        this.tracks = tracks;
    }
}
