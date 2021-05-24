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

import com.uptune.Adapter.CardAdapter;
import com.uptune.Helper.CardContainer;
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

    public CategoriesDetails(){};

    public CategoriesDetails(String title, URL img, String id) {
        this.id = id;
        this.title = title;
        this.img = img;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.toolbar_cat_det);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v ->getFragmentManager().popBackStack());
        toolbar.setTitle(title);

        ImageView imgView = view.findViewById(R.id.categ_details_img);

        try {
            imgView.setImageBitmap(BitmapFactory.decodeStream(img.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bestCateg = view.findViewById(R.id.categ_details);
        bestCateg.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        bestCateg.setItemViewCacheSize(20);
        bestCateg.setDrawingCacheEnabled(true);
        bestCateg.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        ArrayList<CardContainer> setData = new ArrayList<>();
        try {
            JSONArray arr = Web.getCategoriesLastFm(title);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i);
                String name = current.getString("name");
                String id = Web.getIdFromName(name);
                Log.i("TOKEN", id);
                URL img = new URL(current.getJSONArray("image").getJSONObject(3).getString("#text"));
                setData.add(new CardContainer(name, img, id));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        adapter = new CardAdapter(setData, 3);
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