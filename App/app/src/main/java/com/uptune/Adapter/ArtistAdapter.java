package com.uptune.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.uptune.Artist.ArtistStuff;
import com.uptune.Catalog.AlbumFragment;
import com.uptune.Catalog.CardDetails;
import com.uptune.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.FeatureViewHolder> {

    ArrayList<ArtistStuff> albums;
    private final int parentId;

    public ArtistAdapter(ArrayList<ArtistStuff> albums, int parentId) {
        this.albums = albums;
        this.parentId = parentId;
    }

    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_artist_details, parent, false);
        final FeatureViewHolder fvh = new FeatureViewHolder(view);
        view.setOnClickListener(e -> {
            int position = fvh.getAdapterPosition();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(parentId, new AlbumFragment(albums.get(position).getTitle(), albums.get(position).getImage(), albums.get(position).getID()))
                    .addToBackStack("a")
                    .commit();
        });
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        ArtistStuff artistStuff = albums.get(position);
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
        return albums.size();
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
