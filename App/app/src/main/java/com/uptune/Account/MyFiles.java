package com.uptune.Account;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uptune.Adapter.Card.HistoryElement;
import com.uptune.Adapter.UserSongAdapter;
import com.uptune.R;
import com.uptune.SessionAccount;
import com.uptune.Song.SongList;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MyFiles extends AppCompatActivity {
    FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    StorageReference ref;
    UserSongAdapter adapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);

        //toolbar event
        final Toolbar toolbar = findViewById(R.id.toolbar_my_files);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        //Get from db
//        final FirebaseAuth auth = FirebaseAuth.getInstance(); // non era utilizzato
        final FirebaseDatabase rootNode = FirebaseDatabase.getInstance();

        final DatabaseReference owned = rootNode.getReference("history").child(SessionAccount.getUsername());
        owned.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    // TODO: Magari cambiare HistoryElement in qualcos'altro
                    HistoryElement el = d.getValue(HistoryElement.class);

                    //TODO: salvare da qualche parte come string statica
                    if (el.getType().equalsIgnoreCase("album")) {
                        //TODO: gestire album
                        // recuperare ed aggiungere tutte le canzoni dell'album?
                    }
                    if (el.getType().equalsIgnoreCase("song")) {
                        //TODO: gestire song
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("ERROR", "Owned items are not retrievable");
                Log.e("ERROR", error.getMessage());
            }
        });
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
                        rv.setLayoutManager(new LinearLayoutManager(this));
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