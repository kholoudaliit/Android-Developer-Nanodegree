package com.kholoud.popularmovies.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kholoud.popularmovies.BuildConfig;
import com.kholoud.popularmovies.R;
import com.kholoud.popularmovies.adapter.EmptyAdapter;
import com.kholoud.popularmovies.adapter.MoviesAdapter;
import com.kholoud.popularmovies.moivesdbapi.models.Movie;
import com.kholoud.popularmovies.moivesdbapi.models.MoviesList;
import com.kholoud.popularmovies.utils.InternetCheck;
import com.kholoud.popularmovies.utils.Singletone;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {
    private static final String MY_PREFS_NAME = "MoviePref";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private MoviesList moives;
    private TextView emptyView;
    private MoviesList moviesList;
    private SharedPreferences.Editor editor;
    String savedQueryType;

    private static Gson gson;
    public static String moivesdbBaseURL = "http://api.themoviedb.org/3/movie/";
    public static String moivesdb_apiKey = BuildConfig.moviedb_apikey; //remove for legal issues
    public static String moviedbURL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Views
        initViews();

        // init Gson object for pare JSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        // get saved option for SharedPrefrences
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        savedQueryType = prefs.getString("sortOption", "popular");// "popular" is the default value.

        // RecycleView stuff
        recyclerView.setHasFixedSize(true);
        // use a linear GrideLayout manager
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        //refresh Listener
        refreshLayout.setOnRefreshListener(() -> {
            getMovies(savedQueryType);
            refreshLayout.setRefreshing(false);
        });

        // check internet connection
        // then get Movies and set adapter
        new InternetCheck(isConnected -> {
            if (isConnected) {
                getMovies(savedQueryType);

            } else {
                Snackbar.make(findViewById(android.R.id.content), "No Internet Connection, Please try again", Snackbar.LENGTH_LONG).show();
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new EmptyAdapter()); // for enable pullToRefresh behavoirs in empty list
            }

        });



    }

    private void initViews() {
        refreshLayout = findViewById(R.id.refresh_layout);
        emptyView = findViewById(R.id.list_empty);
        recyclerView = findViewById(R.id.my_recycler_view);

    }


    public MoviesList getMovies(String QUERY_TYPE) {

        moviedbURL = moivesdbBaseURL + QUERY_TYPE + "?api_key=" + moivesdb_apiKey;
        moviesList = new MoviesList();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, moviedbURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        MoviesList moviesList = gson.fromJson(String.valueOf(response), MoviesList.class);
                        Log.d("Response: ", response.toString());
                        mAdapter = new MoviesAdapter(moviesList.getResults() , MainActivity.this::onListItemClick);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        emptyView.setVisibility(View.INVISIBLE);

                    }
                }, error -> {
                    //TODO
                });

        // Access the RequestQueue through  singleton class.
        Singletone.getInstance(this).addToRequestQueue(jsonObjectRequest);

        return moviesList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_popular_movies:
                getMovies("popular");
                //save the option to SharedPrefrences
                editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("sortOption", "popular");
                editor.apply();
                return true;
            case R.id.action_top_rated:
                getMovies("top_rated");
                //save the option to SharedPrefrences
                editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("sortOption", "top_rated");
                editor.apply();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(Movie selectedMovie) {
        Intent movieDeatail = new Intent(this, MovieActivity.class);
        movieDeatail.putExtra("selectedMovie", selectedMovie);
        startActivity(movieDeatail);

    }
}
