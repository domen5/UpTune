package com.uptune.Catalog;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.uptune.Adapter.Card.CardArtistAdapter;
import com.uptune.Adapter.Card.NewReleaseAdapter;
import com.uptune.Adapter.CardAdapter;
import com.uptune.Helper.CardContainer;
import com.uptune.Helper.RadioButtonClass;
import com.uptune.R;
import com.uptune.Search.SearchAlbum;
import com.uptune.Search.SearchArtist;
import com.uptune.Search.SearchTracks;
import com.uptune.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Catalog extends Fragment {
    RecyclerView newRelease, artist, bestCat;
    RecyclerView.Adapter adapter;
    TextView showMoreCat;
    boolean firstOpen = false;
    ArrayList<CardContainer> setBestCat = new ArrayList<>();
    ArrayList<CardContainer> setNewRelease = new ArrayList<>();
    ArrayList<CardContainer> setArtist = new ArrayList<>();
    RadioButtonClass rdb1, rdb2, rdb3;
    ImageButton btnSearch;
    TextInputLayout textSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_catalog, container, false);
        return root;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(String.valueOf(R.id.account_frag));
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        showMoreCat = view.findViewById(R.id.show_more);
        showMoreCat.setOnClickListener(v -> {
            Fragment fr = new GetAllCategories();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.catalog, fr);
            transaction.addToBackStack("a");
            transaction.commit();
        });
        newRelease = view.findViewById(R.id.view_new_release);
        artist = view.findViewById(R.id.view_artist);
        bestCat = view.findViewById(R.id.view_categories);
        if (firstOpen)
            renderCards();
        else
            renderBestCateg();

        rdb1 = view.findViewById(R.id.rdb_album);
        rdb2 = view.findViewById(R.id.rdb_artist);
        rdb3 = view.findViewById(R.id.rdb_track);
        setPopUpList();
        btnSearch = view.findViewById(R.id.btn_search_catalog);
        textSearch = view.findViewById(R.id.search_text);

        btnSearch.setOnClickListener(v -> {
            if (textSearch.getEditText().getText().toString().matches("")) {
                textSearch.setError("Search field is empty!");
                return;
            }
            String name = textSearch.getEditText().getText().toString();
            if (rdb1.isChecked()) {
                Fragment fr = new SearchAlbum(name);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.catalog, fr);
                transaction.addToBackStack("a");
                transaction.commit();
            } else if (rdb2.isChecked()) {
                Fragment fr = new SearchArtist(name);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.catalog, fr);
                transaction.addToBackStack("a");
                transaction.commit();

            } else if (rdb3.isChecked()) {
                Fragment fr = new SearchTracks(name);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.catalog, fr);
                transaction.addToBackStack("a");
                transaction.commit();
            } else {
                textSearch.setError("Select a categories below!");
            }
        });

    }

    private void setPopUpList() {
        setRadiosListener();
    }

    private void setRadiosListener() {
        rdb1.setOwnChachedChangeListener((buttonView, isChecked) -> textSearch.setError(null));
        rdb2.setOwnChachedChangeListener((buttonView, isChecked) -> textSearch.setError(null));
        rdb3.setOwnChachedChangeListener((buttonView, isChecked) -> textSearch.setError(null));

    }

    private void renderBestCateg() {
        try {
            JSONArray arr = Web.getCategories();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i);
                String name = current.getString("name");
                String id = current.getString("id");
                URL img = new URL(current.getJSONArray("icons").getJSONObject(0).getString("url"));
                setBestCat.add(new CardContainer(name, img, id));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray arr = Web.getArtistId();
            JSONObject current = arr.getJSONObject(0);
            for (int i = 0; i < 20; i++) {
                String id = current.getJSONObject(i + "").getString("id");
                //prendo img e dati qui
                JSONObject artist = Web.getArtist(id);
                String name = artist.getString("name");
                String popularity = artist.getString("popularity");
                URL img = new URL(artist.getJSONArray("images").getJSONObject(0).getString("url"));
                setArtist.add(new CardContainer(name, img, id, popularity));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray arr = Web.getNewRelease();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i);
                String name = current.getString("name");
                String id = current.getString("id");
                URL img = new URL(current.getJSONArray("images").getJSONObject(0).getString("url"));
                String date = current.getString("release_date");
                String artist = current.getJSONArray("artists").getJSONObject(0).getString("name");
                String tracks = current.getString("total_tracks");
                setNewRelease.add(new CardContainer(name, img, id, artist, date, tracks));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        renderCards();
        firstOpen = true;
    }

    private void renderCards() {
        newRelease.setHasFixedSize(true);
        newRelease.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        artist.setHasFixedSize(true);
        artist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        bestCat.setHasFixedSize(true);
        bestCat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        artist.setItemViewCacheSize(20);
        artist.setDrawingCacheEnabled(true);
        artist.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        newRelease.setItemViewCacheSize(20);
        newRelease.setDrawingCacheEnabled(true);
        newRelease.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        bestCat.setItemViewCacheSize(20);
        bestCat.setDrawingCacheEnabled(true);
        bestCat.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        adapter = new NewReleaseAdapter(setNewRelease);
        this.newRelease.setAdapter(adapter);
        adapter = new CardArtistAdapter(setArtist);
        this.artist.setAdapter(adapter);
        adapter = new CardAdapter(setBestCat, 2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        this.bestCat.setLayoutManager(gridLayoutManager);
        this.bestCat.setAdapter(adapter);
    }
}