package com.kholoud.bakingapp.views;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.kholoud.bakingapp.R;
import com.kholoud.bakingapp.data.BackingService;
import com.kholoud.bakingapp.data.RetrofitClientInstance;
import com.kholoud.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BackingRecipresAdapter.ListItemClickListener {

    private static final String TAG = "MainActivity";
    private static final String RV_POSITION_INSTANCESTATE = "RECIPE_RECYCLE_STATE";
    private List<Recipe> recipesList;
    private ProgressDialog progressDialog;
    @BindView(R.id.recipeslist)
    RecyclerView recyclerView;
    private BackingRecipresAdapter adapter;
    private Parcelable savedRecyclerLayoutState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(RV_POSITION_INSTANCESTATE);
        }


        // Loading ProgressDialog
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        // Get the baking Recipes -- handle retrofit interface
        loadRecipes();

    }

    private List<Recipe> loadRecipes() {

        BackingService service = RetrofitClientInstance.getRetrofitInstance().create(BackingService.class);
        Call<List<Recipe>> call = service.loadRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipesList = response.body();
                progressDialog.dismiss();
                populateList(recipesList);

                Log.d(TAG, "onResponse: "+ response);
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        return recipesList;
    }

    private void populateList(List <Recipe> BakingRecipes) {
        adapter = new BackingRecipresAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        if (savedRecyclerLayoutState != null) {
            layoutManager.onRestoreInstanceState(savedRecyclerLayoutState);
        }
        adapter.setRecipes(recipesList);

    }

    @Override
    public void onListItemClick(Recipe selectedRecipe) {
        Intent RecipeDeatail = new Intent(this, RecipeDetailActivity.class);
        RecipeDeatail.putExtra("Recipe", selectedRecipe);
        startActivity(RecipeDeatail);

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RV_POSITION_INSTANCESTATE, recyclerView.getLayoutManager().onSaveInstanceState());
    }

}

