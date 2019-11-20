package com.kholoud.bakingapp.data;

import android.database.Observable;

import com.kholoud.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BackingService {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> loadRecipes();

}
