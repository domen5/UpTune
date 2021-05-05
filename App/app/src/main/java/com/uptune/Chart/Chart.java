package com.uptune.Chart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uptune.Helper.CaptureAct;
import com.uptune.MainActivity;
import com.uptune.R;
import com.uptune.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import kaaes.spotify.webapi.android.models.Artist;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.client.UrlConnectionClient;

public class Chart extends Fragment {
    private RecyclerView rwTopTracksGlobal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rwTopTracksGlobal = view.findViewById(R.id.topTracksGlobal);
        this.rwTopTracksGlobal.setHasFixedSize(true);
        this.rwTopTracksGlobal.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        final List<ChartItem> items = new ArrayList<>();
        try {
            JSONArray arr = Web.getTopTracksGlobal();

            for(int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i).getJSONObject("track");

                // name
                String name = current.getString("name");
                JSONArray artists = current.getJSONArray("artists");

                // artists
                List<String> artistsList = new ArrayList<>();
                for(int a = 0; a < artists.length(); a++) {
                    String currentArtist = artists.getJSONObject(a).getString("name");
                    artistsList.add(currentArtist);
                }

                // image
                String url = current.getJSONObject("album")
                        .getJSONArray("images")
                        .getJSONObject(0)
                        .getString("url");
                //Log.d("charts", artistsList.get(0));
                Log.d("charts", url);
                URL image = new URL(url);
                items.add(new ChartItem(name, image, artistsList));
                //Log.d("charts", current.getJSONObject("track").getString("name"));
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        this.rwTopTracksGlobal.setAdapter(new ChartAdapter(items));

//        Button scan = view.findViewById(R.id.scan);
//        scan.setOnClickListener(v -> {
//            scanCode();
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        IntentResult res = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (res != null) {
//            if (res.getContents() != null) {
//                AlertDialog.Builder build = new AlertDialog.Builder(getContext());
//                build.setMessage(res.getContents());
//                build.setTitle("Scan res");
//                build.setPositiveButton("Scan again", (dialog, which) -> scanCode()).setNegativeButton("finish", (dialog, which) -> getActivity().finish());
//                AlertDialog dialog = build.create();
//                dialog.show();
//            } else
//                Toast.makeText(getContext(), "no res", Toast.LENGTH_SHORT).show();
//        } else
//            super.onActivityResult(requestCode, resultCode, data);
    }

//    private void scanCode() {
//        IntentIntegrator integrator = new IntentIntegrator(getActivity());
//        integrator.setCaptureActivity(CaptureAct.class);
//        integrator.setOrientationLocked(false);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//        integrator.setPrompt("Scan album code");
//        integrator.initiateScan();
//    }
}