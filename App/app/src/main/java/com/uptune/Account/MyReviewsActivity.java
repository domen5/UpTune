package com.uptune.Account;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyReviewsActivity extends AppCompatActivity {

    ImageButton btnBack;
    ArrayList<ReviewClass> setCards = new ArrayList<>();
    ArrayList<ReviewClass> defaultCards = new ArrayList<>();
    ArrayList<ReviewClass> defaultCards2 = new ArrayList<>();
    private String[] mTitles = {"Default", "Newest", "Oldest", "Star"};
    private int[] mColors;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private Filter<Tag> mFilter;
    private FilterListener<Tag> filterListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_my);
        this.mColors = getResources().getIntArray(R.array.colors);
        btnBack = findViewById(R.id.review_back);
        btnBack.setOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.review_recycler);
        getData();
        this.filterListener = new MyFilterAdapter2();
        this.mFilter = findViewById(R.id.reviewFilter);
        mFilter.setAdapter(new Adapter(getTags()));
        mFilter.setListener(filterListener);
        mFilter.setNoSelectedItemText("All Items");
        mFilter.build();

    }

    private class Adapter extends FilterAdapter<Tag> {

        Adapter(@NotNull List<? extends Tag> items) {
            super(items);
        }

        @NotNull
        @Override
        public FilterItem createView(int position, Tag item) {
            FilterItem filterItem = new FilterItem(getApplicationContext());
            filterItem.setStrokeColor(mColors[0]);
            filterItem.setTextColor(mColors[0]);
            filterItem.setCornerRadius(14);
            filterItem.setCheckedTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
            filterItem.setColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
            filterItem.setCheckedColor(mColors[position]);
            filterItem.setText(item.getText());
            filterItem.deselect();
            return filterItem;
        }
    }

    class MyFilterAdapter2 implements FilterListener<Tag> {
        @Override
        public void onFilterDeselected(Tag tag) {

        }

        @Override
        public void onFilterSelected(Tag tag) {
            switch (tag.getText()) {
                case "Default":
                    mFilter.deselectAll();
                    mFilter.collapse();
                    setCards.clear();
                    setCards.addAll(defaultCards2);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onFiltersSelected(@NotNull ArrayList<Tag> arrayList) {
            setCards.clear();
            for (Tag tag : arrayList) {
                switch (tag.getText()) {
                    case "Default":
                        mFilter.deselectAll();
                        mFilter.collapse(); // Questo genera bug
                        break;
                    case "Star":
                        Collections.sort(defaultCards);
                        break;
                    case "Newest":
                        Collections.sort(defaultCards, ReviewClass.dateComparatorNewest);
                        break;
                    case "Oldest":
                        Collections.sort(defaultCards, ReviewClass.dateComparatorOldest);
                        break;
                }
            }
            for (ReviewClass el : defaultCards) {
                setCards.add(el);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected() {
            if (adapter != null) {
                setCards.clear();
                setCards.addAll(defaultCards2);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < mTitles.length; ++i)
            tags.add(new Tag(mTitles[i], mColors[i]));
        return tags;
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
                                if (ele.getName().equals(SessionAccount.getUsername())) {
                                    ele.setId(d2.getKey());
                                    setCards.add(ele);
                                    defaultCards.add(ele);
                                    defaultCards2.add(ele);
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