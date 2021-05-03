package com.uptune.Catalog;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uptune.Helper.SongAdapter;
import com.uptune.Helper.CardContainer;
import com.uptune.Helper.SongList;
import com.uptune.R;
import com.uptune.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class CategoriesDetails extends Fragment {

    private String id, title;
    RecyclerView bestCateg;
    RecyclerView.Adapter adapter;
    URL img;

    public CategoriesDetails(String title, URL img, String id) {
        this.id = id;
        this.title = title;
        this.img = img;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imgView = view.findViewById(R.id.categ_details_img);

        try {
            imgView.setImageBitmap(BitmapFactory.decodeStream(img.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bestCateg = view.findViewById(R.id.categ_details);
        bestCateg.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        ArrayList<SongList> setData = new ArrayList<>();
        setData.add(new SongList(title, img, id));
        try {
            JSONArray arr = Web.getCategories(title);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i);
                String name = current.getString("name");
                String id = current.getString("id");
                URL img = new URL(current.getJSONArray("images").getJSONObject(0).getString("url"));
                setData.add(new SongList(name, img, id));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        adapter = new SongAdapter(setData);
        bestCateg.setLayoutManager(gridLayoutManager);
        bestCateg.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories_details, container, false);
    }
}