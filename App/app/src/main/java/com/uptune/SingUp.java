package com.uptune;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.Console;

public class SingUp extends AppCompatActivity {

    Button callLogIn, callSingUp;
    ImageView img;
    TextView loginText, loginTitle;
    TextInputLayout username, password, phone, mail, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        callLogIn = findViewById(R.id.btn_login);
        callSingUp = findViewById(R.id.btn_sing_up);
        img = findViewById(R.id.logo);
        loginTitle = findViewById(R.id.LoginTitle);
        loginText = findViewById(R.id.LoginText);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phoneNumber);
        mail = findViewById(R.id.email);
        name = findViewById(R.id.fullName);

        callLogIn.setOnClickListener(v -> {
            Pair[] pairs = new Pair[7];
            Intent intent = new Intent(SingUp.this, Login.class);
            pairs[0] = new Pair<View, String>(img, "logo_img");
            pairs[1] = new Pair<View, String>(loginTitle, "loadTitle");
            pairs[2] = new Pair<View, String>(loginText, "loadText");
            pairs[3] = new Pair<View, String>(username, "loadUsername");
            pairs[4] = new Pair<View, String>(password, "loadPw");
            pairs[5] = new Pair<View, String>(callSingUp, "loadLogin");
            pairs[6] = new Pair<View, String>(callLogIn, "loadSing");
            ActivityOptions opt = ActivityOptions.makeSceneTransitionAnimation(SingUp.this, pairs);
            startActivity(intent, opt.toBundle());
        });
        callSingUp.setOnClickListener(v -> {
            if (validateForm()) {
                Toast.makeText(getApplicationContext(), "Go in dashboard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateForm() {
        String ckName = name.getEditText().getText().toString();
        String ckUsername = username.getEditText().getText().toString();
        String ckMail = mail.getEditText().getText().toString();
        String ckPw = password.getEditText().getText().toString();
        String ckPhone = phone.getEditText().getText().toString();
        Boolean flag = true;
        if (ckName.isEmpty()) {
            name.setError("Name cannot be empty");
            flag = false;
        } else name.setError(null);

        if (ckUsername.isEmpty()) {
            username.setError("Username cannot be empty");
            flag = false;
        } else username.setError(null);

        if (ckMail.isEmpty()) {
            mail.setError("E-Mail cannot be empty");
            flag = false;
        } else mail.setError(null);

        if (ckPw.isEmpty()) {
            password.setError("Password cannot be empty");
            flag = false;
        } else password.setError(null);
        return flag;
    }
}