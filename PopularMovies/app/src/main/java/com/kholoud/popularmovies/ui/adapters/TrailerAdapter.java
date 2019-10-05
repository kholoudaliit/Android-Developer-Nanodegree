package com.kholoud.popularmovies.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kholoud.popularmovies.R;
import com.kholoud.popularmovies.data.models.Trailer;
import com.kholoud.popularmovies.utils.Singletone;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    final private ListItemClickListener mOnClickListener;
    Context context;
    private List<Trailer> trailerList;

    public TrailerAdapter(List<Trailer> trailerList, ListItemClickListener onClickListener) {
        this.trailerList = trailerList;
        mOnClickListener = onClickListener;
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.trailer_item_layout, parent, false);

        return new TrailerAdapter.TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position) {
        Trailer trailer = trailerList.get(position);
        //Image loader
        ImageLoader imageLoader = Singletone.getInstance(context).getImageLoader();
        imageLoader.get("https://img.youtube.com/vi/" + trailer.getKey() + "/sddefault.jpg", new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    //loadingView gone
                } else {
                    //some code
                }
            }
        });
        holder.trailer_image.setImageUrl("https://img.youtube.com/vi/" + trailer.getKey() + "/sddefault.jpg", imageLoader);
    }

    @Override
    public int getItemCount() {
        return trailerList != null ? trailerList.size() : 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(Trailer clickedtrailer);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public NetworkImageView trailer_image;

        public TrailerViewHolder(View view) {
            super(view);
            trailer_image = view.findViewById(R.id.trailer_img);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            Trailer clickedTrailer = trailerList.get(getAdapterPosition());
            mOnClickListener.onListItemClick(clickedTrailer);
        }
    }

}


