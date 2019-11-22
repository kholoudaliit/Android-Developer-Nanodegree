package com.kholoud.bakingapp.views;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.appbar.AppBarLayout;
import com.kholoud.bakingapp.R;
import com.kholoud.bakingapp.model.Step;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoStepsDetailFragment extends Fragment {


    private Step step;
    @BindView(R.id.step_des)
    protected TextView des;
    @BindView(R.id.exo_play)
    protected PlayerView playerView;
    @BindView(R.id.step_img)
    protected ImageView stepImg;
    private FrameLayout.LayoutParams params;
    private ExoPlayer player;
    private String stepDes, stepViedoURL = "", stepThumbnail = "";
    private static final String VIEDO_POSITION = "StepVideoPosition";
    private static final String PLAYER_IS_READY_KEY = "PlsyerReady";
    private long mPosition;
    private boolean playerState;

    public VideoStepsDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        step = new Step();
        setRetainInstance(true);


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state of video position
        outState.putLong(VIEDO_POSITION, mPosition);
        outState.putBoolean(PLAYER_IS_READY_KEY, playerState);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getLong(VIEDO_POSITION);
            playerState = savedInstanceState.getBoolean(PLAYER_IS_READY_KEY);


        }

        SetMedia();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video_steps_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            stepDes = bundle.getString("des");
            stepThumbnail = bundle.getString("thumbnailURL");
            stepViedoURL = bundle.getString("ViedoURL");

        }
        des.setText(stepDes);

        //Handle the Landscape mood
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params = (FrameLayout.LayoutParams) playerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            playerView.setLayoutParams(params);
        }

        return rootView;
    }

    private void SetMedia() {
        if (!stepViedoURL.equals("")) {
            initPlayer(stepViedoURL);
        } else if (!stepThumbnail.equals("")) {
            //initPlayer(step.getThumbnailURL());
            stepImg.setVisibility(View.VISIBLE);
            stepImg.setAlpha((float) 1);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //give YourVideoUrl below
            retriever.setDataSource(stepThumbnail, new HashMap<String, String>());
            // this gets frame at 2nd second
            Bitmap image = retriever.getFrameAtTime(10000000, MediaMetadataRetriever.OPTION_CLOSEST);
            //use this bitmap image
            stepImg.setImageBitmap(image);
        } else {
            stepImg.setVisibility(View.VISIBLE);
        }
    }


    private void initPlayer(String sourceURL) {
        if (player == null) {
            // init the player
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            playerView.setPlayer(player);
            playerView.setVisibility(View.VISIBLE);
        }
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "Baking App "));
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(sourceURL + ""));
        // Prepare the player with the source.
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        player.prepare(videoSource);
        player.seekTo(mPosition);
        player.setPlayWhenReady(playerState);


    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onStop() {
        super.onStop();
        PlayerRelease();


    }

    private void PlayerRelease() {
        if (player != null) {
            mPosition = player.getCurrentPosition();
            playerState = player.getPlayWhenReady();
            player.stop();
            player.release();
            player = null;

        }
    }


}
