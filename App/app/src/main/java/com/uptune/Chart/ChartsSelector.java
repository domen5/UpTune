package com.uptune.Chart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.uptune.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartsSelector extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_charts_selector, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageButton btn1 = view.findViewById(R.id.btnTopTracksGloblal);
        final ImageButton btn2 = view.findViewById(R.id.btnTopTracksItaly);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chart someFragment = Chart.newInstance("Top Songs Global",
                        "37i9dQZEVXbNG2KDcFcKOF");
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.chart_selector, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack("charts_selector");
                transaction.commit();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chart someFragment = Chart.newInstance("Top Songs Italy",
                        "37i9dQZEVXbJUPkgaWZcWG");
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.chart_selector, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack("charts_selector");
                transaction.commit();
            }
        });

    }
}