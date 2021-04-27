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

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder> {

    ArrayList<FetchBestCatg> location;

    public FeatureAdapter(ArrayList<FetchBestCatg> location) {
        this.location = location;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_cat_layout, parent, false);
        FeatureViewHolder fvh = new FeatureViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        FetchBestCatg fetchBest = location.get(position);
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
            img = itemView.findViewById(R.id.catImg);
            title = itemView.findViewById(R.id.catTitle);
            desc = itemView.findViewById(R.id.catDesc);
        }
    }
}
