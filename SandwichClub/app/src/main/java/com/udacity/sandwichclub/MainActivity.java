package com.udacity.sandwichclub;

import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the full sandwiches list
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        List<Sandwich> sandwichList = new ArrayList<>();
        // fill the pares sandwich obj to list
        for (String sandwichJson :
                sandwiches) {
            Sandwich sandwich = JsonUtils.parseSandwichJson(sandwichJson);
            sandwichList.add(sandwich);
        }
        // custom SandwichAdapter
        SandwichAdapter adapter = new SandwichAdapter(this, sandwichList);

        // Simplification: Using a ListView instead of a RecyclerView
        ListView listView = findViewById(R.id.sandwiches_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchDetailActivity(position);
            }
        });
    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        startActivity(intent);
    }


}

class SandwichAdapter extends ArrayAdapter<Sandwich> {
    protected SandwichAdapter(Context context, List<Sandwich> sandwiches) {
        super(context, 0, sandwiches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Sandwich sandwich = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        ImageView sanImage = convertView.findViewById(R.id.sandwich_img);
        final ProgressBar progressBar = convertView.findViewById(R.id.progressBar);
        TextView sanName = convertView.findViewById(R.id.san_name);
        TextView sanHome = convertView.findViewById(R.id.san_place);
        /* Populate the data into the template view using the data object */

        //load image URL using Picasso
        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);
        if (sandwich.getImage() != null) {
            Picasso.with(getContext()).load(sandwich.getImage()).into(sanImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError() {
                }
            });
        }
        sanName.setText(sandwich.getMainName());
        sanHome.setText(sandwich.getPlaceOfOrigin());
        // Return the completed view to render on screen
        return convertView;
    }
}
