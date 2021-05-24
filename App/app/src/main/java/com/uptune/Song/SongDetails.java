package com.uptune.Song;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.uptune.R;
import com.uptune.Web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SongDetails extends Fragment {
    private String id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_song_details, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Toolbar toolbar = view.findViewById(R.id.songDetailsToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getFragmentManager().popBackStack());

        final ImageView imageView = view.findViewById(R.id.songDetailsImg);
        final ImageView imageView2 = view.findViewById(R.id.songDetailsImgBackground);
        final ImageView imageArtist = view.findViewById(R.id.songDetailsArtistImg);
        final TextView txtProductTitle = view.findViewById(R.id.songDetailsBuyTitleDetails);
        final TextView txtArtist = view.findViewById(R.id.songDetailsTitleArtist);

        final SpotifyApi api = new SpotifyApi();
        api.setAccessToken(Web.getToken());

        final SpotifyService spotify = api.getService();
        spotify.getTrack(this.id, new Callback<Track>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Track track, Response response) {
                Log.d("song", track.name);
                txtProductTitle.setText(track.name);
                toolbar.setTitle(track.name);
                String artists = track.artists.stream()
                        .map(a -> a.name)
                        .collect(Collectors.joining(", "));
                txtArtist.setText(artists);

                try {
                    final String imagePath = track.album.images.get(0).url;
                    final URL imageUrl = new URL(imagePath);
                    Bitmap image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                    imageView.setImageBitmap(image);
                    imageView2.setImageBitmap(image);
                    setArtistImage(track, spotify, imageArtist);
                    //
                    //  *********   SET IMAGE INTO IMAGE VIEW   *********
                    //
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
                    imageView.setImageBitmap(image);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
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
