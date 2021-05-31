package com.uptune.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.imageview.ShapeableImageView;
import com.uptune.R;
import com.uptune.SessionAccount;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SettingsActivity extends AppCompatActivity {

    TextView mail, username;
    ShapeableImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_settings);
        mail = findViewById(R.id.settings_mail);
        username = findViewById(R.id.settings_username);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        mail.setText(SessionAccount.getMail());
        username.setText(SessionAccount.getUsername());
        img = findViewById(R.id.settings_img);
        Intent intent = getIntent();
        String url = intent.getStringExtra("bitmap");
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            img.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

        }
    }
}