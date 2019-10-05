package com.kholoud.popularmovies.data.MoviesRoom;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kholoud.popularmovies.data.models.Movie;

@Database(entities = Movie.class, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    private static MovieDatabase instance;

    public static synchronized MovieDatabase getInstance(Context context) {
        if (instance == null) {

            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class, "Movie_db").fallbackToDestructiveMigration().build();

        }
        return instance;

    }

    public abstract MovieDAO movieDao();

}
