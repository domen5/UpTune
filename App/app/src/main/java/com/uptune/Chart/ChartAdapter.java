package com.uptune.Chart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.uptune.R;
import com.uptune.Song.SongDetails;

import java.io.IOException;
import java.util.List;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ViewHolder> {
    private final List<ChartItem> items;
    private  int fragmentId;

    public ChartAdapter(List<ChartItem> items, int fragmentId) {
        this.items = items;
        this.fragmentId = fragmentId;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.charts_card, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        ChartItem item = items.get(position);

        viewHolder.getTrackNameView().setText(item.getName());
        viewHolder.getTrackArtistView().setText(item.getArtistsString());
        viewHolder.getCoverImageView().setImageBitmap(item.getImageFile());

        viewHolder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("called","Card n# " + item.getId());
                SongDetails details = SongDetails.newInstance(item.getId());
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(fragmentId, details)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView trackNameView;
        private final TextView trackArtistView;
        private final ImageView coverImageView;
        private final MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.trackNameView = itemView.findViewById(R.id.trackNameView);
            this.trackArtistView = itemView.findViewById(R.id.trackArtistView);
            this.coverImageView = itemView.findViewById(R.id.coverImageView);
            this.cardView = itemView.findViewById(R.id.chart_card_view);
        }

        public TextView getTrackNameView() {
            return this.trackNameView;
        }

        public TextView getTrackArtistView() {
            return trackArtistView;
        }

        public ImageView getCoverImageView() {
            return this.coverImageView;
        }

        public MaterialCardView getCardView() { return this.cardView; }
    }
}
