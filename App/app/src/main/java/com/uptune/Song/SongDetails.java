package com.uptune.Song;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uptune.Buy.BuyCreditCard;
import com.uptune.R;
import com.uptune.Review.ReviewClass;
import com.uptune.SessionAccount;
import com.uptune.Web;

import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SongDetails extends Fragment {
    private String id, name, imgStr;
    private MediaPlayer mediaPlayer;
    private FloatingActionButton fabPreview;
    MaterialButton btnBottom;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_song_details, container, false);
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Toolbar toolbar = view.findViewById(R.id.songDetailsToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getFragmentManager().popBackStack());


        this.fabPreview = view.findViewById(R.id.songDetailsFabPreview);
        final ImageView imageView = view.findViewById(R.id.songDetailsImg);
        final ImageView imageView2 = view.findViewById(R.id.songDetailsImgBackground);
        final ImageView imageArtist = view.findViewById(R.id.songDetailsArtistImg);
        final TextView txtProductTitle = view.findViewById(R.id.songDetailsBuyTitleDetails);
        final TextView txtArtist = view.findViewById(R.id.songDetailsTitleArtist);
        btnBottom = view.findViewById(R.id.songDetailsBottomBuy);
        btnBottom.setOnClickListener(v -> pay());
        Button submitReview = view.findViewById(R.id.submit_rev);
        submitReview.setOnClickListener(v -> sendReview(view));

//        fabPreview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mediaPlayer.start();
//            }
//        });
        fabPreview.setOnTouchListener((v, motionEvent) -> {
            int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;
            if (action == MotionEvent.ACTION_DOWN) {
                mediaPlayer.start();
            } else if (action == MotionEvent.ACTION_UP) {
                mediaPlayer.stop();
                mediaPlayer.prepareAsync();
            }
            return false;
        });


        final SpotifyApi api = new SpotifyApi();
        api.setAccessToken(Web.getToken());

        final SpotifyService spotify = api.getService();
        spotify.getTrack(this.id, new Callback<Track>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Track track, Response response) {
                try {
                    mediaPlayer.setDataSource(track.preview_url);
                    mediaPlayer.prepareAsync();
                    Log.e("preview", track.preview_url);
                } catch (IOException | NullPointerException e) {
                    CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fabPreview.getLayoutParams();
                    p.setAnchorId(View.NO_ID);
                    fabPreview.setLayoutParams(p);
                    fabPreview.hide();
                    Log.e("preview", "Prewview non disponibile");
                    Toast.makeText(getActivity(), "Preview non disponibile", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
                Log.d("song", track.name);
                txtProductTitle.setText(track.name);
                toolbar.setTitle(track.name);
                name = track.name;
                String artists = track.artists.stream()
                        .map(a -> a.name)
                        .collect(Collectors.joining(", "));
                txtArtist.setText(artists);
                try {
                    final String imagePath = track.album.images.get(0).url;
                    final URL imageUrl = new URL(imagePath);
                    imgStr = imagePath;
                    Bitmap image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                    imageView.post(() -> imageView.setImageBitmap(image));
                    imageView2.post(() -> imageView2.setImageBitmap(image));
                    setArtistImage(track, spotify, imageArtist);
                } catch (Exception e) {
                    Log.e("ERROR", "No image found " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("ERROR", error.getMessage());
            }
        });
    }

    private void sendReview(View view) {
        EditText editText = view.findViewById(R.id.review_edit_text);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        String username = SessionAccount.getUsername();
        String desc = editText.getEditableText().toString();
        String rate = ratingBar.getRating() + "";
        ReviewClass reviewClass = new ReviewClass(username, rate, desc);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("review");
        reference.child(id).push().setValue(reviewClass);
        Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
        Log.i("TAPPI", reviewClass.toString());
    }

    private void pay() {
        Intent i = new Intent(getActivity(), BuyCreditCard.class);
        i.putExtra("price", "1.99");
        i.putExtra("img", imgStr);
        i.putExtra("type", "song");
        i.putExtra("name", name);
        i.putExtra("id", id);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.release();
    }

    private void setArtistImage(Track track, SpotifyService spotify, ImageView imageView) {
        final String artistId = track.artists.get(0).id;
        spotify.getArtist(artistId, new Callback<Artist>() {
            @Override
            public void success(Artist artist, Response response) {
                final String artistUrl = artist.images.get(0).url;
                final URL imageUrl;
                try {
                    imageUrl = new URL(artistUrl);
                    Bitmap image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                    imageView.post(() -> imageView.setImageBitmap(image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.id = getArguments().getString("id");
            this.mediaPlayer = new MediaPlayer();
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Url of the playlist.
     * @return A new instance of fragment charts_selector.
     */
    // TODO: Rename and change types and number of parameters
    public static SongDetails newInstance(String id) {
        SongDetails fragment = new SongDetails();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }
}
