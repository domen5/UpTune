package com.uptune.Adapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uptune.Account.MyFiles;
import com.uptune.R;
import com.uptune.Song.SongList;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class UserSongAdapter extends RecyclerView.Adapter<UserSongAdapter.FeatureViewHolder> {
    Context context;
    ArrayList<SongList> location;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference ref;
    MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable updateSeek;
    FeatureViewHolder playingHolder;
    int state = -1;

    public UserSongAdapter(ArrayList<SongList> location, Context context) {
        this.location = location;
        this.context = context;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> {
            if (this.updateSeek != null)
                handler.removeCallbacks(this.updateSeek);
            mp.reset();
            handler.postDelayed(() -> {
                playingHolder.btnPlay.setImageResource(R.drawable.ic_music_play);
                playingHolder.seekBar.setProgress(0);
                playingHolder.currentTime.setText(millisToTimer(0));
            }, 1000);
        });
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_myfiles, parent, false);
        FeatureViewHolder fvh = new FeatureViewHolder(v);
        return fvh;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        SongList songList = location.get(position);
        holder.title.setText(songList.getTitle());

        holder.btnPlay.setOnClickListener(v -> {

            // PLAY
            if (!mediaPlayer.isPlaying() && (state != position)) {
                Log.d("media", "PLAY");
                if (playingHolder != null) {
                    playingHolder.btnPlay.setImageResource(R.drawable.ic_music_play);
                    Log.d("media", "CHANGE");
                }
                playingHolder = holder;
                state = position;

                try {
                    prepare(holder, songList.getImg());
                    int pos = mediaPlayer.getDuration() / 100 * holder.seekBar.getProgress();
                    mediaPlayer.start();
                    mediaPlayer.seekTo(pos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateSeek();
                holder.btnPlay.setImageResource(R.drawable.ic_music_pause);
                return;
            }

            // PAUSE
            if (mediaPlayer.isPlaying() && (state == position)) {
                Log.d("media", "PAUSE");
                handler.removeCallbacksAndMessages(null);
                mediaPlayer.pause();
                holder.btnPlay.setImageResource(R.drawable.ic_music_play);
                return;
            }

            // RESUME
            if (!mediaPlayer.isPlaying() && (state == position)) {
                Log.d("media", "RESUME");
                int pos = mediaPlayer.getDuration() / 100 * holder.seekBar.getProgress();
                holder.currentTime.setText(millisToTimer(mediaPlayer.getCurrentPosition()));
                mediaPlayer.start();
                mediaPlayer.seekTo(pos);
                holder.btnPlay.setImageResource(R.drawable.ic_music_pause);
                updateSeek();
                return;
            }

            // CHANGE
            if (mediaPlayer.isPlaying() && state != position) {
                Log.d("media", "CHANGE");
                mediaPlayer.reset();
                playingHolder.btnPlay.setImageResource(R.drawable.ic_music_play);

                try {
                    prepare(holder, songList.getImg());
                    int pos = mediaPlayer.getDuration() / 100 * holder.seekBar.getProgress();
                    mediaPlayer.start();
                    mediaPlayer.seekTo(pos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                playingHolder = holder;
                state = position;
                holder.btnPlay.setImageResource(R.drawable.ic_music_pause);
                return;
            }
        });

        holder.seekBar.setOnTouchListener((v, event) -> {
            SeekBar seekBar = (SeekBar) v;
            int pos = mediaPlayer.getDuration() / 100 * seekBar.getProgress();
            holder.currentTime.setText(millisToTimer(mediaPlayer.getCurrentPosition()));
            if (holder == playingHolder) {
                mediaPlayer.seekTo(pos);
            }
            return false;
        });
        holder.btnDownload.setOnClickListener(v -> download(songList.getTitle(), songList.getImg()));
    }

    public void releaseMediaPlayer() {
        this.handler.removeCallbacks(this.updateSeek);
        this.mediaPlayer.release();
    }

    private void updateSeek() {
        if (this.updateSeek != null) {
            handler.removeCallbacks(this.updateSeek);
        }
        this.updateSeek = () -> {
            double totalDuration = mediaPlayer.getDuration();
            double currentDuration = mediaPlayer.getCurrentPosition();

            // Updating progress bar
            int progress = (int) (currentDuration * 100.0 / totalDuration);
            playingHolder.seekBar.setProgress(progress);
            playingHolder.currentTime.setText(millisToTimer(mediaPlayer.getCurrentPosition()));

            // Running this thread after 100 milliseconds
            handler.postDelayed(updateSeek, 100);
        };
        handler.postDelayed(this.updateSeek, 100);
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
        Log.i("timerOne", time);
        return time;
    }

    private void prepare(FeatureViewHolder holder, URL url) throws IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(url.toString());
        mediaPlayer.prepare();
        holder.totalTime.setText(millisToTimer(mediaPlayer.getDuration()));
    }

    private void download(String name, URL url) {

        downloadFiles(this.context, name, ".m4a", DIRECTORY_DOWNLOADS, url);
    }

    private void downloadFiles(Context context, String fileName, String ext, String dest, URL url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url.toString());
        DownloadManager.Request req = new DownloadManager.Request(uri);
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        req.setDestinationInExternalFilesDir(context, dest, fileName + ext);
        downloadManager.enqueue(req);
    }

    @Override
    public int getItemCount() {
        return location.size();
    }


    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        TextView title, currentTime, totalTime;
        ImageButton btnPlay, btnDownload;
        SeekBar seekBar;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.trackNameSong);
            currentTime = itemView.findViewById(R.id.current_time);
            totalTime = itemView.findViewById(R.id.total_time);
            seekBar = itemView.findViewById(R.id.player_seek);
            btnDownload = itemView.findViewById(R.id.btn_download);
            btnPlay = itemView.findViewById(R.id.btn_play);
            seekBar.setMax(100);
        }
    }
}
