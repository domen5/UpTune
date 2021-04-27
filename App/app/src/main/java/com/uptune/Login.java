package com.uptune;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {


   private Button callSingUp, callLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        callSingUp = findViewById(R.id.sing_up);
        callLogIn = findViewById(R.id.logIn);
        ImageView img = findViewById(R.id.logo);
        TextView loginTitle = findViewById(R.id.LoginTitle);
        TextView loginText = findViewById(R.id.LoginText);
        TextInputLayout username = findViewById(R.id.username);
        TextInputLayout password = findViewById(R.id.password);


        callSingUp.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "asdsada", Toast.LENGTH_SHORT);
            Log.i("sadada", "adasdasd");
        });

        callSingUp.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SingUp.class);
            Pair[] pairs = new Pair[7];
            pairs[0] = new Pair<View, String>(img, "logo_img");
            pairs[1] = new Pair<View, String>(loginTitle, "loadTitle");
            pairs[2] = new Pair<View, String>(loginText, "loadText");
            pairs[3] = new Pair<View, String>(username, "loadUsername");
            pairs[4] = new Pair<View, String>(password, "loadPw");
            pairs[5] = new Pair<View, String>(callSingUp, "loadLogin");
            pairs[6] = new Pair<View, String>(callLogIn, "loadSing");
            ActivityOptions opt = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
            startActivity(intent, opt.toBundle());
        });
    }
}