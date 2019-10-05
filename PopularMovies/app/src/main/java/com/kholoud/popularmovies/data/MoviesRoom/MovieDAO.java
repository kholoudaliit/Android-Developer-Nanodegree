package com.kholoud.popularmovies.data.MoviesRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kholoud.popularmovies.data.models.Movie;

import java.util.List;

@Dao
public interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavMovie(Movie movie);

    @Delete
    void deleteFavMovie(Movie movie);

    @Query("Select * from MovieDb ORDER BY releaseDate DESC")
    LiveData<List<Movie>> getFavMovies();
}
