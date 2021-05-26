package com.uptune.Adapter.Card;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.R;
import com.uptune.Review.ReviewClass;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class CardReviewAdapter extends RecyclerView.Adapter<com.uptune.Adapter.Card.CardReviewAdapter.FeatureViewHolder> {

    ArrayList<ReviewClass> reviews;

    public CardReviewAdapter(ArrayList<ReviewClass> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public CardReviewAdapter.FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Log.i("TAPPI", "OK");
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_review, parent, false);
        CardReviewAdapter.FeatureViewHolder fvh = new CardReviewAdapter.FeatureViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CardReviewAdapter.FeatureViewHolder holder, int position) {
        Bitmap image = null;
        ReviewClass reviewEl = reviews.get(position);
        try {
            image = BitmapFactory.decodeStream(new URL(reviewEl.getImg()).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.img.setImageBitmap(image);
        holder.name.setText(reviewEl.getName());
        holder.date.setText(reviewEl.getDate());
        holder.username.setText(reviewEl.getName());
        holder.desc.setText(reviewEl.getDesc());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class FeatureViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView username, date, desc, name;
        RatingBar rate;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.card_review_img);
            username = itemView.findViewById(R.id.card_review_user);
            date = itemView.findViewById(R.id.card_review_date);
            rate = itemView.findViewById(R.id.card_review_rating);
            desc = itemView.findViewById(R.id.card_review_desc);
            name = itemView.findViewById(R.id.card_review_name);
        }

    }
}
