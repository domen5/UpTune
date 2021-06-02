package com.uptune.Buy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uptune.R;
import com.uptune.SessionAccount;
import com.uptune.Used.UsedElement;
import com.uptune.Web;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class BuyUsed extends Fragment {

    String id, price, imgStr, name;
    ImageView imgUser, imgArtist, img, imgBg;
    TextView title, artist, description, label, manuf;
    MaterialButton bottonBuy;

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
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_used, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar_used);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getFragmentManager().popBackStack());
        toolbar.setVisibility(View.VISIBLE);
        imgBg = view.findViewById(R.id.used_image_bg);
        imgUser = view.findViewById(R.id.img_buy_profile);
        img = view.findViewById(R.id.buy_img);
        imgArtist = view.findViewById(R.id.buy_artist_img);
        bottonBuy = view.findViewById(R.id.bottom_buy);
        artist = view.findViewById(R.id.buy_title_artist);
        description = view.findViewById(R.id.buy_description);
        manuf = view.findViewById(R.id.buy_manuf);
        label = view.findViewById(R.id.buy_label);
        title = view.findViewById(R.id.buy_title_details);
        fetchDataAndSetValue(id);
        bottonBuy.setOnClickListener(v -> pay());
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchDataAndSetValue(id);
    }

    private void pay() {
        Intent i = new Intent(getActivity(), BuyCreditCard.class);
        i.putExtra("price", price);
        i.putExtra("img", imgStr);
        i.putExtra("type", "used");
        i.putExtra("name", name);
        i.putExtra("id", id);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void fetchDataAndSetValue(String id) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("used").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsedElement ele = dataSnapshot.getValue(UsedElement.class);
                if(ele==null)
                    return;
                Bitmap bitmap = null;
                imgStr = ele.getImg().toString();
                try {
                    bitmap = BitmapFactory.decodeStream((InputStream) (ele.getImg()).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                img.setImageBitmap(bitmap);
                imgBg.setImageBitmap(bitmap);
                price = ele.getPrice();
                bottonBuy.setText("Buy for " + price + "€");
                name = ele.getName();
                title.setText(ele.getName());
                description.setText(ele.getDescription());
                artist.setText(ele.getArtist());
                label.setText("Label: " + ele.getLabel());
                manuf.setText("Manufacturing: " + ele.getManuf());

                try {
                    JSONArray data = Web.getArtistFromName(ele.getArtist());
                    String obj = data.getJSONObject(0).getJSONArray("images").getJSONObject(0).getString("url");
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(obj).getContent());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                imgArtist.setImageBitmap(bitmap);
                DatabaseReference userRef = rootNode.getReference("user").child(ele.getUser());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SessionAccount s = dataSnapshot.getValue(SessionAccount.class);
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(s.getImg()).getContent());
                            imgUser.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}