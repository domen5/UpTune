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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uptune.R;

import java.io.IOException;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MyFiles extends AppCompatActivity {
    TextView currentTime, totalTime;
    SeekBar seekBar;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference ref;
    private Handler handler = new Handler();
    MediaPlayer mediaPlayer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);
        Button btn = findViewById(R.id.btn_download);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.player_seek);
        Button btnPlay = findViewById(R.id.btn_play);
        Button btnStop = findViewById(R.id.btn_stop);
        mediaPlayer = new MediaPlayer();
        btn.setOnClickListener(v -> download());
        seekBar.setMax(100);
        btnPlay.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                handler.removeCallbacks(updater);
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
                updateSeek();
            }
        });

        try {
            prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        seekBar.setOnTouchListener((v, event) -> {
            SeekBar seekBar = (SeekBar) v;
            int pos = mediaPlayer.getDuration() / 100 * seekBar.getProgress();
            mediaPlayer.seekTo(pos);
            currentTime.setText(millisToTimer(mediaPlayer.getCurrentPosition()));
            return false;
        });
    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeek();
            long currentDuration = mediaPlayer.getCurrentPosition();
            currentTime.setText(millisToTimer(currentDuration));
        }
    };

    private void updateSeek() {
        if (mediaPlayer.isPlaying()) {
            seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }

    private String millisToTimer(long millis) {
        String time = "";
        String secStr;
        int hours = (int) (millis / (1000 * 3600));
        int min = (int) (millis % (1000 * 3600)) / (1000 * 60);
        int sec = (int) ((millis % (1000 * 3600)) % (1000 * 60) / 1000);
        if (hours > 0)
            time = hours + ":";
        if (sec < 10)
            secStr = "0" + sec;
        else
            secStr = "" + sec;
        time = time + min + ":" + secStr;
        Log.i("TIME", time);
        return time;
    }

    private void prepare() throws IOException {
        mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/uptune-2d37c.appspot.com/o/Shoot%20the%20stars%20aim%20for%20the%20moon%2F01%20Bad%20Bitch%20from%20Tokyo%20(Intro).m4a?alt=media&token=5417b3b9-be65-4776-896d-09d4f9790f8d");
        mediaPlayer.prepare();
        totalTime.setText(millisToTimer(mediaPlayer.getDuration()));
        updateSeek();
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