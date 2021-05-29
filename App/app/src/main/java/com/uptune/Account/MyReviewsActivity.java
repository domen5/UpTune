package com.uptune.Account;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uptune.Adapter.Card.CardMyReviewAdapter;
import com.uptune.R;
import com.uptune.Review.ReviewClass;
import com.uptune.SessionAccount;
import com.uptune.Used.Tag;
import com.yalantis.filter.widget.Filter;

import java.util.ArrayList;

public class MyReviewsActivity extends AppCompatActivity {

    ImageButton btnBack;
    ArrayList<ReviewClass> setCards = new ArrayList<>();
    ArrayList<ReviewClass> defaultCards = new ArrayList<>();
    private String[] mTitles = {"Default", "Newest", "Oldest", "Star"};
    private int[] mColors;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private Filter<Tag> mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_my);
        btnBack = findViewById(R.id.review_back);
        btnBack.setOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.review_recycler);
        getData();
    }


    private void getData() {
        final ArrayList<String> oldId = new ArrayList<>();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("lookupReview").child(SessionAccount.getUsername());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setCards = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (oldId.size() == 0)
                        oldId.add(d.child("id").getValue().toString());
                    else if (oldId.contains(d.child("id").getValue().toString()))
                        continue;
                    oldId.add(d.child("id").getValue().toString());
                    DatabaseReference reference = rootNode.getReference("review").child(d.child("id").getValue().toString());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot d2 : snapshot.getChildren()) {
                                ReviewClass ele = d2.getValue(ReviewClass.class);
                                Log.i("TAPPI", ele.getName());
                                if (ele.getName().equals(SessionAccount.getUsername())) {
                                    ele.setId(d2.getKey());
                                    Log.i("TAPPI", "ele.getName()");
                                    setCards.add(ele);
                                    defaultCards.add(ele);
                                }
                            }
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            recyclerView.setItemViewCacheSize(20);
                            recyclerView.setDrawingCacheEnabled(true);
                            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                            adapter = new CardMyReviewAdapter(setCards);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}