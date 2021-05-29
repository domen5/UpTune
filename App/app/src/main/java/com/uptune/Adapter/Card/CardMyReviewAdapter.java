package com.uptune.Adapter.Card;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.uptune.R;
import com.uptune.Review.ReviewClass;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class CardMyReviewAdapter extends CardReviewAdapter {
    public CardMyReviewAdapter(ArrayList<ReviewClass> reviews) {
        super(reviews);
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_review_user, parent, false);
        CardReviewAdapter.FeatureViewHolder fvh = new CardReviewAdapter.FeatureViewHolder(v);
        return fvh;
    }


    @Override
    public void onBindViewHolder(@NonNull CardReviewAdapter.FeatureViewHolder holder, int position) {
        ReviewClass reviewEl = reviews.get(position);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new URL(reviewEl.getImgProduct()).openStream());
            holder.img.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.name.setText(reviewEl.getProductName());
        holder.date.setText(reviewEl.getDate());
        holder.username.setText(reviewEl.getArtists());
        holder.desc.setText(reviewEl.getDesc());
        holder.rate.setRating(Float.parseFloat(reviewEl.getRate()));
    }
}
