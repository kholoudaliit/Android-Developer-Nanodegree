package com.kholoud.popularmovies.adapter;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kholoud.popularmovies.moivesdbapi.models.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kholoud.popularmovies.R;
import com.kholoud.popularmovies.utils.Singletone;

import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;
    final private ListItemClickListener mOnClickListener;
    Context context;

    public interface ListItemClickListener {
        void onListItemClick(Movie clickedMovie);
    }


    public MoviesAdapter(List<Movie> moviesList, ListItemClickListener onClickListener) {
        this.moviesList = moviesList;
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

    @Override
    public int getItemCount() {
        return moviesList != null ? moviesList.size() : 0;
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


}


