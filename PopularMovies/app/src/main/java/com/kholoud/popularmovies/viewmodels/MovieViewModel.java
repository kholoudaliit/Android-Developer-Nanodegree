package com.kholoud.popularmovies.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kholoud.popularmovies.data.MoviesRoom.MovieRepoistry;
import com.kholoud.popularmovies.data.models.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> favMovies;
    private MovieRepoistry repoistory;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repoistory = new MovieRepoistry(application);
        favMovies = repoistory.getAllFavMovies();
    }

    public void insertFavMovie(Movie movie) {

        repoistory.insert(movie);
    }

    public void deleteFavMovie(Movie movie) {

        repoistory.delete(movie);
    }

    public LiveData<List<Movie>> getAllFavMovie() {

        return favMovies;
    }
}
