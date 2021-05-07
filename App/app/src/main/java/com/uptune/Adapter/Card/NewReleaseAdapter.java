package com.uptune.Adapter.Card;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.Artist.ArtistDetails;
import com.uptune.Catalog.CardDetails;
import com.uptune.Catalog.CategoriesDetails;
import com.uptune.Helper.CardContainer;
import com.uptune.R;
import com.uptune.Web;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class NewReleaseAdapter extends RecyclerView.Adapter<com.uptune.Adapter.Card.NewReleaseAdapter.FeatureViewHolder> {

    ArrayList<CardContainer> location;
    int lastPos = -1;

    public NewReleaseAdapter(ArrayList<CardContainer> location) {
        this.location = location;
    }

    @NonNull
    @Override
    public com.uptune.Adapter.Card.NewReleaseAdapter.FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_new_release, parent, false);
        com.uptune.Adapter.Card.NewReleaseAdapter.FeatureViewHolder fvh = new com.uptune.Adapter.Card.NewReleaseAdapter.FeatureViewHolder(v);

        v.setOnClickListener(e -> {
            int position = fvh.getAdapterPosition();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.catalog, new CardDetails(location.get(position).getTitle(), location.get(position).getImage(), location.get(position).getID()))
                    .addToBackStack("a")
                    .commit();
        });
        return fvh;
    }


    @Override
    public void onBindViewHolder(@NonNull com.uptune.Adapter.Card.NewReleaseAdapter.FeatureViewHolder holder, int position) {
        holder.itemView.clearAnimation();
        lastPos = position;
        Bitmap image = null;
        CardContainer fetchBest = location.get(position);
        try {
            image = BitmapFactory.decodeStream(fetchBest.getImage().openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.img.setImageBitmap(image);
        holder.title.setText(fetchBest.getTitle());
        holder.artist.setText(fetchBest.getArtist());
        holder.date.setText(fetchBest.getDate());
        holder.tracks.setText("Tracks:"+fetchBest.getTracks());
    }


    @Override
    public int getItemCount() {
        return location.size();
    }


    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, artist, date, tracks;
        Context context;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            img = itemView.findViewById(R.id.new_release_img);
            title = itemView.findViewById(R.id.new_release_title);
            artist = itemView.findViewById(R.id.new_release_artist_name);
            date = itemView.findViewById(R.id.new_release_date);
            tracks= itemView.findViewById(R.id.new_release_tracks);

        }
    }
}
