package com.uptune.Account;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uptune.History.History;
import com.uptune.MainActivity;
import com.uptune.R;
import com.uptune.SessionAccount;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Account extends Fragment {

    Dialog dialog;
    String urlSettings;
    MaterialCardView btnSettings, btnSell, btnBought, btnMyFiles, btnRating;
    TextView accountName, accountMail;
    ShapeableImageView accountImg;
    TextView nReview, nBought, nSold;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("user");
    private String username;

    private final int PERMISSION_CODE = 1001;
    private final int IMAGE_PICK_CODE = 1000;
    private URL userImgUpload;

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
        btnBought = view.findViewById(R.id.btn_search);
        btnMyFiles = view.findViewById(R.id.btn_my_files);
        accountImg = view.findViewById(R.id.account_change_img);
        accountName = view.findViewById(R.id.account_username_txt);
        accountMail = view.findViewById(R.id.account_mail_txt);
        nReview = view.findViewById(R.id.account_review_written);
        nBought = view.findViewById(R.id.account_bought);
        nSold = view.findViewById(R.id.account_sold);
        //Set account data
        accountName.setText(SessionAccount.getName());
        accountMail.setText(SessionAccount.getMail());

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        this.username = SessionAccount.getUsername();
        fetchData(rootRef);


        //change img
        accountImg.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permission, PERMISSION_CODE);
                } else openFileChooser();
            } else
                openFileChooser();
        });

        // region button event
        btnMyFiles.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), MyFiles.class);
            startActivity(intent);
        });
        btnSettings.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            intent.putExtra("bitmap", urlSettings);
            startActivity(intent);
        });
        btnBought.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), History.class);
            startActivity(intent);
        });
        btnSell.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), SellActivity.class);
            startActivity(intent);
        });
        btnRating.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), MyReviewsActivity.class);
            startActivity(intent);
        });
        //endregion
        dialog = new Dialog(getContext());
        logout.setOnClickListener(v -> openLogoutDialog());
    }

    private void fetchData(DatabaseReference rootRef) {
        DatabaseReference used = rootRef.child("lookupUsed").child(username);
        DatabaseReference history = rootRef.child("history").child(username);
        DatabaseReference review = rootRef.child("lookupReview").child(username);
        history.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nBought.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        used.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                nSold.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        review.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nReview.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        root.child(username).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        SessionAccount account = dataSnapshot.getValue(SessionAccount.class);
                        String name = account.getImg();
                        urlSettings = name;
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(name).getContent());
                            accountImg.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openFileChooser();
            else
                Toast.makeText(getContext(), "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            accountImg.setImageURI(data.getData());
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("user/").child(System.currentTimeMillis() + "." + getFilesExtension(data.getData()));
            ref.putFile(data.getData()).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                result.addOnSuccessListener(uri -> {
                    try {
                        userImgUpload = new URL(uri.toString());
                        urlSettings = uri.toString();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
                    Query checkUser = reference.orderByKey().equalTo(username);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user").child(username);
                                mDatabase.child("img").setValue(userImgUpload.toString());
                                try {
                                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(userImgUpload.toString()).getContent());
                                    accountImg.setImageBitmap(bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else
                                Toast.makeText(getContext(), "NULL", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), "DB PROBLEM", Toast.LENGTH_LONG).show();
                        }
                    });

                });
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

    @Override
    public void onResume() {
        super.onResume();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        fetchData(rootRef);
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