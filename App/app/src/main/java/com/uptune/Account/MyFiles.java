package com.uptune.Account;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uptune.Adapter.UserSongAdapter;
import com.uptune.R;
import com.uptune.SessionAccount;
import com.uptune.Song.SongList;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

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


        final FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        ArrayList<SongList> setSongOwned = new ArrayList<>();
        DatabaseReference owned = rootNode.getReference("owned").child(SessionAccount.getUsername());
        owned.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    storageReference = firebaseStorage.getInstance().getReference(d.getValue().toString());
                    storageReference.listAll().addOnSuccessListener(result -> {
                        for (StorageReference fileRef : result.getItems()) {
                            Task<Uri> urlTask = fileRef.getDownloadUrl();
                            urlTask.addOnSuccessListener(uri -> {
                                try {
                                    String url = urlTask.getResult().toString();
                                    String tmp = url.substring(url.lastIndexOf('/') + 1);
                                    String fileName = tmp.substring(0, tmp.lastIndexOf('.'));
                                    fileName = java.net.URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());
                                    fileName = fileName.substring(d.getValue().toString().length() + 3);

                                    setSongOwned.add(new SongList(fileName, url, new URL(url), ""));
                                    RecyclerView rv = findViewById(R.id.user_songs_recycler);
                                    rv.setHasFixedSize(true);
                                    rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    adapter = new UserSongAdapter(setSongOwned, getApplicationContext());
                                    Collections.sort(setSongOwned, SongList.sort);
                                    rv.setAdapter(adapter);
                                } catch (MalformedURLException | UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "FAIL", Toast.LENGTH_LONG).show());
                }
            }

            @Override
            public void onCancelled( @NotNull DatabaseError error) {
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        this.adapter.releaseMediaPlayer();
    }
}