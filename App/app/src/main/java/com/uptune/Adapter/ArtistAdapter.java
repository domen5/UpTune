package com.uptune.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.uptune.Artist.ArtistStuff;
import com.uptune.R;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.ArrayList;

//abstract for template method
public abstract class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.FeatureViewHolder> {

    ArrayList<ArtistStuff> items;

    //template method
    public abstract void onClick(View view, FeatureViewHolder fvh, ArtistStuff s);

    public ArtistAdapter(ArrayList<ArtistStuff> items) {
        this.items = items;
    }

    @NotNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_artist_details, parent, false);
        final FeatureViewHolder fvh = new FeatureViewHolder(view);

        view.setOnClickListener(e -> {
            int position = fvh.getAdapterPosition();
            ArtistStuff s = items.get(position);
            // template method actuation
            onClick(view, fvh, s);
        });

        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        ArtistStuff artistStuff = items.get(position);
        holder.title.setText(artistStuff.getTitle());
        Bitmap image = null;
        try {
            image = BitmapFactory.decodeStream(artistStuff.getImage().openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.img.setImageBitmap(image);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_artist);
            title = itemView.findViewById(R.id.artist_elem_title);
        }
    }
}
