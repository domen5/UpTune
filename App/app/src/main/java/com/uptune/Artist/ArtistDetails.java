package com.uptune.Artist;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uptune.Adapter.ArtistAdapter;
import com.uptune.Artist.ArtistStuff;
import com.uptune.R;
import com.uptune.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class ArtistDetails extends Fragment {
    private int fragmentId;
    private String id, title, bio;
    RecyclerView artistStuff;
    RecyclerView.Adapter adapter;
    URL img;

    public ArtistDetails(String title, URL img, String id, String bio) {
        this.id = id;
        this.title = title;
        this.bio = bio;
        this.img = img;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.fragmentId = view.getId();

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(String.valueOf(R.id.categories_details));
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.toolbar_artist_det);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        toolbar.setTitle(title);

        TextView bio = view.findViewById(R.id.artist_details_bio);
        bio.setText(this.bio);

        ImageView imgView = view.findViewById(R.id.details_artist_img);
        try {
            imgView.setImageBitmap(BitmapFactory.decodeStream(img.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        artistStuff = view.findViewById(R.id.artist_object);
        artistStuff.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        ArrayList<ArtistStuff> setData = new ArrayList<>();

        try {
            //ALBUM & TRACK OF ARTIST
            JSONArray arr = Web.getArtistStuff(id);
            JSONObject current = new JSONObject();
            //GET ALBUMS
            ArrayList<String> setted = new ArrayList<String>(6);
            int inserted = 0;

            for (int i = 0; i < arr.getJSONArray(0).length(); i++) {
                current = arr.getJSONArray(0).getJSONObject(i);
                String name = current.getString("name");
                if (setted.contains(name)) {
                    continue;
                }
                inserted++;
                setted.add(name);
                String id = current.getString("id");
                URL img = new URL(current.getJSONArray("images").getJSONObject(0).getString("url"));
                //popularity
                //release date
                //total_tracks
                setData.add(new ArtistStuff(name, id, img));
                if (inserted == 6)
                    break;
            }
            //GET Tracks
            setted = new ArrayList<String>(6);
            inserted = 0;
            for (int i = 0; i < arr.getJSONArray(1).length(); i++) {
                current = arr.getJSONArray(1).getJSONObject(i);
                URL img = new URL(current.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url"));
                //preview_url a volte null!
                String name = current.getString("name");
                if (setted.contains(name)) {
                    continue;
                }
                inserted++;
                setted.add(name);
                String id = current.getString("id");
                setData.add(new ArtistStuff(name, id, img));
                if (inserted == 6)
                    break;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        adapter = new ArtistAdapter(setData, getId());
        artistStuff.setLayoutManager(gridLayoutManager);
        artistStuff.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artist_details, container, false);
    }
}