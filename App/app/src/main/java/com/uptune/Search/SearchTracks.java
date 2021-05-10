package com.uptune.Search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uptune.Adapter.ArtistAdapter;
import com.uptune.Adapter.SongAdapter;
import com.uptune.Artist.ArtistStuff;
import com.uptune.R;
import com.uptune.Song.SongList;
import com.uptune.Web;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchTracks extends Fragment {
    String name;
    JSONArray arr;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<SongList> cardContainers = new ArrayList<>();

    public SearchTracks(String name) {
        this.name = name;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.search_tracks_list);
        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.search_tracks_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setTitle("Tracks result for: "+name);
        renderCards();
        super.onViewCreated(view, savedInstanceState);
    }

    private void renderCards() {
        try {
            arr = Web.getTracksFromName(name);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < arr.length(); i++) {
                //popularity
                String name = arr.getJSONObject(i).getString("name");
                String id = arr.getJSONObject(i).getString("id");
                cardContainers.add(new SongList(name,id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new SongAdapter(cardContainers);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_tracks, container, false);
    }
}