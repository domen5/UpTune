package com.uptune.Account;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.uptune.Catalog.Catalog;
import com.uptune.MainActivity;
import com.uptune.Navigation.SpaceTab;
import com.uptune.R;
import com.uptune.Search.SearchAlbum;
import com.uptune.SessionAccount;

import java.io.ByteArrayOutputStream;

public class Account extends Fragment {

    Dialog dialog;
    MaterialCardView btnSettings, btnSell, btnSearch, btnMyFiles, btnRating;
    TextView accountName, accountMail;
    ShapeableImageView accountImg;
    Uri tmpImg;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("user");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton logout = view.findViewById(R.id.btn_logout);
        btnSell = view.findViewById(R.id.btn_sell);
        btnSettings = view.findViewById(R.id.btn_settings);
        btnRating = view.findViewById(R.id.btn_rating);
        btnSearch = view.findViewById(R.id.btn_search);
        btnMyFiles = view.findViewById(R.id.btn_my_files);
        accountImg = view.findViewById(R.id.account_change_img);
        accountName = view.findViewById(R.id.account_username_txt);
        accountMail = view.findViewById(R.id.account_mail_txt);

        //Set account data
        accountName.setText(SessionAccount.getName());
        accountMail.setText(SessionAccount.getMail());
        //change img
        accountImg.setOnClickListener(v -> openFileChooser());

        //button event
        btnMyFiles.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), MyFiles.class);
            startActivity(intent);
        });
        btnSettings.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });
        btnSearch.setOnClickListener(e -> {
            Fragment fr = new Catalog();
            SwitchFragment(fr);
        });
        btnSell.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), SellActivity.class);
            startActivity(intent);
        });
        btnRating.setOnClickListener(e -> {
            Toast.makeText(getContext(), "5", Toast.LENGTH_SHORT).show();
        });


        dialog = new Dialog(getContext());
        logout.setOnClickListener(v -> {
            openLogoutDialog();
        });
    }

    private void SwitchFragment(Fragment fr) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.account_frag, fr).commit();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            tmpImg = data.getData();
            //set data in db
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
            Query checkUser = reference.orderByChild("username").equalTo(SessionAccount.getUsername());

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child(SessionAccount.getUsername()).child("img").getValue(String.class);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "DB PROBLEM", Toast.LENGTH_LONG).show();
                }
            });
            //Upload img view
            Picasso.get().load(tmpImg).into(accountImg);
            //Upload in DB
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("upload/" +
                    "").child(System.currentTimeMillis() + "." + getFilesExtension(tmpImg));
            ref.putFile(tmpImg).addOnSuccessListener(taskSnapshot -> {
                Model model = new Model(tmpImg.toString());
                String id = root.push().getKey();
                root.child(id).setValue(model);
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_LONG).show();
            }).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Loading" + progress + "%");
            });
        }
    }

    private String getFilesExtension(Uri tmpImg) {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(tmpImg));

    }

    private void openLogoutDialog() {
        dialog.setContentView(R.layout.dialog_no_connection);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnCancel = dialog.findViewById(R.id.cancel);
        TextView txt = dialog.findViewById(R.id.txtDialog);
        Button btnConfirm = dialog.findViewById(R.id.confirm);
        btnConfirm.setText("Exit");
        txt.setText("Are you sure to log out?");
        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}

class Model {
    private String imgUrl;

    public Model() {

    }

    public Model(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}