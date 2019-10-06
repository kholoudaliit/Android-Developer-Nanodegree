package com.kholoud.popularmovies.ui.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kholoud.popularmovies.R;
import com.kholoud.popularmovies.data.models.Movie;
import com.kholoud.popularmovies.data.models.ReviewsList;
import com.kholoud.popularmovies.data.models.Trailer;
import com.kholoud.popularmovies.data.models.TrailerList;
import com.kholoud.popularmovies.ui.adapters.ReviewsAdapter;
import com.kholoud.popularmovies.ui.adapters.TrailerAdapter;
import com.kholoud.popularmovies.utils.Singletone;
import com.kholoud.popularmovies.viewmodels.MovieViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kholoud.popularmovies.ui.views.MainActivity.moivesdbBaseURL;
import static com.kholoud.popularmovies.ui.views.MainActivity.moivesdb_apiKey;
import static com.kholoud.popularmovies.ui.views.MainActivity.moviedbURL;

public class MovieActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {

    private static final String MY_PREFS_NAME = "MoviePref";
    MovieViewModel viewModel;
    private Movie selectedMovie;
    private Gson gson;
    private TrailerList trailerList;
    private SharedPreferences.Editor editor;
    private ReviewsList reviewsList;

    @BindView(R.id.movie_backdrop)
    NetworkImageView backdrop_im;
    @BindView(R.id.movie_poster_img)
    NetworkImageView poster_img;
    @BindView(R.id.movie_title)
    TextView title;
    @BindView(R.id.movie_rating)
    TextView rating;
    @BindView(R.id.movie_language)
    TextView lang;
    @BindView(R.id.movie_overview)
    TextView overview;
    @BindView(R.id.movie_releasedate)
    TextView releaseDate;
    @BindView(R.id.label_overview)
    TextView reviewLable;
    @BindView(R.id.label_trailers)
    TextView trailerLabel;
    @BindView(R.id.list_reviews)
    RecyclerView reviews;
    @BindView(R.id.list_trailers)
    RecyclerView trailer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get Selected Movie from Intent extras
        Intent intent = getIntent();
        selectedMovie = (Movie) intent.getSerializableExtra("selectedMovie");
        //initialize the ui
        initUI(selectedMovie);


        // init Gson object for pare JSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();


        //Get the movie trailers and reviews
        getMovieReviews(selectedMovie.getId());
        getMovieTrailers(selectedMovie.getId());


        //region show the app title only when AppbarLayout collapsed
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
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
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
//endregion

        // init ViewModel
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
    }

    private void getMovieReviews(Integer id) {

        String QUERY_TYPE = "reviews";
        moviedbURL = moivesdbBaseURL + id + "/" + QUERY_TYPE + "?api_key=" + moivesdb_apiKey;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, moviedbURL, null, response -> {
                    reviewsList = gson.fromJson(String.valueOf(response), ReviewsList.class);
                    if (reviewsList.getResults().isEmpty()) {
                        reviewLable.setVisibility(View.INVISIBLE);
                        reviews.setVisibility(View.INVISIBLE);
                    } else {
                        Log.d("Response: ", response.toString());
                        ReviewsAdapter mAdapter = new ReviewsAdapter(reviewsList.getResults());
                        reviews.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }

                }, error -> {
                    Log.e(MovieActivity.class.getName(), error.getStackTrace().toString());
                });

        // Access the RequestQueue through  singleton class.
        Singletone.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void getMovieTrailers(Integer id) {
        String QUERY_TYPE = "videos";
        moviedbURL = moivesdbBaseURL + id + "/" + QUERY_TYPE + "?api_key=" + moivesdb_apiKey;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, moviedbURL, null, response -> {
                    trailerList = gson.fromJson(String.valueOf(response), TrailerList.class);
                    if (trailerList.getResults().isEmpty()) {
                        trailerLabel.setVisibility(View.INVISIBLE);
                        trailer.setVisibility(View.INVISIBLE);
                    } else {
                        Log.d("Response: ", response.toString());
                        TrailerAdapter mAdapter = new TrailerAdapter(trailerList.getResults(), MovieActivity.this);
                        trailer.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }

                }, error -> {
                    Log.e(MovieActivity.class.getName(), error.getStackTrace().toString());
                });

        // Access the RequestQueue through  singleton class.
        Singletone.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    //region UI Stuff
    private void initUI(Movie selectedMovie) {

        // RecycleView stuff
        // use a linearLayout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reviews.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager layoutManager_ = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailer.setLayoutManager(layoutManager_);
        //set movie data to the detail view
        backdrop_im.setImageUrl("http://image.tmdb.org/t/p/w500" + this.selectedMovie.getBackdropPath(), Singletone.getInstance(this).getImageLoader());
        poster_img.setImageUrl("http://image.tmdb.org/t/p/w500" + this.selectedMovie.getPosterPath(), Singletone.getInstance(this).getImageLoader());
        title.setText(this.selectedMovie.getTitle());
        rating.setText(this.selectedMovie.getVoteAverage().toString());
        lang.setText(this.selectedMovie.getOriginalLanguage().toUpperCase());
        overview.setText(this.selectedMovie.getOverview());

        //Extract year from release date
        String[] dateParts = this.selectedMovie.getReleaseDate().split("-");
        String year = dateParts[0];
        releaseDate.setText(year);

    }
    //endregion

    //region Menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        menu.getItem(1).setIcon(prefs.getInt("Fav_imag", android.R.drawable.btn_star_big_off));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_share_movie:
                //create share Intent
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);

                if (!trailerList.getResults().isEmpty())
                    shareIntent.putExtra(Intent.EXTRA_TEXT, " Watch " + selectedMovie.getTitle() + "  traialer on YouTube: "
                            + "http://www.youtube.com/watch?v=" + trailerList.getResults().get(0).getKey() + "");
                else // if there's no trailers make a YouTube Query :) to find more about the movie
                    shareIntent.putExtra(Intent.EXTRA_TEXT, " Watch " + selectedMovie.getTitle() + "  traialer on YouTube: "
                            + "http://www.youtube.com/results?search_query=" + selectedMovie.getTitle().replaceAll("\\s", "+") + "");

                shareIntent.setType("text/plain");
                Intent shareIntentChooser = Intent.createChooser(shareIntent, null);
                startActivity(shareIntentChooser);
                return true;
            case R.id.action_fav_movie:
                if (selectedMovie.is_Fav()) { // unfavorite
                    item.setIcon(getDrawable(android.R.drawable.btn_star_big_off));
                    //save the Un fav image
                    editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putInt("Fav_imag", android.R.drawable.btn_star_big_off);
                    editor.commit();
                    selectedMovie.setFav(!selectedMovie.is_Fav());
                    // insert movie to local list
                    viewModel.deleteFavMovie(selectedMovie);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "The moive " + selectedMovie.getTitle() + " removed from your favourte list", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else {//Favorite
                    item.setIcon(getDrawable(android.R.drawable.btn_star_big_on));
                    selectedMovie.setFav(!selectedMovie.is_Fav());
                    // insert movie to local list
                    viewModel.insertFavMovie(selectedMovie);
                    //save the Fav icon
                    editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putInt("Fav_imag", android.R.drawable.btn_star_big_on);
                    editor.commit();

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "The moive " + selectedMovie.getTitle() + " added to your favourte list", Snackbar.LENGTH_LONG);
                    snackbar.show();


                }

                //change icon

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(Trailer clickedtrailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + clickedtrailer.getKey())));

    }

    //endregion

}
