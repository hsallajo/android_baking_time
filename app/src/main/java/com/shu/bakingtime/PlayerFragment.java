package com.shu.bakingtime;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
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

import static com.shu.bakingtime.RecipeActivity.CURRENT_STEP_DATA;
import static com.shu.bakingtime.RecipeActivity.EXT_STEP_DATA;

public class PlayerFragment extends Fragment implements ExoPlayer.EventListener{

    public static final String TAG = PlayerFragment.class.getSimpleName();
    public static final String PLAYER_CURRENT_POSITION = "PLAYER_CURRENT_POSITION";
    public static final String PLAYER_PLAY_WHEN_READY = "PLAYER_PLAY_WHEN_READY";
    public static final String PLAYER_CURRENT_WINDOW_INDEX = "PLAYER_CURRENT_WINDOW_INDEX";

    private Step mStep;
    private ImageView mNoVideoPlaceHolder;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private MediaSessionCompat mBakingTimeMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private String mUserAgent;
    private long mPlayerCurrentPosition;
    private int mPlayerCurrentWindowsIndex;
    private boolean mPlayWhenReady = true;

    public PlayerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(EXT_STEP_DATA)) {
            mStep = Parcels.unwrap(getArguments().getParcelable(EXT_STEP_DATA));
        }

        if (savedInstanceState != null) {
            mPlayerCurrentPosition = savedInstanceState.getLong(PLAYER_CURRENT_POSITION);
            mPlayerCurrentWindowsIndex = savedInstanceState.getInt(PLAYER_CURRENT_WINDOW_INDEX);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAYER_PLAY_WHEN_READY);
            mStep = Parcels.unwrap(savedInstanceState.getParcelable(CURRENT_STEP_DATA));
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
    }

    @Override
    public void onResume() {
        super.onResume();

        if (TextUtils.isEmpty(mStep.getVideoURL())) {
            hidePlayerView();
            return;
        }

        setupMediaSession();

        setupPlayer(Uri.parse(mStep.getVideoURL()));
        showPlayerView();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }

        mBakingTimeMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.i(TAG, "Exo media player error " + error);
    }

    private void setupPlayer(Uri uri) {

        Context c = getContext();

        if(c == null)
            return;

        if(mBakingTimeMediaSession==null)
            return;

        if (mExoPlayer == null) {

            DefaultTrackSelector mTrackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(c, mTrackSelector, loadControl);

            mPlayerView.setPlayer(mExoPlayer);
            mUserAgent = Util.getUserAgent(c, getResources().getString(R.string.app_name));

            mExoPlayer.addListener(this);
        }

        MediaSource source = new ExtractorMediaSource(uri
                , new DefaultDataSourceFactory(c, mUserAgent)
                , new DefaultExtractorsFactory()
                , null
                , null);

        if (mPlayerCurrentPosition != C.TIME_UNSET) {
            mExoPlayer.seekTo(mPlayerCurrentWindowsIndex, mPlayerCurrentPosition);
        }

        //mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        //mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        mExoPlayer.prepare(source, false, false);
        mExoPlayer.setPlayWhenReady(mPlayWhenReady);
    }


    private void setupMediaSession(){

        Context c = getActivity();
        if(c==null)
            return;

        mBakingTimeMediaSession = new MediaSessionCompat( c , TAG);
        mBakingTimeMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                      /*  PlaybackStateCompat.ACTION_PAUSE |*/
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        );

        mBakingTimeMediaSession.setMediaButtonReceiver(null);

        mBakingTimeMediaSession.setPlaybackState(mStateBuilder.build());

        mBakingTimeMediaSession.setCallback(new BakingTimePlayerSessionCallback());

        mBakingTimeMediaSession.setActive(true);

    }

    private class BakingTimePlayerSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {

            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
            mBakingTimeMediaSession.setPlaybackState(mStateBuilder.build());
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {

            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
            mBakingTimeMediaSession.setPlaybackState(mStateBuilder.build());

            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }

        @Override
        public void onStop() {
            super.onStop();
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {

            mPlayerCurrentPosition = mExoPlayer.getCurrentPosition();
            mPlayerCurrentWindowsIndex = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();

            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
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
        outState.putInt(PLAYER_CURRENT_WINDOW_INDEX, mPlayerCurrentWindowsIndex);
        outState.putBoolean(PLAYER_PLAY_WHEN_READY, mPlayWhenReady);
        outState.putParcelable(CURRENT_STEP_DATA, Parcels.wrap(mStep));
    }

    @Override
    public void onPause() {
        super.onPause();

        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mBakingTimeMediaSession != null)
            mBakingTimeMediaSession.setActive(false);
    }
}
