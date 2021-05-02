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
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Chart extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        LinearLayout linear = view.findViewById(R.id.topTracksGlobal);

        try {
            JSONArray arr = Web.getTopTracksGlobal();
            

            for(int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i);
                TextView trackName = new TextView(getActivity());

                Log.d("charts", current.getJSONObject("track").getString("name"));
                trackName.setText(current.getJSONObject("track").getString("name"));
                trackName.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                linear.addView(trackName);
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




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