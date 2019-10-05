package com.kholoud.popularmovies.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kholoud.popularmovies.R;
import com.kholoud.popularmovies.data.models.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    Context context;
    private List<Review> reviewList;

    public ReviewsAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.review_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ReviewsAdapter.MyViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.reviewer.setText(review.getAuthor());
        holder.content.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewer, content;

        public MyViewHolder(View view) {
            super(view);
            reviewer = view.findViewById(R.id.reviewer_name);
            content = view.findViewById(R.id.review_content);


        }


    }


}




