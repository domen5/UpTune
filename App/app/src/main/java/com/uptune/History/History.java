package com.uptune.History;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uptune.Adapter.Card.HistoryElement;
import com.uptune.Adapter.HistoryAdapter;
import com.uptune.R;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class History extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rwAdapter;
    private FilterAdapter<Tag> filterAdapter;
    private Filter<Tag> filter;
    private String[] mTitles = {HistoryElement.USED_ALBUM, HistoryElement.DIGITAL_ALBUM, HistoryElement.SONG};
    private int[] colors;
    private List<Tag> tags;
    private List<HistoryElement> cardItems;
    private List<HistoryElement> defaultCards;


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
                filterItem.select();
                return filterItem;
            }
        };
    }

    private FilterListener<Tag> makeFilterListener() {
        return new FilterListener<Tag>() {
            @Override
            public void onFiltersSelected(@NotNull ArrayList<Tag> selectedTags) {

            }

            @Override
            public void onNothingSelected() {

            }

            @Override
            public void onFilterSelected(Tag tag) {
                int size = defaultCards.size();
                String tagName = tag.getText();
                for (int i = 0; i < size; i++) {
                    Log.d("type", i + "" + defaultCards.get(i).getName() + " " + defaultCards.get(i).getType());
                    if (defaultCards.get(i).getType().equalsIgnoreCase(tagName)) {
                        cardItems.add(defaultCards.get(i));
                    }
                }
                rwAdapter.notifyDataSetChanged();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFilterDeselected(Tag tag) {
                int size = cardItems.size();
                String tagName = tag.getText();
                for (int i = 0; i < size; i++) {
                    Log.d("type", i + "" + cardItems.get(i).getName() + " " + cardItems.get(i).getType());
                    if (cardItems.get(i).getType().equalsIgnoreCase(tagName)) {
                        cardItems.remove(i);
                        size--;
                        i--;
                    }
                }
                rwAdapter.notifyDataSetChanged();
            }
        };
    }

//                List<HistoryElement> toDelete = cardItems.stream()
//                        .filter(i -> !i.getType().equalsIgnoreCase(tag.getText()))
//                        .collect(Collectors.toList());
//
//                for(int i = 0; i < toDelete.size(); i++) {
//                    int position = cardItems.indexOf(toDelete.get(i));
//                    cardItems.remove(position);
//                    recyclerView.removeViewAt(position);
//                    rwAdapter.notifyItemRemoved(position);
//                    rwAdapter.notifyItemRangeChanged(position, cardItems.size());
//                }

//                    rwAdapter.notifyDataSetChanged();


    private void getData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("history").child(SessionAccount.getUsername());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardItems = new ArrayList<>();
                defaultCards = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    HistoryElement ele = d.getValue(HistoryElement.class);
                    ele.setId(d.getKey());
                    ele.setColor(0);
                    tags.forEach(t -> {
                        if (t.getText().equalsIgnoreCase(ele.getType())) {
                            ele.setColor(t.getColor());
                        }
                    });
                    cardItems.add(ele);
                    defaultCards.add(ele);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(History.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(120);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                rwAdapter = new HistoryAdapter(cardItems);
                recyclerView.setAdapter(rwAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}