package com.uptune.Helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.Catalog.CardDetails;
import com.uptune.Catalog.CategoriesDetails;
import com.uptune.MainActivity;
import com.uptune.R;

import java.io.IOException;
import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.FeatureViewHolder> {

    ArrayList<CardContainer> location;
    int type;

    public CardAdapter(ArrayList<CardContainer> location, int type) {
        this.type = type;
        this.location = location;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (this.type) {
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_cat_layout, parent, false);
                break;
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_listen_card, parent, false);
                break;
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_card, parent, false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.type);
        }
        FeatureViewHolder fvh = new FeatureViewHolder(v);
        switch (this.type) {
            case 0:
                v.setOnClickListener(e -> {
                    Toast.makeText(parent.getContext(), "TEST1 " + fvh.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                });
                break;
            case 1:
                v.setOnClickListener(e -> Toast.makeText(parent.getContext(), "TEST2 " + fvh.getAdapterPosition(), Toast.LENGTH_SHORT).show());
                break;
            case 2:
                v.setOnClickListener(e -> Toast.makeText(parent.getContext(), "TEST3 " + fvh.getAdapterPosition(), Toast.LENGTH_SHORT).show());
                break;
        }
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
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

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.catTitle);
            desc = itemView.findViewById(R.id.catDesc);
        }
    }
}
