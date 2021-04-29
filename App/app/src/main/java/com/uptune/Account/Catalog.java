package com.uptune.Account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.uptune.Helper.CardAdapter;
import com.uptune.Helper.CardContainer;
import com.uptune.MainActivity;
import com.uptune.R;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Catalog extends AppCompatActivity {

    RecyclerView bestCateg, mostList, mostList2;
    RecyclerView.Adapter adapter, adapter2;

    private static final String CLIENT_ID = "38b6f15617554f428ca3d3c5358deab4";
    private static final String REDIRECT_URI = "https://api-university.com/";
    private SpotifyAppRemote mSpotifyAppRemote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        setContentView(R.layout.activity_catalog);
        bestCateg = findViewById(R.id.bestCateg);
        mostList = findViewById(R.id.mostList);
        mostList2 = findViewById(R.id.mostList2);


        SpotifyApi api = new SpotifyApi();

        api.setAccessToken("BQAWkQzS8rO6Ec2ImQ9Epcd0M4lQeSxyj8X7EpFTvEGtAAOblc6SANGD4wFxI1bdJDs_BlpWfc3khXbPdqNc0HKM2N_BpvmfzBV1GuBvBjr68Luauz4dnHhAyTF98lGA4GWCyxoEQRAvCEIlWonNMEYmUQCJVGs96yXVKA");
        SpotifyService fetchData = api.getService();

        /*fetchData.getAlbum("4aawyAB9vmqN3uQ7FjRGTy", new Callback<Album>() {
            @Override
            public void success(Album album, retrofit.client.Response response) {
                Log.i("Success", album.name );
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Failure", error.toString());
            }
        });
*/
        fetchData.searchAlbums("Music to be murder", new Callback<AlbumsPager>() {

            @Override
            public void success(AlbumsPager albumsPager, Response response) {
                List<AlbumSimple> list = albumsPager.albums.items;
                for (AlbumSimple i : list) {
                    Log.i("Success",i.images.toString() );

                }


            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        renderBestCateg();
        //region Nav Bottom
        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav);
        bottomNavView.setSelectedItemId(R.id.search);
        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class)); //start
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.charts:
                    startActivity(new Intent(getApplicationContext(), Chart.class)); //start
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), Catalog.class)); //start
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.user:
                    startActivity(new Intent(getApplicationContext(), Account.class)); //start
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });
        //endregion
    }

    private void renderBestCateg() {
        bestCateg.setHasFixedSize(true);
        bestCateg.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mostList.setHasFixedSize(true);
        mostList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mostList2.setHasFixedSize(true);
        mostList2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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