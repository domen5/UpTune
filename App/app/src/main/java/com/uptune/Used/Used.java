package com.uptune.Used;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uptune.Adapter.Card.CardUsedAdapter;
import com.uptune.R;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Used extends Fragment implements FilterListener<Tag> {

    ArrayList<UsedElement> setCards = new ArrayList<>();
    ArrayList<UsedElement> defaultCards = new ArrayList<>();
    private String[] mTitles = {"Default", "Price", "A-Z", "Z-A", "Vendor"};
    private int[] mColors;
    RecyclerView usedCardsRecycler;
    RecyclerView.Adapter adapter;
    private Filter<Tag> mFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_used, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usedCardsRecycler = view.findViewById(R.id.used_catalog_recycler);
        mColors = getResources().getIntArray(R.array.colors);
        mFilter = view.findViewById(R.id.filter);
        mFilter.setAdapter(new Adapter(getTags()));
        mFilter.setListener(this);
        //the text to show when there's no selected items
        mFilter.setNoSelectedItemText("All Items");
        mFilter.build();
        getData();
    }

    private void getData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("used");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setCards = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    UsedElement ele = d.getValue(UsedElement.class);
                    ele.setId(d.getKey());
                    setCards.add(ele);
                    defaultCards.add(ele);
                    usedCardsRecycler.setHasFixedSize(true);
                    usedCardsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    adapter = new CardUsedAdapter(setCards);
                    usedCardsRecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onFilterDeselected(Tag tag) {

    }

    @Override
    public void onFilterSelected(Tag tag) {
        switch (tag.getText()) {
            case "Default":
                adapter = new CardUsedAdapter(defaultCards);
                usedCardsRecycler.setAdapter(adapter);
                mFilter.deselectAll();
                mFilter.collapse();
                break;
        }
    }

    @Override
    public void onFiltersSelected(@NotNull ArrayList<Tag> arrayList) {
        for (Tag tag : arrayList) {
            Log.i("TAPPI", tag.getText());
            switch (tag.getText()) {
                case "Default":
                    mFilter.deselectAll();
                    mFilter.collapse();
                    adapter = new CardUsedAdapter(defaultCards);
                    usedCardsRecycler.setAdapter(adapter);
                    break;
                case "Price":
                    Collections.sort(setCards);
                    adapter = new CardUsedAdapter(setCards);
                    usedCardsRecycler.setAdapter(adapter);
                    break;
                case "A-Z":
                    Collections.sort(setCards, UsedElement.comparator);
                    adapter = new CardUsedAdapter(setCards);
                    usedCardsRecycler.setAdapter(adapter);
                    break;
                case "Z-A":
                    Collections.sort(setCards, UsedElement.comparatorZ);
                    adapter = new CardUsedAdapter(setCards);
                    usedCardsRecycler.setAdapter(adapter);
                    break;
                case "Vendor":
                    Collections.sort(setCards, UsedElement.usercomparator);
                    adapter = new CardUsedAdapter(setCards);
                    usedCardsRecycler.setAdapter(adapter);
                    break;

            }
        }
    }

    @Override
    public void onNothingSelected() {
        adapter = new CardUsedAdapter(defaultCards);
        usedCardsRecycler.setAdapter(adapter);
    }

    private List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < mTitles.length; ++i)
            tags.add(new Tag(mTitles[i], mColors[i]));
        return tags;
    }

    class Adapter extends FilterAdapter<Tag> {

        Adapter(@NotNull List<? extends Tag> items) {
            super(items);
        }

        @NotNull
        @Override
        public FilterItem createView(int position, Tag item) {
            FilterItem filterItem = new FilterItem(getContext());
            filterItem.setStrokeColor(mColors[0]);
            filterItem.setTextColor(mColors[0]);
            filterItem.setCornerRadius(14);
            filterItem.setCheckedTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
            filterItem.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
            filterItem.setCheckedColor(mColors[position]);
            filterItem.setText(item.getText());
            filterItem.deselect();
            return filterItem;
        }
    }

}