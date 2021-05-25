package com.uptune.Account;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.uptune.Adapter.CardAdapter;
import com.uptune.Adapter.UserSongAdapter;
import com.uptune.Helper.CardContainer;
import com.uptune.R;
import com.uptune.Song.SongList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MyFiles extends AppCompatActivity {
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference ref;
    UserSongAdapter adapter;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth auth;
    Toolbar toolbar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);
        toolbar = findViewById(R.id.toolbar_my_files);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        //Get from db
        auth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("owned").child("leleshady");
        ArrayList<SongList> setSongOwned = new ArrayList<>();


        storageReference = firebaseStorage.getInstance().getReference("Shoot the stars aim for the moon/");
        storageReference.listAll().addOnSuccessListener(result -> {
            for (StorageReference fileRef : result.getItems()) {
                Task<Uri> urlTask = fileRef.getDownloadUrl();
                urlTask.addOnSuccessListener(uri -> {
                    try {
                        String url = urlTask.getResult().toString();
                        String tmp = url.substring(url.lastIndexOf('/') + 1);
                        String fileName = tmp.substring(0, tmp.lastIndexOf('.'));
                        fileName = java.net.URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());
                        fileName = fileName.substring("Shoot the stars aim for the moon/".length() + 3);
                        setSongOwned.add(new SongList(fileName, url, new URL(url), ""));
                        RecyclerView rv = findViewById(R.id.user_songs_recycler);
                        rv.setHasFixedSize(true);
                        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                        adapter = new UserSongAdapter(setSongOwned, this);
                        rv.setAdapter(adapter);
                    } catch (MalformedURLException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "FAIL", Toast.LENGTH_LONG).show());
    }

    private void download() {
        storageReference = firebaseStorage.getInstance().getReference("Shoot the stars aim for the moon/");
        ref = storageReference.child("01 Bad Bitch from Tokyo (Intro).m4a");
        ref.getDownloadUrl().addOnSuccessListener(uri -> {
            String url = uri.toString();
            downloadFiles(MyFiles.this, "01 Bad Bitch from Tokyo (Intro)", ".m4a", DIRECTORY_DOWNLOADS, url);
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_LONG).show();
        });
    }

    private void downloadFiles(Context context, String fileName, String ext, String dest, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        req.setDestinationInExternalFilesDir(context, dest, fileName + ext);
        downloadManager.enqueue(req);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.adapter.releaseMediaPlayer();
    }
}