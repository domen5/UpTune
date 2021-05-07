package com.uptune.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.Artist.ArtistStuff;
import com.uptune.R;
import com.uptune.Web;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.FeatureViewHolder> {

    ArrayList<ArtistStuff> location;

    public ArtistAdapter(ArrayList<ArtistStuff> location) {
        this.location = location;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_artist_details, parent, false);
        FeatureViewHolder fvh = new FeatureViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        ArtistStuff artistStuff = location.get(position);
        holder.title.setText(artistStuff.getTitle());
        Bitmap image = null;
        try {
            image = BitmapFactory.decodeStream(artistStuff.getImage().openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.img.setImageBitmap(image);
    }

    @Override
    public int getItemCount() {
        return location.size();
    }


    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_artist);
            title = itemView.findViewById(R.id.artist_elem_title);
        }
    }
}
