package com.uptune.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uptune.R;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MyFiles extends AppCompatActivity {

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);
        Button btn = findViewById(R.id.btn_download);
        btn.setOnClickListener(v -> download());
    }

    private void download() {
        storageReference = firebaseStorage.getInstance().getReference();
        ref = storageReference.child("a.pdf");
        ref.getDownloadUrl().addOnSuccessListener(uri -> {
            String url = uri.toString();
            downloadFiles(MyFiles.this, "a", ".pdf", DIRECTORY_DOWNLOADS, url);
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
}