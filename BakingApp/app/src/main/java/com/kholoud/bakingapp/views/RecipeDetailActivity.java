package com.kholoud.bakingapp.views;

import android.app.PendingIntent;
import android.appwidget.AppWidgetHostView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kholoud.bakingapp.R;
import com.kholoud.bakingapp.model.Ingredient;
import com.kholoud.bakingapp.model.Recipe;
import com.kholoud.bakingapp.widget.WidgetData;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link VideoStepsDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Recipe recipe;
    AppWidgetHostView appWidgetManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get Selected Recipe from starter Activity
        Intent starterActivity = getIntent();
        if (starterActivity.hasExtra("Recipe")) {
            recipe = (Recipe) getIntent().getSerializableExtra("Recipe");
            toolbar.setTitle(recipe.getName());

        } else {
            Toast.makeText(this, "Recipe not available", Toast.LENGTH_SHORT).show();
        }


        //AddToWidget FB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sf = getSharedPreferences(WidgetData.IngredientWidgetFile , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sf.edit();
                editor.putString(WidgetData.IngredientList , Ingredient.getIngredientsString(recipe.getIngredients()));
                editor.apply();

                Snackbar.make(view, "Added to Home page widget", Snackbar.LENGTH_LONG)
                        .setAction("Dismiss", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // Add detail fragment
        Bundle arguments = new Bundle();
        arguments.putSerializable("recipe", recipe);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
