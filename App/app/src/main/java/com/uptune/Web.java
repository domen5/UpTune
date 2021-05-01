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

public class Web{

    private static String token;

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        token = br.lines().collect(Collectors.joining());
        Log.i("TOKEN", token);

        token = token.split("[},:{]")[2];
        Log.i("TOKEN", token);
        http.disconnect();
    }
    public static String getToken() {
        return token;
    }
}