package com.uptune.Account;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uptune.Helper.CaptureAct;
import com.uptune.R;
import com.uptune.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class SellActivity extends AppCompatActivity {

    MaterialButton scan, sell;
    TextInputLayout album, artist, label, manufacturer, comment, price;
    ShapeableImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    /*    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sell);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
*/
        setContentView(R.layout.activity_sell);

        album = findViewById(R.id.sell_album_name);
        artist = findViewById(R.id.sell_artist_name);
        label = findViewById(R.id.sell_label);
        manufacturer = findViewById(R.id.sell_manufacturer);
        comment = findViewById(R.id.sell_comment);
        price = findViewById(R.id.sell_price);
        img = findViewById(R.id.sell_add_img);
        scan = findViewById(R.id.scan_code);
        scan.setOnClickListener(v -> {
            scanCode();
        });

        sell = findViewById(R.id.btn_sell_now);
        sell.setOnClickListener(v -> {

        });
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan album code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult res = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (res != null) {
            if (res.getContents() != null) {
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setMessage(res.getContents());
                try {
                    JSONObject current = Web.getBarCodeStuff(res.getContents()).getJSONObject(0);
                    if (!current.getString("category").contains("Music")) {
                        //ERROR
                        build.setTitle("ERROR SCANNING!");
                        build.setMessage("It seems like the product is not a cd...");
                        build.setPositiveButton("Scan again", (dialog, which) -> scanCode())
                                .setNegativeButton("finish", (dialog, which) -> closeContextMenu());
                        AlertDialog dialog = build.create();
                        dialog.show();
                    } else {
                        String productName = current.getString("title");
                        String productLabel = current.getString("label");
                        String productArtist = current.getString("artist");
                        String productDescription = current.getString("description");
                        String productManufacturer = current.getString("manufacturer");
                        URL productImg = new URL(current.getJSONArray("images").getString(0));
                        String price = current.getJSONArray("stores").getJSONObject(0).getString("store_price");
                        setSellTexts(productName, productLabel, productArtist, productDescription, productManufacturer, price, productImg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(this, "no res", Toast.LENGTH_SHORT).show();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void setSellTexts(String productName, String productLabel, String productArtist, String productDescription, String productManufacturer, String price, URL productImg) {
        this.album.getEditText().setText(productName);
        this.label.getEditText().setText(productLabel);
        this.artist.getEditText().setText(productArtist);
        this.comment.getEditText().setText(productDescription);
        this.manufacturer.getEditText().setText(productManufacturer);
        this.price.getEditText().setText(price);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) productImg.getContent());
            this.img.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}