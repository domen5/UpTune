package com.uptune.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.R;
import com.uptune.Song.SongDetails;
import com.uptune.Song.SongList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.FeatureViewHolder> {

    ArrayList<SongList> location;
    URL img;
    private int fragmentId;

    public SongAdapter(ArrayList<SongList> location, int fragmentId) {
        this.location = location;
        this.fragmentId = fragmentId;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.charts_card, parent, false);
        FeatureViewHolder fvh = new FeatureViewHolder(v);
        v.setOnClickListener(e -> {
            int position = fvh.getAdapterPosition();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(fragmentId, SongDetails.newInstance(location.get(position).getId()))
                    .addToBackStack(null)
                    .commit();
        });
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        SongList obj = location.get(position);
        holder.title.setText(obj.getTitle());
        holder.artist.setText(obj.getArtists());
        try {
            if (obj.getImg() != null) {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) obj.getImg().getContent());
                holder.img.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return location.size();
    }


    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        ImageView img;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.trackNameView);
            img = itemView.findViewById(R.id.coverImageView);
            artist = itemView.findViewById(R.id.trackArtistView);
        }
    }
}
