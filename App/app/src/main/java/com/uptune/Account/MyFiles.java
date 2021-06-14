package com.uptune.Account;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;
import com.uptune.Adapter.UserSongAdapter;
import com.uptune.Helper.LoadingDialog;
import com.uptune.R;
import com.uptune.SessionAccount;
import com.uptune.Song.SongList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class MyFiles extends AppCompatActivity {
    FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    StorageReference ref;
    UserSongAdapter adapter;
    private LoadingDialog loading;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);
        RecyclerView rv = findViewById(R.id.user_songs_recycler);
        //toolbar event
        final Toolbar toolbar = findViewById(R.id.toolbar_my_files);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());


        this.loading = new LoadingDialog(this);
        this.loading.startLoadingAnimation();

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
                                    Bitmap bitmap = getBitmap(d.getValue().toString());
                                    setSongOwned.add(new SongList(fileName, url, new URL(url), bitmap));
                                    rv.setHasFixedSize(true);
                                    rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    adapter = new UserSongAdapter(setSongOwned, getApplicationContext());
                                    Collections.sort(setSongOwned, SongList.sort);
                                    rv.setAdapter(adapter);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "FAIL", Toast.LENGTH_LONG).show());
                }
                Handler handler = new Handler();
                handler.postDelayed(() -> loading.dismissLoadingDialog(), 2000);

            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
            }
        });
        FastScrollerView fastScrollerView = findViewById(R.id.fastscroller);
        FastScrollerThumbView fastScrollerThumbView = findViewById(R.id.fastscroller_thumb);
        char[] alphabet = "#abcdefghijklmnopqrstuvwxyz".toCharArray();

        fastScrollerView.setupWithRecyclerView(rv, (position) -> {
            if (position >= alphabet.length)
                return null;
            else
                return new FastScrollItemIndicator.Text(alphabet[position] + "");
        });
        fastScrollerThumbView.setupWithFastScroller(fastScrollerView);
    }

    private Bitmap getBitmap(String toString) throws IOException {
        switch (toString) {
            case "/Shoot the stars aim for the moon":
                return BitmapFactory.decodeStream((InputStream) new URL("https://images-na.ssl-images-amazon.com/images/I/61-xuSJrAPL._AC_SY355_.jpg").getContent());
            case "/Yela":
                return BitmapFactory.decodeStream((InputStream) new URL("https://m.media-amazon.com/images/I/71XEhJ5lWgL._SS500_.jpg").getContent());
            case "/Music to be murder by":
                return BitmapFactory.decodeStream((InputStream) new URL("https://www.marshallmathers.eu/public/images/media/WhatsAppImage2020-12-13at09.59.29.jpeg").getContent());
            case "/King's disease":
                return BitmapFactory.decodeStream((InputStream) new URL("https://images-na.ssl-images-amazon.com/images/I/71jJHiBlYPL._AC_SY450_.jpg").getContent());
            case "/Exodus":
                return BitmapFactory.decodeStream((InputStream) new URL("https://i1.wp.com/abokimusic.com/wp-content/uploads/2021/05/Hood-Blues-364x364-1.jpeg?fit=364%2C364&ssl=1").getContent());
            case "/Coti":
                return BitmapFactory.decodeStream((InputStream) new URL("https://images.genius.com/7a1f299b7eca7489a9ede64dfa29017a.300x300x1.png").getContent());
        }
        return null;
    }


    @Override
    protected void onStop() {
        super.onStop();
        this.adapter.releaseMediaPlayer();
    }
}