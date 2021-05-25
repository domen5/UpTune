package com.uptune.Chart;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.imageview.ShapeableImageView;
import com.uptune.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartsSelector extends Fragment {

    ShapeableImageView img1, img2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_charts_selector, container, false);
        return root;
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        img1 = view.findViewById(R.id.btnTopTracksGloblal);
        img2 = view.findViewById(R.id.btnTopTracksItaly);

        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL("https://i.scdn.co/image/ab67706c0000bebb825e19c34ed5609896688ee5").getContent());
            img1.setImageBitmap(bitmap);
            bitmap = BitmapFactory.decodeStream((InputStream) new URL("https://i.scdn.co/image/ab67706c0000bebbb302acbaf8f051edf3f47d89").getContent());
            img2.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        img1.setOnClickListener(v -> {
            Chart someFragment = Chart.newInstance("Top Songs Global", "37i9dQZEVXbNG2KDcFcKOF", "https://i.scdn.co/image/ab67706c0000bebb825e19c34ed5609896688ee5");
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.chart_selector, someFragment); // give your fragment container id in first parameter
            transaction.addToBackStack("charts_selector");
            transaction.commit();
        });

        img2.setOnClickListener(v -> {
            Chart someFragment = Chart.newInstance("Top Songs Italy", "37i9dQZEVXbJUPkgaWZcWG", "https://i.scdn.co/image/ab67706c0000bebbb302acbaf8f051edf3f47d89");
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.chart_selector, someFragment); // give your fragment container id in first parameter
            transaction.addToBackStack("charts_selector");
            transaction.commit();
        });

    }
}