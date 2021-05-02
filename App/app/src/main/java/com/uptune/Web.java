package com.uptune;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Web {

    private static String token = "";


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
        http.disconnect();
        httpCall2();
    }

    public static void httpCall2() throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = new URL("https://api.spotify.com/v1/browse/categories?limit=10");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Authorization", "Bearer " + token);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String tmp = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tmp = br.lines().collect(Collectors.joining());
        }
        Log.i("Success", tmp.toString());

        http.disconnect();
    }


    public static String getToken() {
        return token;
    }
}