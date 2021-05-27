package com.uptune.Catalog;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uptune.Adapter.Card.CardReviewAdapter;
import com.uptune.Adapter.SongAdapter;
import com.uptune.Buy.BuyCreditCard;
import com.uptune.Helper.LookupSell;
import com.uptune.R;
import com.uptune.Review.ReviewClass;
import com.uptune.SessionAccount;
import com.uptune.Song.SongList;
import com.uptune.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class AlbumFragment extends Fragment {
    private String id, title, price;
    private RecyclerView songList;
    private RecyclerView.Adapter adapter;
    RecyclerView reviewRecycler;
    ArrayList<ReviewClass> setCards = new ArrayList<>();
    private URL img;
    MaterialButton buy;

    public AlbumFragment(String title, URL img, String id) {
        this.id = id;
        this.title = title;
        this.img = img;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.album_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setTitle(title);


        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(String.valueOf(R.id.categories_details));
        if (fragment != null)
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();

        ImageView imgView = view.findViewById(R.id.album_details_img);

        try {
            imgView.setImageBitmap(BitmapFactory.decodeStream(img.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        songList = view.findViewById(R.id.album_recycler_view);
        songList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        ArrayList<SongList> setData = new ArrayList<>();

        try {
            JSONArray arr = Web.getAlbum(id);
            price = String.format("%.2f", (arr.length() * 0.75));
            for (int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i);
                String name = current.getString("name");
                String id = current.getString("id");
                String artists = "";
                for (int j = 0; j < current.getJSONArray("artists").length(); j++) {
                    String artistName = current.getJSONArray("artists").getJSONObject(j).getString("name");
                    if (j == current.getJSONArray("artists").length() - 1)
                        artists += artistName;
                    else
                        artists += artistName + ", ";
                }
                setData.add(new SongList(name, id, img, artists));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        final int idV = View.generateViewId();
        view.setId(idV);
        adapter = new SongAdapter(setData, idV);
        songList.setLayoutManager(gridLayoutManager);
        songList.setAdapter(adapter);

        reviewRecycler = view.findViewById(R.id.album_details_recycler_review);
        fetchRecycler();
        Button submitReview = view.findViewById(R.id.submit_rev);
        submitReview.setOnClickListener(v -> sendReview(view));

        buy = view.findViewById(R.id.album_details_buy);
        buy.setText("Buy for " + price + "€");
        buy.setOnClickListener(v -> pay());

    }

    private void pay() {
        Intent i = new Intent(getActivity(), BuyCreditCard.class);
        i.putExtra("price", price);
        i.putExtra("img", img);
        i.putExtra("type", "album");
        i.putExtra("name", title);
        i.putExtra("id", id);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void fetchRecycler() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("review").child(id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    ReviewClass ele = d.getValue(ReviewClass.class);
                    setCards.add(ele);
                }
                reviewRecycler.setHasFixedSize(true);
                reviewRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                adapter = new CardReviewAdapter(setCards);
                reviewRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void sendReview(View view) {
        EditText editText = view.findViewById(R.id.review_edit_text);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        String username = SessionAccount.getUsername();
        String desc = editText.getEditableText().toString();
        String rate = ratingBar.getRating() + "";
        ReviewClass reviewClass = new ReviewClass(username, rate, desc);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("review");
        reference.child(id).push().setValue(reviewClass);
        LookupSell lookupSell = new LookupSell(SessionAccount.getUsername(), id);
        reference = FirebaseDatabase.getInstance().getReference("lookupReview");
        reference.child(SessionAccount.getUsername()).push().setValue(lookupSell);
        Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }
}
