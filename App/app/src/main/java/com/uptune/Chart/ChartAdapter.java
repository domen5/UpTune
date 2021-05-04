package com.uptune.Chart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.R;

import java.io.IOException;
import java.util.List;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ViewHolder> {
    private List<ChartItem> items;

    public ChartAdapter(List<ChartItem> items) {
        this.items = items;
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

        // Retrieve image from url
        try {
            Bitmap bmp = null;
            bmp = BitmapFactory.decodeStream(item.getImage().openConnection().getInputStream());
            viewHolder.getCoverImageView().setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERROR", e.getMessage());
            viewHolder.getCoverImageView().setImageResource(R.drawable.ic_user);
        }

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView trackNameView;
        private  final TextView trackArtistView;
        private  final ImageView coverImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.trackNameView = itemView.findViewById(R.id.trackNameView);
            this.trackArtistView = itemView.findViewById(R.id.trackArtistView);
            this.coverImageView = itemView.findViewById(R.id.coverImageView);
        }

        public TextView getTrackNameView() { return this.trackNameView; }

        public TextView getTrackArtistView() { return trackArtistView; }

        public ImageView getCoverImageView() { return this.coverImageView; }
    }
}
