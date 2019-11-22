package com.kholoud.bakingapp.views;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.kholoud.bakingapp.R;
import com.kholoud.bakingapp.model.Recipe;
import com.kholoud.bakingapp.model.Step;

/**
 * An activity representing a single items detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeDetailActivity}.
 */
public class VideoStepsDetailActivity extends AppCompatActivity {

    Step step;
    String stepId;
    String stepDes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("step")) {
            step = (Step) getIntent().getSerializableExtra("step");
            stepId = step.getId()+"";
            stepDes= step.getDescription();
        } else {
            Toast.makeText(this, "Recipe not available", Toast.LENGTH_SHORT).show();
        }

        collapsingToolbarLayout.setTitle(stepId + "| " + stepDes);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Full screen when the orientation changes
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            appBarLayout.setVisibility(View.GONE);
            this.getSupportActionBar().hide();

        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null && step != null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putSerializable("step", step);
            arguments.putString("des", step.getDescription());
            arguments.putString("thumbnailURL", step.getThumbnailURL());
            arguments.putString("ViedoURL", step.getVideoURL());
            VideoStepsDetailFragment fragment = new VideoStepsDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.items_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
