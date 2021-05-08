package com.uptune;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.uptune.Chart.Chart;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartsSelector extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_charts_selector, container, false);
        final ImageButton btn1 = view.findViewById(R.id.btnTopTracksGloblal);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chart someFragment = Chart.newInstance("Top Songs Global",
                        "https://api.spotify.com/v1/playlists/37i9dQZEVXbNG2KDcFcKOF");
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.chart_selector, someFragment); // give your fragment container id in first parameter
                transaction.commit();
            }
        });
        return view;
    }
}