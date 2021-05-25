package com.uptune.Search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;

import com.uptune.Adapter.CardAdapter;
import com.uptune.Helper.CardContainer;
import com.uptune.R;
import com.uptune.Web;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchAlbum extends Fragment {
    String name;
    ImageView img;
    JSONArray arr;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<CardContainer> cardContainers = new ArrayList<>();

    public SearchAlbum(String name) {
        this.name = name;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.search_album_list);
        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.search_album_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setTitle(name);
        img=view.findViewById(R.id.search_albums_img);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL("https://images.unsplash.com/photo-1459233313842-cd392ee2c388?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80").getContent());
            img.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderCards();
        super.onViewCreated(view, savedInstanceState);
    }

    private void renderCards() {
        try {
            arr = Web.getAlbumsFromName(name);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < arr.length(); i++) {
                //release_date
                //images
                //artist
                String name = arr.getJSONObject(i).getString("name");
                String id = arr.getJSONObject(i).getString("id");
                URL img = new URL(arr.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url"));
                cardContainers.add(new CardContainer(name, img, id));
            }
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new CardAdapter(cardContainers, 4);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_album, container, false);
    }
}