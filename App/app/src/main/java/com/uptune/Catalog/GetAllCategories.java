package com.uptune.Catalog;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uptune.Adapter.CardAdapter;
import com.uptune.Helper.CardContainer;
import com.uptune.R;
import com.uptune.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GetAllCategories extends Fragment {
    RecyclerView viewCategories;
    RecyclerView.Adapter adapter;
    ArrayList<CardContainer> allCat = new ArrayList<>();
    Boolean firstCall=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_get_all_categories, container, false);
        return root;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCategories = view.findViewById(R.id.all_categories_list);

        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.toolbar_all_cat);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(String.valueOf(R.id.account_frag));
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        if (firstCall)
            renderCards();
        else
            renderAllCateg();
    }

    private void renderAllCateg() {
        try {
            JSONArray arr = Web.getAllCategories();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject current = arr.getJSONObject(i);
                String name = current.getString("name");
                String id = current.getString("id");
                URL img = new URL(current.getJSONArray("icons").getJSONObject(0).getString("url"));
                allCat.add(new CardContainer(name, img, id));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        renderCards();
        firstCall = true;
    }

    private void renderCards() {
        viewCategories.setHasFixedSize(true);
        viewCategories.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new CardAdapter(allCat, 2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        this.viewCategories.setLayoutManager(gridLayoutManager);
        this.viewCategories.setAdapter(adapter);
    }
}