package com.uptune.Chart;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChartItem {
    private String name;
    private List<String> artists;
    private URL image;

    public ChartItem(String name, URL image, String...artists) {
        this.name = name;
        this.image = image;
        this.artists = new ArrayList<String>();
        for(String s: artists){
            this.artists.add(s);
        }
    }

    public ChartItem(String name, URL image, List<String> artists) {
        this.name = name;
        this.image = image;
        this.artists = artists;
    }

    public String getName() {
        return this.name;
    }

    public URL getImage() {
        return  this.image;
    }

    public List<String> getArtists() {
        return Collections.unmodifiableList(this.artists);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getArtistsString() {
        return this.artists.stream().collect(Collectors.joining(","));
    }

}
