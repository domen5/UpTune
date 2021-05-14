package com.uptune.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uptune.Helper.UserHelper;
import com.uptune.R;
import com.uptune.SessionAccount;

public class SignUp extends AppCompatActivity {

    Button callLogIn, callSingUp;
    ImageButton back;
    ImageView img;
    TextView loginText, loginTitle;
    TextInputLayout username, password, phone, mail, name;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth auth;

    private float x1, x2;
    private final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        callLogIn = findViewById(R.id.btn_login);
        callSingUp = findViewById(R.id.btn_sign_up);
        back = findViewById(R.id.back);
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
            Intent intent = new Intent(SignUp.this, Login.class);
            pairs[0] = new Pair<View, String>(img, "logo_img");
            pairs[1] = new Pair<View, String>(loginTitle, "loadTitle");
            pairs[2] = new Pair<View, String>(loginText, "loadText");
            pairs[3] = new Pair<View, String>(username, "loadUsername");
            pairs[4] = new Pair<View, String>(password, "loadPw");
            pairs[5] = new Pair<View, String>(callSingUp, "loadLogin");
            pairs[6] = new Pair<View, String>(callLogIn, "loadSing");
            ActivityOptions opt = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
            startActivity(intent, opt.toBundle());
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        callSingUp.setOnClickListener(v -> {
            if (!validateName() | !validateUsername() | !validateMail() | !validatePass() | !validateUsername()) {
                return;
            }

            if (!isConnected(this)) {
                noInternetDialog();
                return;
            }
            auth = FirebaseAuth.getInstance();

            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("user");
            UserHelper helper = new UserHelper(name, username, mail, phone, password);
            reference.child(username.getEditText().getText().toString()).setValue(helper);
            Intent intent = new Intent(SignUp.this, FirstLogin.class);
            startActivity(intent);
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();

                float valX = x2 - x1;

                if (Math.abs(valX) > MIN_DISTANCE) {
                    if (x2 > x1) {
                        Intent intent = new Intent(SignUp.this, Login.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    private Boolean validateName() {
        String val = name.getEditText().getText().toString();
        if (val.isEmpty()) {
            name.setError("Name cannot be empty");
            return false;
        }
        name.setError(null);
        name.setErrorEnabled(false);
        return true;
    }


    private Boolean validateMail() {
        String val = mail.getEditText().getText().toString();
        if (val.isEmpty()) {
            mail.setError("E-Mail cannot be empty");
            return false;
        }
        mail.setError(null);
        mail.setErrorEnabled(false);
        return true;
    }

    private Boolean validatePass() {
        String passVal = "^" +
                //"(?=.*[0-9])" +
                "(?=.*[a-zA-Z])" +
                //"(?=.*[@#$%^&+=!])" +
                "(?=\\S+$)" +
                ".{4,}$";
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Password cannot be empty");
            return false;
        }
        password.setError(null);
        password.setErrorEnabled(false);
        return true;
    }

    private Boolean validateUsername() {
        String space = "\\A\\w{4,20}\\z";
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Username cannot be empty");
            return false;
        }
        if (val.length() >= 20) {
            username.setError("Username too long");
            return false;
        }
        if (!val.matches(space)) {
            username.setError("White Space not allowed");
            return false;
        }
        username.setError(null);
        username.setErrorEnabled(false);
        return true;
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

    private boolean isConnected(SignUp signUp) {
        ConnectivityManager connectivityManager = (ConnectivityManager) signUp.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected()))
            return true;
        else
            return false;
    }
}