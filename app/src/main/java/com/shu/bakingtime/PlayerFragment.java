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
    public static final String TAG = PlayerFragment.class.getSimpleName();
    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView mNoVideoPlaceHolder;
    private long mPlayerCurrentPosition;

    private String mUserAgent;
    TrackSelector mTrackSelector;

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

    public void updateStep(Step s){
        mStep = s;

        initializePlayer(Uri.parse(s.getVideoURL()));
        showPlayerView();
    }

    private void initializePlayer(Uri uri) {

        if (mExoPlayer == null) {

            mTrackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), mTrackSelector, loadControl);

            mPlayerView.setPlayer(mExoPlayer);
            mUserAgent = Util.getUserAgent(getContext(), "BakingTime");
        }

        MediaSource source = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getContext(), mUserAgent)
                , new DefaultExtractorsFactory(), null, null);

        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        if (mPlayerCurrentPosition != C.TIME_UNSET) {
            mExoPlayer.seekTo(mPlayerCurrentPosition);
        }
        mExoPlayer.prepare(source);
        mExoPlayer.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerCurrentPosition = mExoPlayer.getCurrentPosition();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + mStep.getId());
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
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + mStep.getId());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + mStep.getId());
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + mStep.getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + mStep.getId());
    }
}
