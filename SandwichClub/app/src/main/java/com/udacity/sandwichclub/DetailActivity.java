package com.udacity.sandwichclub;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = 0;
        if (intent != null) {
            position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        }
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        ImageView ingredientsIv = findViewById(R.id.image_iv);
        TextView SanName = findViewById(R.id.sanName);
        TextView SanOthersName = findViewById(R.id.also_known_tv);
        TextView SanOthersNameLBL = findViewById(R.id.aslo_known_lbl);
        TextView SanSIngredent = findViewById(R.id.ingredients_tv);
        TextView SanSIngredentLBL = findViewById(R.id.ingrdients_lbl);
        TextView SanOriginPlace = findViewById(R.id.placeOfOrigin);
        TextView SanOriginPlaceLBL = findViewById(R.id.place_lbl);
        TextView SanDesciption = findViewById(R.id.description_tv);

        final ProgressBar progressBar = findViewById(R.id.progressBar_detail);

        // Set the sandwich image
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                    }
                });

        //Set Activity title
        setTitle(sandwich.getMainName());
        //Set Names
        SanName.setText(sandwich.getMainName());
        if (!sandwich.getAlsoKnownAs().isEmpty()) {
            String othersName = String.join(" | ", sandwich.getAlsoKnownAs());
            SanOthersName.setText(othersName);
        } else {
            SanOthersNameLBL.setVisibility(View.INVISIBLE);
            SanOthersName.setVisibility(View.INVISIBLE);
        }

        //Set Origin Place
        if (! sandwich.getPlaceOfOrigin().isEmpty()) {
            SanOriginPlace.setText(sandwich.getPlaceOfOrigin());
        } else {
            SanOriginPlace.setVisibility(View.INVISIBLE);
            SanOriginPlaceLBL.setVisibility(View.INVISIBLE);
        }

        //Set Description
        SanDesciption.setText(sandwich.getDescription());

        //Set ingredients
        if (!sandwich.getIngredients().isEmpty()) {
            String ingredient = String.join(" \n ", sandwich.getIngredients());
            SanSIngredent.setText(ingredient);
        } else {
            SanSIngredentLBL.setVisibility(View.INVISIBLE);
            SanSIngredent.setVisibility(View.INVISIBLE);
        }

    }
}
