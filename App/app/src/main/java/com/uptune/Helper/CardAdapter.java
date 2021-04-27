package com.uptune.Helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.R;

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
            default:
                throw new IllegalStateException("Unexpected value: " + this.type);
        }
        FeatureViewHolder fvh = new FeatureViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        CardContainer fetchBest = location.get(position);
        holder.img.setImageResource(fetchBest.getImage());
        holder.title.setText(fetchBest.getTitle());
        holder.desc.setText(fetchBest.getDesc());
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
