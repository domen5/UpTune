package com.uptune.Adapter;

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

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.FeatureViewHolder> {

    ArrayList<CardContainer> location;
    int type;
    int lastPos = -1;

    public CardAdapter(ArrayList<CardContainer> location, int type) {
        this.type = type;
        this.location = location;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_categories, parent, false);
        FeatureViewHolder fvh = new FeatureViewHolder(v);
        switch (this.type) {
            case 2:
                v.setOnClickListener(e -> {
                    int position = fvh.getAdapterPosition();
                    // Toast.makeText(parent.getContext(), "TEST1 " + fvh.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.catalog, new CategoriesDetails(location.get(position).getTitle(), location.get(position).getImage(), location.get(position).getID()))
                            .addToBackStack("a")
                            .commit();
                });
                break;
            case 3:
                v.setOnClickListener(e -> {
                    int position = fvh.getAdapterPosition();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.categories_details, new CardDetails(location.get(position).getTitle(), location.get(position).getImage(), location.get(position).getID()))
                            .addToBackStack("a")
                            .commit();
                });
                break;

        }
        return fvh;
    }


    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        holder.itemView.clearAnimation();

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(holder.context,
                (position > lastPos) ?
                        R.anim.card_animation_caller :
                        R.anim.card_animation_null);
        holder.itemView.startAnimation(animation.getAnimation());

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
    }


    @Override
    public int getItemCount() {
        return location.size();
    }


    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, desc;
        RatingBar popularity;
        Context context;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.catTitle);
            desc = itemView.findViewById(R.id.artist_desc);

            popularity = itemView.findViewById(R.id.artist_rating);
        }
    }
}
