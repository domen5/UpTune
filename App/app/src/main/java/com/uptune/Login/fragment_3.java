package com.uptune.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.uptune.Account.Account;
import com.uptune.R;


public class fragment_3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_3, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton btn = (ImageButton) view.findViewById(R.id.go_account);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Account.class);
            /*
            intent.putExtra("name", name);
            intent.putExtra("username", username);
            intent.putExtra("email", mail);
            intent.putExtra("phone", phone);
            */
            startActivity(intent);
        });
    }
}