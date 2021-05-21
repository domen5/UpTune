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
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class UserSongAdapter extends RecyclerView.Adapter<UserSongAdapter.FeatureViewHolder> {

    Context context;
    ArrayList<SongList> location;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference ref;
    MediaPlayer mediaPlayer;
    int state = -1;
    private Handler handler = new Handler();
    FeatureViewHolder playingHolder;

    public UserSongAdapter(ArrayList<SongList> location, Context context) {
        this.location = location;
        this.context = context;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playingHolder.btnPlay.setImageResource(R.drawable.ic_music_play);
            }
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

            // PAUSE
            if (mediaPlayer.isPlaying() && (state == position)) {
                Log.d("media", "PAUSE");
                handler.removeCallbacksAndMessages(null);
                mediaPlayer.pause();
                holder.btnPlay.setImageResource(R.drawable.ic_music_play);
                return;
            }

            // PLAY
            if (!mediaPlayer.isPlaying() && (state != position)) {
                Log.d("media", "PLAY");
                if (playingHolder != null) {
                    // playingHolder.makeStopPlayeing()
                    playingHolder.btnPlay.setImageResource(R.drawable.ic_music_play);
                    Log.d("media", "CHANGE");
                }

                try {
                    prepare(holder);
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                playingHolder = holder;
                state = position;
                updateSeek(holder);
                holder.btnPlay.setImageResource(R.drawable.ic_music_pause);
                return;
            }

            // RESUME
            if(!mediaPlayer.isPlaying() && (state == position)) {
                Log.d("media", "RESUME");
                int pos = mediaPlayer.getDuration() / 100 * holder.seekBar.getProgress();
                holder.currentTime.setText(millisToTimer(mediaPlayer.getCurrentPosition()));
                mediaPlayer.start();
                mediaPlayer.seekTo(pos);
                holder.btnPlay.setImageResource(R.drawable.ic_music_pause);
//                updateSeek(holder);
                return;
            }

            // CHANGE
            if(mediaPlayer.isPlaying() && state != position) {
                Log.d("media", "CHANGE");
                mediaPlayer.reset();
                playingHolder.btnPlay.setImageResource(R.drawable.ic_music_play);
                playingHolder.seekBar.setProgress(0);

                try {
                    prepare(holder);
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                playingHolder = holder;
                state = position;
                updateSeek(holder);
                holder.btnPlay.setImageResource(R.drawable.ic_music_pause);
                return;
            }
        });

        final FeatureViewHolder myHolder = holder;
        holder.seekBar.setOnTouchListener((v, event) -> {
            SeekBar seekBar = (SeekBar) v;
            int pos = mediaPlayer.getDuration() / 100 * seekBar.getProgress();
            myHolder.currentTime.setText(millisToTimer(mediaPlayer.getCurrentPosition()));
            if(myHolder == playingHolder) {
                mediaPlayer.seekTo(pos);
            }
            return false;
        });
        holder.btnDownload.setOnClickListener(v -> download());
    }

    public void releaseMediaPlayer() {
        this.mediaPlayer.release();
    }

    private void updateSeek(FeatureViewHolder holder) {
        if (mediaPlayer.isPlaying()) {
            holder.seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(() -> {
                long currentDuration = mediaPlayer.getCurrentPosition();
                holder.currentTime.setText(millisToTimer(currentDuration));
            }, 1000);
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

    private void prepare(FeatureViewHolder holder) throws IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/uptune-2d37c.appspot.com/o/Shoot%20the%20stars%20aim%20for%20the%20moon%2F01%20Bad%20Bitch%20from%20Tokyo%20(Intro).m4a?alt=media&token=5417b3b9-be65-4776-896d-09d4f9790f8d");
        mediaPlayer.prepare();
        holder.totalTime.setText(millisToTimer(mediaPlayer.getDuration()));
        updateSeek(holder);
    }

    private void download() {
        storageReference = firebaseStorage.getInstance().getReference("Shoot the stars aim for the moon/");
        ref = storageReference.child("01 Bad Bitch from Tokyo (Intro).m4a");
        ref.getDownloadUrl().addOnSuccessListener(uri -> {
            String url = uri.toString();
            downloadFiles(this.context, "01 Bad Bitch from Tokyo (Intro)", ".m4a", DIRECTORY_DOWNLOADS, url);
        }).addOnFailureListener(e -> {
            Toast.makeText(this.context, "NO", Toast.LENGTH_LONG).show();
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
