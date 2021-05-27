package com.uptune.History;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uptune.Adapter.Card.CardUsedAdapter;
import com.uptune.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.uptune.Used.Tag;
import com.uptune.Used.UsedElement;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class History extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FilterAdapter<Tag> filterAdapter;
    private Filter<Tag> filter;

    private String[] mTitles = {"Digital Album", "Used Album", "Song", "Type 4", "Type 5"};
    private int[] colors;
    private List<Tag> tags;
    // TODO: transition to List
    private ArrayList<UsedElement> cardItems;
    ArrayList<UsedElement> defaultCards = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);

        final Toolbar toolbar = findViewById(R.id.historyToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        this.colors = getResources().getIntArray(R.array.colors);

        this.tags = IntStream.range(0, mTitles.length)
                .mapToObj(i -> new Tag(mTitles[i], colors[i]))
                .collect(Collectors.toList());

        this.recyclerView = findViewById(R.id.historyRecyclerView);

        this.filter = findViewById(R.id.historyFilter);
        this.filterAdapter = makeFilterAdapter();
        filter.setAdapter(filterAdapter);
        filter.setListener(makeFilterListener());
        filter.setNoSelectedItemText("All Items");
        filter.build();

        getData();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = super.onCreateView(parent, name, context, attrs);



        return view;
    }

    private FilterAdapter<Tag> makeFilterAdapter() {
        return new FilterAdapter<Tag>(tags) {
            @NotNull
            @Override
            public FilterItem createView(int position, Tag tag) {
                FilterItem filterItem = new FilterItem(History.this);
                filterItem.setStrokeColor(colors[0]);
                filterItem.setTextColor(colors[0]);
                filterItem.setCornerRadius(14);
                filterItem.setCheckedTextColor(ContextCompat.getColor(History.this, android.R.color.white));
                filterItem.setColor(ContextCompat.getColor(History.this, android.R.color.white));
                filterItem.setCheckedColor(colors[position]);
                filterItem.setText(tag.getText());
                filterItem.deselect();
                return filterItem;
            }
        };
    }

    private FilterListener<Tag> makeFilterListener() {
        return new FilterListener<Tag>() {
            @Override
            public void onFiltersSelected(@NotNull ArrayList<Tag> arrayList) {
                for (Tag tag : arrayList) {
                    Log.i("TAPPI", tag.getText());
                    switch (tag.getText()) {
                        case "Default":
                            filter.deselectAll();
                            filter.collapse();
                            adapter = new CardUsedAdapter(defaultCards);
                            recyclerView.setAdapter(adapter);
                            break;
                        case "Price":
                            Collections.sort(cardItems);
                            adapter = new CardUsedAdapter(cardItems);
                            recyclerView.setAdapter(adapter);
                            break;
                        case "A-Z":
                            Collections.sort(cardItems, UsedElement.comparator);
                            adapter = new CardUsedAdapter(cardItems);
                            recyclerView.setAdapter(adapter);
                            break;
                        case "Z-A":
                            Collections.sort(cardItems, UsedElement.comparatorZ);
                            adapter = new CardUsedAdapter(cardItems);
                            recyclerView.setAdapter(adapter);
                            break;
                        case "Vendor":
                            Collections.sort(cardItems, UsedElement.usercomparator);
                            adapter = new CardUsedAdapter(cardItems);
                            recyclerView.setAdapter(adapter);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected() {
                adapter = new CardUsedAdapter(defaultCards);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFilterSelected(Tag tag) {
                switch (tag.getText()) {
                    case "Default":
                        adapter = new CardUsedAdapter(defaultCards);
                        recyclerView.setAdapter(adapter);
                        filter.deselectAll();
                        filter.collapse();
                        break;
                }
            }

            @Override
            public void onFilterDeselected(Tag tag) {

            }
        };
    }

    private void getData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("history");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardItems = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    UsedElement ele = d.getValue(UsedElement.class);
                    ele.setId(d.getKey());
                    cardItems.add(ele);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(History.this, LinearLayoutManager.VERTICAL, false));
                    adapter = new CardUsedAdapter(cardItems);
                    recyclerView.setAdapter(adapter);
                    defaultCards = cardItems;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}