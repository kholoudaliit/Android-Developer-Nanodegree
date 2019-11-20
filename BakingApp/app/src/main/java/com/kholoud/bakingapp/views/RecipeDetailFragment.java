package com.kholoud.bakingapp.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kholoud.bakingapp.R;
import com.kholoud.bakingapp.model.Ingredient;
import com.kholoud.bakingapp.model.Recipe;
import com.kholoud.bakingapp.model.Step;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A fragment representing a single items detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link VideoStepsDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    private static final String ARG_ITEM_ID = "recipe";
    Recipe recipe;
    boolean mTwoPane;
    @BindView(R.id.ingredients)
    TextView ingredients;
    @BindView(R.id.steps)
    RecyclerView steps;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the recipe arg from activity
        assert getArguments() != null;
        if (getArguments().containsKey(ARG_ITEM_ID) ) {
            recipe = (Recipe) getArguments().getSerializable("recipe");
            getActivity().setTitle(recipe.getName()+"");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);
        if (rootView.findViewById(R.id.items_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        ingredients.setText(Ingredient.getIngredientsString(recipe.getIngredients()));
        setupRecyclerView(steps);

        return rootView;
    }




    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter((RecipeDetailActivity) this.getActivity(), recipe.getSteps(), mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<Step> steps;
        private RecipeDetailActivity mParentActivity;
        private boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step step = (Step) view.getTag();

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable("step",step );
                    arguments.putString("des", step.getDescription());
                    arguments.putString("thumbnailURL", step.getThumbnailURL());
                    arguments.putString("ViedoURL", step.getVideoURL());
                    VideoStepsDetailFragment fragment = new VideoStepsDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.items_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, VideoStepsDetailActivity.class);
                    intent.putExtra("step", step);


                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeDetailActivity parent, List<Step> items, boolean twoPane) {
            steps = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(steps.get(position).getId()+"");
            holder.mContentView.setText(steps.get(position).getShortDescription());

            holder.itemView.setTag(steps.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {

            return steps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView =  view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}
