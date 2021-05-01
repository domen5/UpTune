package com.uptune;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.uptune.Account.Account;
import com.uptune.Account.Catalog;
import com.uptune.Account.Chart;
import com.uptune.Helper.CardAdapter;
import com.uptune.Helper.CardContainer;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CatalogFragment extends Fragment {
    RecyclerView bestCateg, mostList, mostList2;
    RecyclerView.Adapter adapter, adapter2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_catalogf, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bestCateg = view.findViewById(R.id.bestCateg);
        mostList = view.findViewById(R.id.mostList);
        mostList2 = view.findViewById(R.id.mostList2);
        SpotifyApi api = new SpotifyApi();
        Log.i("token", Web.getToken());
        api.setAccessToken(Web.getToken());
        SpotifyService fetchData = api.getService();
        fetchData.searchAlbums("Music to be murder", new Callback<AlbumsPager>() {
            @Override
            public void success(AlbumsPager albumsPager, Response response) {
                List<AlbumSimple> list = albumsPager.albums.items;
                for (AlbumSimple i : list) {
                    Log.i("Success", i.images.toString());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Success", "FAIL");
            }
        });
        renderBestCateg();
        //endregion
    }

    private void renderBestCateg() {
        bestCateg.setHasFixedSize(true);
        bestCateg.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mostList.setHasFixedSize(true);
        mostList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mostList2.setHasFixedSize(true);
        mostList2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        ArrayList<CardContainer> setData = new ArrayList<>();
        setData.add(new CardContainer(R.drawable.logo2, "logo", "sadadsadasdas"));
        setData.add(new CardContainer(R.drawable.logo2, "logo111", "sdas"));
        setData.add(new CardContainer(R.drawable.logo2, "logo2121", "saasda"));
        adapter = new CardAdapter(setData, 0);
        adapter2 = new CardAdapter(setData, 1);
        bestCateg.setAdapter(adapter);
        mostList.setAdapter(adapter2);
        mostList2.setAdapter(adapter2);
    }
}