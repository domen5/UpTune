package com.uptune.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uptune.Account.Account;
import com.uptune.R;

public class Login extends AppCompatActivity {


    private Button callSingUp, callLogIn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth auth;

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


        callLogIn.setOnClickListener(v -> {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("user");
            String us = username.getEditText().getText().toString();
            String pw = password.getEditText().getText().toString();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
            Query checkUser = reference.orderByChild("username").equalTo(us);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        username.setError(null);
                        username.setErrorEnabled(false);

                        String passwordDB = dataSnapshot.child(us).child("passw").getValue(String.class);
                        if (passwordDB.equals(pw)) {

                            password.setError(null);
                            password.setErrorEnabled(false);

                            String name = dataSnapshot.child(us).child("name").getValue(String.class);
                            String username = dataSnapshot.child(us).child("username").getValue(String.class);
                            String phone = dataSnapshot.child(us).child("phone").getValue(String.class);
                            String mail = dataSnapshot.child(us).child("mail").getValue(String.class);

                            Intent intent = new Intent(getApplicationContext(), Account.class);
                            intent.putExtra("name", name);
                            intent.putExtra("username", username);
                            intent.putExtra("email", mail);
                            intent.putExtra("phone", phone);

                            startActivity(intent);
                        } else {
                            password.setError("Wrong password");
                            password.requestFocus();
                        }
                    } else {
                        username.setError("This username does not exist");
                        username.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


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