package com.uptune.Account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uptune.Helper.FeatureAdapter;
import com.uptune.Helper.FetchBestCatg;
import com.uptune.MainActivity;
import com.uptune.R;

import java.util.ArrayList;

public class Catalog extends AppCompatActivity {

    RecyclerView bestCateg, mostList;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        bestCateg = findViewById(R.id.bestCateg);
        mostList = findViewById(R.id.mostList);

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

        ArrayList<FetchBestCatg> setData = new ArrayList<>();
        setData.add(new FetchBestCatg(R.drawable.logo2, "logo", "sadadsadasdas"));
        setData.add(new FetchBestCatg(R.drawable.logo2, "logo111", "sdas"));
        setData.add(new FetchBestCatg(R.drawable.logo2, "logo2121", "saasda"));

        adapter = new FeatureAdapter(setData);
        bestCateg.setAdapter(adapter);
        mostList.setAdapter(adapter);
    }
}