package com.uptune.Adapter.Card;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.Artist.ArtistDetails;
import com.uptune.Helper.CardContainer;
import com.uptune.R;
import com.uptune.Web;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class CardArtistAdapter extends RecyclerView.Adapter<com.uptune.Adapter.Card.CardArtistAdapter.FeatureViewHolder> {

    ArrayList<CardContainer> location;
    int lastPos = -1;

    public CardArtistAdapter(ArrayList<CardContainer> location) {
        this.location = location;
    }

    @NonNull
    @Override
    public com.uptune.Adapter.Card.CardArtistAdapter.FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_artist, parent, false);
        com.uptune.Adapter.Card.CardArtistAdapter.FeatureViewHolder fvh = new com.uptune.Adapter.Card.CardArtistAdapter.FeatureViewHolder(v);
        v.setOnClickListener(e -> {
            int position = fvh.getAdapterPosition();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            try {
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.catalog, new ArtistDetails(location.get(position).getTitle(), location.get(position).getImage(), location.get(position).getID(), Web.getArtistSummaryLastFm(location.get(position).getTitle()))).addToBackStack("a").commit();
            } catch (IOException | JSONException ioException) {
                ioException.printStackTrace();
            }
        });
        return fvh;
    }


    @Override
    public void onBindViewHolder(@NonNull com.uptune.Adapter.Card.CardArtistAdapter.FeatureViewHolder holder, int position) {
        holder.itemView.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(holder.context,
                (position > lastPos) ?
                        R.anim.card_animation_fade_scroll :
                        R.anim.card_animation_null);
        holder.itemView.startAnimation(animation);
        lastPos = position;
        Bitmap image = null;
        CardContainer fetchBest = location.get(position);
        try {
            image = BitmapFactory.decodeStream(fetchBest.getImage().openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.img.setImageBitmap(image);
        holder.name.setText(fetchBest.getTitle());
        try {
            holder.desc.setText(Web.getArtistSummaryLastFm(fetchBest.getTitle()));
            int convertPop = Integer.parseInt(fetchBest.getPopularity()) / 20;
            holder.popularity.setRating(convertPop);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return location.size();
    }

    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, desc;
        RatingBar popularity;
        Context context;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            img = itemView.findViewById(R.id.card_artist_img);
            name = itemView.findViewById(R.id.card_review_name);
            desc = itemView.findViewById(R.id.card_review_desc);
            popularity = itemView.findViewById(R.id.card_review_rating);
        }
    }
}