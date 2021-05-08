package com.uptune;

import android.os.StrictMode;
import android.util.Log;

import com.uptune.Chart.ChartItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Web {

    private static String token = "";
    private static List<ChartItem> bestSongsGlobal;
    private static List<ChartItem> bestSongsItaly;
    private static List<ChartItem> bestAlbumsGlobal;
    private static List<ChartItem> bestAlbumsItaly;

    public static void httpCall() throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = new URL("https://accounts.spotify.com/api/token");

        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Authorization", "Basic MzhiNmYxNTYxNzU1NGY0MjhjYTNkM2M1MzU4ZGVhYjQ6ODk0ZmMwMTc2MTlmNGFiYWFiN2FhOTJmZmZlNjVlZDg=");
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String data = "grant_type=client_credentials";
        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        OutputStream stream = http.getOutputStream();
        stream.write(out);
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String tmp = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tmp = br.lines().collect(Collectors.joining());
        }
        tmp = tmp.split("[},:{]")[2].replace("\"", "");
        if (tmp != token) {
            token = tmp;
        }
        Log.i("TOKEN", token);
        http.disconnect();
    }

    public static JSONArray getCategories() throws IOException, JSONException {
        URL url = new URL("https://api.spotify.com/v1/browse/categories?country=US&limit=10&offset=1");
        JSONObject obj = getJsonFromUrl(url);
        return obj.getJSONObject("categories").getJSONArray("items");
    }

    public static JSONArray getAllCategories() throws IOException, JSONException {
        URL url = new URL("https://api.spotify.com/v1/browse/categories?&limit=50&country=US&offset=1");
        JSONObject obj = getJsonFromUrl(url);
        return obj.getJSONObject("categories").getJSONArray("items");
    }


    public static JSONArray getCategoriesLastFm(String type) throws IOException, JSONException {
        URL url = new URL("https://ws.audioscrobbler.com/2.0/?method=tag.gettopalbums&tag=" + type + "&api_key=030333446fe36a7c6b24368071dd1579&format=json");
        JSONObject obj = getJsonFromLastFm(url);
        return obj.getJSONObject("albums").getJSONArray("album");
    }

    public static String getArtistSummaryLastFm(String name) throws IOException, JSONException {
        URL url = new URL("https://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + name + "&api_key=030333446fe36a7c6b24368071dd1579&format=json");
        JSONObject obj = getJsonFromLastFm(url);
        String artistBio = obj.getJSONObject("artist").getJSONObject("bio").getString("summary");
        int iend = artistBio.indexOf("<a href");
        String subString = "";
        if (iend != -1) {
            subString = artistBio.substring(0, iend); //this will give abc
        }
        return subString;
    }

    public static String getArtistContentLastFm(String name) throws IOException, JSONException {
        URL url = new URL("https://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + name + "&api_key=030333446fe36a7c6b24368071dd1579&format=json");
        JSONObject obj = getJsonFromLastFm(url);
        return obj.getJSONObject("artist").getJSONObject("bio").getString("content");
    }

    public static void initialize() {
        try {
            bestSongsGlobal = new ArrayList<>();
            fetchItems(new URL("https://api.spotify.com/v1/playlists/37i9dQZEVXbNG2KDcFcKOF"), bestSongsGlobal);
//            bestSongsItaly = new ArrayList<>();
//            fetchItems(new URL("https://api.spotify.com/v1/playlists/37i9dQZEVXbNG2KDcFcKOF"), bestSongsGlobal);
//            bestAlbumsGlobal = new ArrayList<>();
//            fetchItems(new URL("https://api.spotify.com/v1/playlists/37i9dQZEVXbNG2KDcFcKOF"), bestSongsGlobal);
//            bestAlbumsItaly = new ArrayList<>();
//            fetchItems(new URL("https://api.spotify.com/v1/playlists/37i9dQZEVXbNG2KDcFcKOF"), bestSongsGlobal);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void fetchItems(URL url, List<ChartItem> items) {
        try {
            // fetch JSON from Spotify
            JSONArray arr = getJsonFromUrl(url)
                    .getJSONObject("tracks")
                    .getJSONArray("items");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i).getJSONObject("track");

                // name
                String name = current.getString("name");
                JSONArray artists = current.getJSONArray("artists");

                // artists
                List<String> artistsList = new ArrayList<>();
                for (int a = 0; a < artists.length(); a++) {
                    String currentArtist = artists.getJSONObject(a).getString("name");
                    artistsList.add(currentArtist);
                }

                // image
                String stringUrl = current.getJSONObject("album")
                        .getJSONArray("images")
                        .getJSONObject(0)
                        .getString("url");
                //Log.d("charts", artistsList.get(0));
                Log.d("charts", stringUrl);
                URL image = new URL(stringUrl);
                ChartItem item = new ChartItem(name, image, artistsList);

                // download image
                item.fetchImage();

                //add item to the list
                items.add(item);
            }
        } catch (IOException | JSONException ex) {
            Log.e("ERROR", ex.getMessage());
        }
    }

    public static List<ChartItem> getTopTracksGlobal() {
        return bestSongsGlobal;
    }

    public static JSONArray getTopTracksItaly() throws IOException, JSONException {
        URL url = new URL("https://api.spotify.com/v1/playlists/37i9dQZEVXbJUPkgaWZcWG");
        JSONArray arr = getJsonFromUrl(url)
                .getJSONObject("tracks")
                .getJSONArray("items");
        return arr;

    }

    public static JSONArray getTopAlbumsGlobal() throws IOException, JSONException {
        URL url = new URL("https://api.spotify.com/v1/playlists/37i9dQZEVXbNG2KDcFcKOF");
        JSONArray arr = getJsonFromUrl(url)
                .getJSONObject("tracks")
                .getJSONArray("items");
        return arr;

    }

    public static JSONArray getTopAlbumsItaly() throws IOException, JSONException {
        URL url = new URL("https://api.spotify.com/v1/playlists/37i9dQZEVXbNG2KDcFcKOF");
        JSONArray arr = getJsonFromUrl(url)
                .getJSONObject("tracks")
                .getJSONArray("items");
        return arr;

    }

    public static JSONArray getAlbum(String id) throws IOException, JSONException {
        URL url = new URL("https://api.spotify.com/v1/albums/" + id + "/tracks?market=US");
        JSONObject obj = getJsonFromUrl(url);
        return obj.getJSONArray("items");
    }

    public static JSONArray getNewRelease() throws IOException, JSONException {
        URL url = new URL("https://api.spotify.com/v1/browse/new-releases");
        JSONObject obj = getJsonFromUrl(url);
        return obj.getJSONObject("albums").getJSONArray("items");
    }


    public static JSONArray getArtistId() throws IOException, JSONException {
        URL url = new URL("https://api.spotify.com/v1/recommendations?seed_artists=7dGJo4pcD2V6oG8kP0tJRR&seed_genres=rap&seed_tracks=4JNKmlZrxBwsdUVKakeU6G");
        JSONObject obj = getJsonFromUrl(url);
        JSONObject list1 = new JSONObject();
        String check[] = new String[20];
        JSONArray arr = new JSONArray();
        for (int i = 0; i < 20; i++) {
            if (Arrays.asList(check).contains(obj.getJSONArray("tracks").getJSONObject(i).getJSONArray("artists").getJSONObject(0).toString())) {
                i--;
                obj = getJsonFromUrl(url);
            } else {
                list1.put(i + "", obj.getJSONArray("tracks").getJSONObject(i).getJSONArray("artists").getJSONObject(0));
                check[i] = obj.getJSONArray("tracks").getJSONObject(i).getJSONArray("artists").getJSONObject(0).toString();
            }
        }
        arr.put(list1);
        //Log.i("TOKEN", arr.toString());
        return arr;
    }

    public static JSONObject getArtist(String id) throws IOException, JSONException {
        URL url = new URL("https://api.spotify.com/v1/artists/" + id);
        JSONObject obj = getJsonFromUrl(url);
        return obj;
    }


    public static JSONArray getArtistStuff(String id) throws IOException, JSONException {
        URL urlTracks = new URL("https://api.spotify.com/v1/artists/" + id + "/top-tracks?market=US");
        JSONObject obj2 = getJsonFromUrl(urlTracks);
        URL urlAlbum = new URL("https://api.spotify.com/v1/artists/" + id + "/albums?market=US&limit=10");
        JSONObject obj = getJsonFromUrl(urlAlbum);
        JSONArray arr = new JSONArray();
        //album
        arr.put(obj.getJSONArray("items"));
        //track
        arr.put(obj2.getJSONArray("tracks"));
        return arr;
    }

    public static String getIdFromName(String type) throws IOException, JSONException {
        URL url = new URL("https://api.spotify.com/v1/search?query=" + URLEncoder.encode(type, String.valueOf(StandardCharsets.UTF_8)) + "&type=album&limit=1");
        JSONObject obj = getJsonFromUrl(url);
        return obj.getJSONObject("albums").getJSONArray("items").getJSONObject(0).getString("id");
    }

    public static JSONObject getJsonFromUrl(URL url) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JSONObject obj = null;
        String tmp = null;
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Authorization", "Bearer " + token);
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tmp = br.lines().collect(Collectors.joining());
        }
        try {
            obj = new JSONObject(tmp);
        } catch (JSONException err) {
            Log.d("Error", err.toString());
        }
        http.disconnect();
        return obj;
    }

    public static JSONObject getJsonFromLastFm(URL url) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JSONObject obj = null;
        String tmp = null;
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestProperty("Authorization", "Bearer " + "030333446fe36a7c6b24368071dd1579");
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tmp = br.lines().collect(Collectors.joining());
        }
        try {
            obj = new JSONObject(tmp);

        } catch (JSONException err) {
            Log.d("Error", err.toString());
        }
        http.disconnect();
        return obj;
    }


    public static String getToken() {
        return token;
    }
}