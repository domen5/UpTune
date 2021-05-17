package com.uptune.Chart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChartItem {
    private String id;
    private String name;
    private List<String> artists;
    private URL imageUrl;
    private Bitmap imageFile;

    public ChartItem(String id, String name, URL image, String...artists) {
        this.id = id;
        this.name = name;
        this.imageUrl = image;
        this.artists = new ArrayList<String>();
        for(String s: artists){
            this.artists.add(s);
        }
    }

    public ChartItem(String id, String name, URL image, List<String> artists) {
        this.id = id;
        this.name = name;
        this.imageUrl = image;
        this.artists = artists;
    }

    public void fetchImage() throws IOException {
        this.imageFile = BitmapFactory.decodeStream(this.imageUrl.openConnection().getInputStream());
    }

    public String getId() { return  this.id; }

    public Bitmap getImageFile() { return imageFile; }

    public String getName() { return this.name; }

    public URL getImage() { return  this.imageUrl; }

    public List<String> getArtists() { return Collections.unmodifiableList(this.artists); }

    public String getArtistsString() {
        String line = "";
        for(String a : artists) { line += a + ", "; }
        return line.substring(0, line.length() - 2);
    }

}
