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
import com.uptune.ChartsSelector;
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
    private String title;
    private String playlistUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        this.rwTopTracksGlobal = view.findViewById(R.id.topTracksGlobal);
        this.rwTopTracksGlobal.setHasFixedSize(true);
        this.rwTopTracksGlobal.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        final List<ChartItem> items = Web.getTopTracksGlobal();
        this.rwTopTracksGlobal.setAdapter(new ChartAdapter(items));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.title = getArguments().getString("title");
            this.playlistUrl = getArguments().getString("url");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Title of the chart.
     * @param url Url of the playlist.
     * @return A new instance of fragment charts_selector.
     */
    // TODO: Rename and change types and number of parameters
    public static Chart newInstance(String title, String url) {
        Chart fragment = new Chart();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }
}