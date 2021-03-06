package com.kholoud.popularmovies.ui.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kholoud.popularmovies.R;
import com.kholoud.popularmovies.data.models.Movie;
import com.kholoud.popularmovies.utils.Singletone;

import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    final private ListItemClickListener mOnClickListener;
    Context context;
    private List<Movie> moviesList;

    public MoviesAdapter(ListItemClickListener onClickListener) {
        //this.moviesList = moviesList;
        mOnClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.moive_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        //Image loader
        ImageLoader imageLoader = Singletone.getInstance(context).getImageLoader();
        imageLoader.get("http://image.tmdb.org/t/p/w500" + movie.getPosterPath(), new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    //loadingView gone
                } else {
                    //some code
                }
            }
        });
        holder.thumbnail.setImageUrl("http://image.tmdb.org/t/p/w342" + movie.getPosterPath(), imageLoader);
        holder.rating.setText(movie.getVoteAverage().toString() + " /10");
        holder.year.setText(movie.getReleaseDate());
    }

    public void setMoives(List<Movie> movies) {
        this.moviesList = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return moviesList != null ? moviesList.size() : 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(Movie clickedMovie);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, rating, year;
        public NetworkImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.moive_name);
            thumbnail = view.findViewById(R.id.poster_img);
            rating = view.findViewById(R.id.moive_rating);
            year = view.findViewById(R.id.moive_year);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            Movie clickedMovie = moviesList.get(getAdapterPosition());
            mOnClickListener.onListItemClick(clickedMovie);
        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

}


