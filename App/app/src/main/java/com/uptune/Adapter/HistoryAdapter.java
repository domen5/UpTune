package com.uptune.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.Adapter.Card.HistoryElement;
import com.uptune.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.FeatureViewHolder> {

    private List<HistoryElement> items;
    private int[] colors;

    //template method
    public void onClick(View view, HistoryAdapter.FeatureViewHolder fvh, HistoryElement s) {

    }

    public HistoryAdapter(List<HistoryElement> items) {
        this.items = items;
        for (HistoryElement element : items) {
            String image = element.getImg();
            element.setImg(image);
        }
    }

    @NotNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history, parent, false);
        final HistoryAdapter.FeatureViewHolder fvh = new HistoryAdapter.FeatureViewHolder(view);

        view.setOnClickListener(e -> {
            int position = fvh.getAdapterPosition();
            HistoryElement s = items.get(position);
            // template method actuation
            onClick(view, fvh, s);
        });

        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.FeatureViewHolder holder, int position) {
        HistoryElement element = items.get(position);

        holder.name.setText(element.getName());
        holder.date.setText(element.getDate());
        holder.price.setText("€" + element.getPrice());
        holder.constraintLayout.setBackgroundColor(element.getColor());
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(element.getImg()).getContent());
            holder.img.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.type.setText(element.getType());
        switch (element.getType()){
            case "album":
                holder.type.setChipBackgroundColorResource(R.color.chip_album);
                break;
            case "song":
                holder.type.setChipBackgroundColorResource(R.color.chip_song);
                break;
            case "used":
                holder.type.setChipBackgroundColorResource(R.color.chip_used);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, price;
        ImageView img;
        ConstraintLayout constraintLayout;
        com.google.android.material.chip.Chip type;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.used_name);
            date = itemView.findViewById(R.id.used_user);
            price = itemView.findViewById(R.id.used_price);
            img = itemView.findViewById(R.id.used_img);
            constraintLayout = itemView.findViewById(R.id.historyConstraintLayout1);
            type= itemView.findViewById(R.id.chip_1);
        }
    }
}
