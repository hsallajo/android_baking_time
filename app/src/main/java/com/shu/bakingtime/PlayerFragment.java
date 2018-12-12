package com.shu.bakingtime;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

public class PlayerFragment extends Fragment {

    public static final String ARG_STEP_DETAIL = "item_id";
    public static final String TAG = PlayerFragment.class.getSimpleName();

    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    ImageView mNoVideoPlaceHolder;

    public PlayerFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP_DETAIL)) {
            mStep = Parcels.unwrap(getArguments().getParcelable(ARG_STEP_DETAIL));
        }

    }

    private void initializePlayer(Uri uri) {

        if(mExoPlayer == null){

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);

            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "BakingTime");
            MediaSource source = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getContext(), userAgent)
                    , new DefaultExtractorsFactory(), null, null);

            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

            mExoPlayer.prepare(source);

            mExoPlayer.setPlayWhenReady(true);
        }
    }
    private void releasePlayer(){
        if(mExoPlayer != null){
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

        if(mStep != null){
            mPlayerView = rootView.findViewById(R.id.playerView);
        }

        Uri u = null;
        if(!TextUtils.isEmpty(mStep.getVideoURL())){

            mNoVideoPlaceHolder.setVisibility(View.INVISIBLE);
            u = Uri.parse(mStep.getVideoURL());
            initializePlayer(u);

        } else{
            mNoVideoPlaceHolder.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
