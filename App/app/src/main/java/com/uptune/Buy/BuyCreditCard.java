package com.uptune.Buy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.craftman.cardform.CardForm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uptune.Navigation.SpaceTab;
import com.uptune.R;
import com.uptune.SessionAccount;

public class BuyCreditCard extends AppCompatActivity {

    String price, id, type, img, name;

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
        name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");
        img = intent.getStringExtra("img");
        CardForm cardForm = findViewById(R.id.card_form);
        TextView txtDesc = findViewById(R.id.payment_amount);
        txtDesc.setText(price + "€");
        Button btn = findViewById(R.id.btn_pay);
        btn.setText("Confirm and Pay");
        Log.i("TAPPI", name);
        cardForm.setPayBtnClickListner(card -> {
            //CARD N.O. 4111 1111 4555 1142
            //Record in db
            HistoryBought historyBought = new HistoryBought(name, price, img, type);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("history");
            reference.child(SessionAccount.getUsername()).push().setValue(historyBought);

            //Remove record from DB
            if (type.equals("used")) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("used");
                Query applesQuery = ref.orderByKey().equalTo(id);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                //Create notification & alert
            }

            reference = FirebaseDatabase.getInstance().getReference("owned");
            switch (name) {
                case "Exodus":
                    reference.child(SessionAccount.getUsername()).push().setValue("/Exodus");
                    break;
                case "Shoot For The Stars Aim For The Moon":
                    reference.child(SessionAccount.getUsername()).push().setValue("/Shoot the stars aim for the moon");
                    break;
                case "Music To Be Murdered By - Side B (Deluxe Edition)":
                    reference.child(SessionAccount.getUsername()).push().setValue("/Music to be murder by");
                    break;
                case "American You":
                    reference.child(SessionAccount.getUsername()).push().setValue("/Yela");
                    break;
                case "King's Disease":
                    reference.child(SessionAccount.getUsername()).push().setValue("/King's disease");
                    break;
            }
            Intent accountIntent = new Intent(getApplicationContext(), SpaceTab.class);
            startActivity(accountIntent);
            overridePendingTransition(R.anim.static_anim, R.anim.zoom_out);
            this.finish();
        });
    }
}