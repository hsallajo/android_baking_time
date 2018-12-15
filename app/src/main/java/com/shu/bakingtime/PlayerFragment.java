package com.shu.bakingtime;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.shu.bakingtime.model.Step;

import org.parceler.Parcels;

import static com.shu.bakingtime.RecipeActivity.EXT_STEP_DATA;

public class PlayerFragment extends Fragment {

    public static final String PLAYER_CURRENT_POSITION = "PLAYER_CURRENT_POSITION";
    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView mNoVideoPlaceHolder;
    private long mPlayerCurrentPosition;

    public PlayerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(EXT_STEP_DATA)) {
            mStep = Parcels.unwrap(getArguments().getParcelable(EXT_STEP_DATA));
        }

        if (savedInstanceState != null) {
            mPlayerCurrentPosition = savedInstanceState.getLong(PLAYER_CURRENT_POSITION);
        } else
            mPlayerCurrentPosition = C.TIME_UNSET;
    }

    private void initializePlayer(Uri uri) {

        if (mExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);

            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "BakingTime");
            MediaSource source = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getContext(), userAgent)
                    , new DefaultExtractorsFactory(), null, null);

            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

            if (mPlayerCurrentPosition != C.TIME_UNSET) {
                Log.d("exo", "initializePlayer: current position " + mPlayerCurrentPosition );
                mExoPlayer.seekTo(mPlayerCurrentPosition);
            }
            mExoPlayer.prepare(source);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerCurrentPosition = mExoPlayer.getCurrentPosition();
            Log.d("exo", "releasing, position now: " + mPlayerCurrentPosition);

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.player, container, false);
        mNoVideoPlaceHolder = rootView.findViewById(R.id.no_video_placeholder);

        if (mStep != null) {
            mPlayerView = rootView.findViewById(R.id.playerView);

        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(mStep.getVideoURL())) {
            hidePlayerView();
            return;
        }

        initializePlayer(Uri.parse(mStep.getVideoURL()));
        showPlayerView();
    }

    private void showPlayerView() {
        mNoVideoPlaceHolder.setVisibility(View.INVISIBLE);
        mPlayerView.setVisibility(View.VISIBLE);
    }

    private void hidePlayerView() {
        mNoVideoPlaceHolder.setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PLAYER_CURRENT_POSITION, mPlayerCurrentPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
