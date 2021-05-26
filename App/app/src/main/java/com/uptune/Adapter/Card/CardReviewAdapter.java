package com.uptune.Adapter.Card;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardReviewAdapter extends RecyclerView.Adapter<com.uptune.Adapter.Card.CardReviewAdapter.FeatureViewHolder> {
    @NonNull
    @Override
    public CardReviewAdapter.FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CardReviewAdapter.FeatureViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class FeatureViewHolder extends RecyclerView.ViewHolder {
        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
