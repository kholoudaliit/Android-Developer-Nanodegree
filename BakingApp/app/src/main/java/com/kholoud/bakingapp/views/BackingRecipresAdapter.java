package com.kholoud.bakingapp.views;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kholoud.bakingapp.R;
import com.kholoud.bakingapp.model.Recipe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BackingRecipresAdapter extends RecyclerView.Adapter<BackingRecipresAdapter.MyViewHolder> {

    final private ListItemClickListener mOnClickListener;
    private Context context;
    private List<Recipe> recpiesList;

    BackingRecipresAdapter(ListItemClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.recipe_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Recipe recipe = recpiesList.get(position);
        holder.title.setText(recipe.getName() +"");

        if (recipe.getImage().isEmpty()) {

            switch (recipe.getName()){
                case "Nutella Pie":
                    recipe.setImage("https://butternutbakeryblog.com/wp-content/uploads/2019/04/no-bake-nutella-pie-768x1368.jpg");
                    break;

                case "Brownies":
                    recipe.setImage("https://thestayathomechef.com/wp-content/uploads/2018/02/Best-Brownies-1-small.jpg");
                    break;

                case "Yellow Cake":
                    recipe.setImage("https://i2.wp.com/grandbaby-cakes.com/wp-content/uploads/2014/03/Yellow-Cake-Recipe-With-Chocolate-Frosting-in-book.jpg");
                    break;
                case "Cheesecake":
                    recipe.setImage("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2009/5/27/0/IGSP01_25282_s4x3.jpg.rend.hgtvcom.826.620.suffix/1485531666198.jpeg");

                    break;

            }
        }

        Picasso.with(context).load(recipe.getImage()).into(holder.recipeImage, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                if (holder.progressBar != null) {
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
            }
        });

    }

    void setRecipes(List<Recipe> recipes) {
        this.recpiesList = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recpiesList != null ? recpiesList.size() : 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(Recipe clickedMovie);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView recipeImage;
        ProgressBar progressBar ;

        MyViewHolder(View view) {
            super(view);
              title = view.findViewById(R.id.recipe_name);
              recipeImage = view.findViewById(R.id.recipe_img);
              progressBar =view.findViewById(R.id.progressBar);

           itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            Recipe clickedRecipe = recpiesList.get(getAdapterPosition());
            mOnClickListener.onListItemClick(clickedRecipe);
        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

}


