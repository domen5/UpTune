package com.uptune.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.Catalog.CategoriesDetails;
import com.uptune.R;

import java.io.IOException;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.FeatureViewHolder> {

    ArrayList<SongList> location;

    public SongAdapter(ArrayList<SongList> location) {
        this.location = location;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_card, parent, false);
        FeatureViewHolder fvh = new FeatureViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        SongList fetchBest = location.get(position);
        holder.title.setText(fetchBest.getTitle());
    }

    @Override
    public int getItemCount() {
        return location.size();
    }


    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.catTitle);
        }
    }
}
