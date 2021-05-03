package com.uptune.Catalog;

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

import com.uptune.Helper.SongAdapter;
import com.uptune.Helper.SongList;
import com.uptune.R;
import com.uptune.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class ArtistDetails extends Fragment {
    private String id, title;
    RecyclerView songList;
    RecyclerView.Adapter adapter;
    URL img;

    public ArtistDetails(String title, URL img, String id) {
        this.id = id;
        this.title = title;
        this.img = img;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(String.valueOf(R.id.categories_details));
        if (fragment != null)
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        ImageView imgView = view.findViewById(R.id.details_artist_img);
        try {
            imgView.setImageBitmap(BitmapFactory.decodeStream(img.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        songList = view.findViewById(R.id.artist_object);
        songList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        ArrayList<SongList> setData = new ArrayList<>();

        try {
            JSONArray arr = Web.getArtistId();
            JSONObject current = arr.getJSONObject(0);
            String id = current.getString("id");
            arr = Web.getArtistStuff(id);
            for (int i = 0; i < arr.length(); i++) {
                current = arr.getJSONObject(i);
                id = current.getString("id");
                String name = current.getString("name");
                setData.add(new SongList(name, id));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        adapter = new SongAdapter(setData);
        songList.setLayoutManager(gridLayoutManager);
        songList.setAdapter(adapter);
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