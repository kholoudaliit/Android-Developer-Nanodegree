package com.kholoud.popularmovies.data.MoviesRoom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kholoud.popularmovies.data.models.Movie;

import java.util.List;

public class MovieRepoistry {

    private MovieDAO movieDao;
    private LiveData<List<Movie>> favMovies;

    public MovieRepoistry(Application app) {
        MovieDatabase database = MovieDatabase.getInstance(app);
        movieDao = database.movieDao();
        favMovies = movieDao.getFavMovies();
    }

    public void insert(Movie movie) {
        new InsertFavMovieAsyncTask(movieDao).execute(movie);
    }

    public void delete(Movie movie) {
        new deleteFavMovieAsyncTask(movieDao).execute(movie);


    }

    public LiveData<List<Movie>> getAllFavMovies() {

        return favMovies;
    }


    private static class InsertFavMovieAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDAO mDAO;

        public InsertFavMovieAsyncTask(MovieDAO mDAO) {
            this.mDAO = mDAO;
        }


        @Override
        protected Void doInBackground(Movie... movie) {
            mDAO.insertFavMovie(movie[0]);
            return null;
        }
    }

    private static class deleteFavMovieAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDAO mDAO;

        public deleteFavMovieAsyncTask(MovieDAO mDAO) {
            this.mDAO = mDAO;
        }

        @Override
        protected Void doInBackground(Movie... movie) {
            mDAO.deleteFavMovie(movie[0]);
            return null;
        }
    }


}
