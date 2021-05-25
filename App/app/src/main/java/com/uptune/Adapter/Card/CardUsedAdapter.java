package com.uptune.Adapter.Card;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.Buy.BuyUsed;
import com.uptune.Catalog.CategoriesDetails;
import com.uptune.R;
import com.uptune.Used.UsedElement;

import java.io.IOException;
import java.util.ArrayList;

public class CardUsedAdapter extends RecyclerView.Adapter<com.uptune.Adapter.Card.CardUsedAdapter.FeatureViewHolder> {

    ArrayList<UsedElement> location;

    public CardUsedAdapter(ArrayList<UsedElement> location) {
        this.location = location;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_used, parent, false);
        FeatureViewHolder fvh = new FeatureViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        Bitmap image = null;
        UsedElement usedElement = location.get(position);
        try {
            image = BitmapFactory.decodeStream(usedElement.getImg().openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.img.setImageBitmap(image);
        holder.name.setText(usedElement.getName());
        holder.price.setText(usedElement.getPrice() + "€");
        holder.user.setText(usedElement.getUser());
        holder.card.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.catalog_used, new BuyUsed(location.get(position).getId()))
                    .addToBackStack("a")
                    .commit();
        });
    }


    @Override
    public int getItemCount() {
        return location.size();
    }

    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, price, user;
        ConstraintLayout card;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.used_img);
            name = itemView.findViewById(R.id.used_name);
            price = itemView.findViewById(R.id.used_price);
            user = itemView.findViewById(R.id.used_user);
            card = itemView.findViewById(R.id.card_used);
        }
    }
}