package com.kholoud.popularmovies.views;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.kholoud.popularmovies.R;
import com.kholoud.popularmovies.moivesdbapi.models.Movie;
import com.kholoud.popularmovies.utils.Singletone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

public class MovieActivity extends AppCompatActivity {

    private Movie selectedMovie;
    private NetworkImageView backdrop_im;
    private TextView title;
    private NetworkImageView poster_img;
    private TextView rating;
    private TextView releaseDate;
    private TextView lang;
    private TextView overview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize the ui
        initUI();

        // get Selected Movie from Intent extras
        Intent intent = getIntent();
        selectedMovie = (Movie) intent.getSerializableExtra("selectedMovie");

        //set movie data to the detail view
        backdrop_im.setImageUrl("http://image.tmdb.org/t/p/w500" + selectedMovie.getBackdropPath(), Singletone.getInstance(this).getImageLoader());
        poster_img.setImageUrl("http://image.tmdb.org/t/p/w500" + selectedMovie.getPosterPath(), Singletone.getInstance(this).getImageLoader());
        title.setText(selectedMovie.getTitle());
        rating.setText(selectedMovie.getVoteAverage().toString());
        lang.setText(selectedMovie.getOriginalLanguage().toUpperCase());
        overview.setText(selectedMovie.getOverview());
        //Extract year from release date
        String [] dateParts = selectedMovie.getReleaseDate().split("-");
        String year = dateParts[0];
        releaseDate.setText(year);

        //show the app title only when AppbarLayout collapsed
        final CollapsingToolbarLayout collapsingToolbarLayout =  findViewById(R.id.collapsingToolbarLayout);
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(selectedMovie.getTitle());
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });


    }
    private void initUI() {
        backdrop_im = findViewById(R.id.movie_backdrop);
        poster_img = findViewById(R.id.movie_poster_img);
        title = findViewById(R.id.movie_title);
        rating = findViewById(R.id.movie_rating);
        lang = findViewById(R.id.movie_language);
        overview = findViewById(R.id.movie_overview);
        releaseDate = findViewById(R.id.movie_releasedate);
        backdrop_im = findViewById(R.id.movie_backdrop);

    }

}
