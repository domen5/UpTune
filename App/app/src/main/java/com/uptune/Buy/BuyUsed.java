package com.uptune.Buy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.uptune.R;
import com.uptune.Used.UsedElement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class BuyUsed extends Fragment {

    String id;
    ImageView img;

    public BuyUsed(String id) {
        this.id = id;
    }

    public BuyUsed() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_used, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar_used);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getFragmentManager().popBackStack());
        img= view.findViewById(R.id.used_image_bg);
        fetchDataAndSetValue(id);
    }

    private void fetchDataAndSetValue(String id) {
        Toast.makeText(getContext(), id, Toast.LENGTH_LONG).show();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("used").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsedElement ele = dataSnapshot.getValue(UsedElement.class);
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream((InputStream)(ele.getImg()).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                img.setImageBitmap(bitmap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}