package com.uptune.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uptune.Account.Account;
import com.uptune.Account.Chart;
import com.uptune.MainActivity;
import com.uptune.R;

public class Login extends AppCompatActivity {


    private Button callSingUp, callLogIn;
    ImageButton singUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        callSingUp = findViewById(R.id.sign_up);
        singUp = findViewById(R.id.sign_up_btn);
        callLogIn = findViewById(R.id.logIn);
        ImageView img = findViewById(R.id.logo);
        TextView loginTitle = findViewById(R.id.LoginTitle);
        TextView loginText = findViewById(R.id.LoginText);
        TextInputLayout username = findViewById(R.id.username);
        TextInputLayout password = findViewById(R.id.password);

        callLogIn.setOnClickListener(v -> {
            if (!isConnected(this)) {
                noInternetDialog();
                return;
            }
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
            Intent intent = new Intent(Login.this, SignUp.class);
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

        singUp.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void noInternetDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_no_connection);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnCancel = dialog.findViewById(R.id.cancel);
        Button btnConfirm = dialog.findViewById(R.id.confirm);
        btnConfirm.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            dialog.dismiss();
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected()))
            return true;
        else
            return false;
    }
}