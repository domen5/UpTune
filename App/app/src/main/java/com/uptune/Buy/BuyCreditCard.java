package com.uptune.Buy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.uptune.R;

public class BuyCreditCard extends AppCompatActivity {

    String price, id, type, img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_credit_card);
        Toolbar toolbar = findViewById(R.id.toolbar_buy);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        Intent intent = getIntent();
        price = intent.getStringExtra("price");
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");
        img = intent.getStringExtra("img");
        CardForm cardForm = findViewById(R.id.card_form);
        TextView txtDesc = findViewById(R.id.payment_amount);
        txtDesc.setText(price + "€");
        Button btn = findViewById(R.id.btn_pay);
        btn.setText("Confirm and Pay");
        cardForm.setPayBtnClickListner(card -> {
            //CARD N.O. 4111 1111 4555 1142
            if (type == "used") {
                Toast.makeText(getApplicationContext(), "EVENT_USED", Toast.LENGTH_SHORT).show();
                //Remove record from DB
                //Create notification & alert
            }
            Toast.makeText(getApplicationContext(), "EVENT", Toast.LENGTH_SHORT).show();
        });
    }
}