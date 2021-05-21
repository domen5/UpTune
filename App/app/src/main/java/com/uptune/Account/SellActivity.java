package com.uptune.Account;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uptune.Helper.CaptureAct;
import com.uptune.Helper.LookupSell;
import com.uptune.Helper.SellHelper;
import com.uptune.R;
import com.uptune.Web;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SellActivity extends AppCompatActivity {

    MaterialButton scan, sell, addImg;
    TextInputLayout album, artist, label, manufacturer, comment, price;
    ShapeableImageView img;
    DatabaseReference reference;
    FirebaseAuth auth;
    URL productImg;
    Toolbar toolbar;
    private final int PERMISSION_CODE = 1001;
    private final int IMAGE_PICK_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        toolbar = (Toolbar) findViewById(R.id.toolbar_sell);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
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
            auth = FirebaseAuth.getInstance();
            reference = FirebaseDatabase.getInstance().getReference("used");
            String name = album.getEditText().getText().toString();
            String label = this.label.getEditText().getText().toString();
            String artist = this.artist.getEditText().getText().toString();
            String desc = this.comment.getEditText().getText().toString();
            String manuf = this.manufacturer.getEditText().getText().toString();
            String price = this.price.getEditText().getText().toString();
            String img = productImg.toString();
            SellHelper sellHelper = new SellHelper(name, label, artist, desc, manuf, price, img, "leleshady"); //"SessionAccount.getUsername()"
            DatabaseReference ref = reference.push();
            ref.setValue(sellHelper);
            String key = ref.getKey();
            LookupSell lookupSell = new LookupSell("SessionAccount.getUsername()", key);
            reference = FirebaseDatabase.getInstance().getReference("lookupUsed");
            reference.child("leleshady").push().setValue(lookupSell);
        });
        addImg = findViewById(R.id.sell_add_img_btn);
        addImg.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permission, PERMISSION_CODE);
                } else pickImage();
            } else
                pickImage();
        });

        img = findViewById(R.id.sell_add_img);
        img.setOnClickListener(v -> {
            //FULLSCREEN IMG
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                pickImage();
            else
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
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
        if (requestCode == IMAGE_PICK_CODE) {
            img.setImageURI(data.getData());
        } else {
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
                            productImg = new URL(current.getJSONArray("images").getString(0));
                            String price = current.getJSONArray("stores").getJSONObject(0).getString("store_price");
                            setSellTexts(productName, productLabel, productArtist, productDescription, productManufacturer, price, productImg);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(this, "no res", Toast.LENGTH_SHORT).show();
            } else
                super.onActivityResult(requestCode, resultCode, data);
        }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}