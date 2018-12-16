package com.shu.bakingtime;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.shu.bakingtime.model.Step;
import org.parceler.Parcels;
import static com.shu.bakingtime.RecipeActivity.CURRENT_STEP_DATA;
import static com.shu.bakingtime.RecipeActivity.EXT_STEP_DATA;
import static com.shu.bakingtime.RecipeActivity.EXT_IS_NEXT_STEP;
import static com.shu.bakingtime.RecipeActivity.EXT_IS_PREV_STEP;
import static com.shu.bakingtime.RecipeActivity.EXT_NEXT_PREV_CLICK_EVENT;

public class StepActivity extends AppCompatActivity {

    private static final String TAG = StepActivity.class.getSimpleName();
    public static final String FRAG_PLAYER_ONE_PANE = "player_fragment_one_pane_layout";

    private boolean mIsPreviousStep;
    private boolean mIsNextStep;
    private Step mCurrentStepData;

    int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orientation = getResources().getConfiguration().orientation;
        setContentView(R.layout.activity_step);

        mIsPreviousStep = false;
        mIsNextStep = false;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (getIntent() != null && getIntent().getExtras() != null) {
                if (getIntent().getExtras().containsKey(EXT_IS_NEXT_STEP))
                    mIsNextStep = getIntent().getExtras().getBoolean(EXT_IS_NEXT_STEP);
                if (getIntent().getExtras().containsKey(EXT_IS_PREV_STEP)) {
                    mIsPreviousStep = getIntent().getExtras().getBoolean(EXT_IS_PREV_STEP);
                }
            }
        }
        if (getIntent().getExtras().containsKey(EXT_STEP_DATA)) {
            Parcelable p = getIntent().getExtras().getParcelable(EXT_STEP_DATA);
            mCurrentStepData = Parcels.unwrap(p);
        }

        if(savedInstanceState != null
                && savedInstanceState.containsKey(CURRENT_STEP_DATA)){
            mCurrentStepData = Parcels.unwrap(savedInstanceState.getParcelable(CURRENT_STEP_DATA));
        }

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toolbar toolbar = findViewById(R.id.detail_toolbar);

            if (mCurrentStepData != null)
                toolbar.setTitle(mCurrentStepData.getShortDescription());
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        View v = getWindow().getDecorView();
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            v.setSystemUiVisibility(0);
        }


        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            TextView tv_instructions_hh = findViewById(R.id.tv_instruction);
            tv_instructions_hh.setText(mCurrentStepData.getDescription());
            setupBottomNavigationButtons();
        }

        setupPlayerFragment();
    }

    private void setupPlayerFragment() {
        Bundle arguments = new Bundle();
        arguments.putParcelable(EXT_STEP_DATA, getIntent().getParcelableExtra(EXT_STEP_DATA));

        PlayerFragment playerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentByTag(FRAG_PLAYER_ONE_PANE);
        if(playerFragment == null) {
            playerFragment = new PlayerFragment();
            playerFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_player_fragment_container, playerFragment, FRAG_PLAYER_ONE_PANE)
                    .commit();
        } else {
            playerFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .detach(playerFragment)
                    .attach(playerFragment)
                    .commit();
        }

    }


    private void setupBottomNavigationButtons() {
        Button prev = findViewById(R.id.step_btn_prev);
        Drawable drawablePrev = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        drawablePrev = DrawableCompat.wrap(drawablePrev);

        if (!mIsPreviousStep) {
            DrawableCompat.setTint(drawablePrev, getResources().getColor(R.color.grey_light));
            prev.setAlpha(.5f);
        } else {
            DrawableCompat.setTint(drawablePrev, getResources().getColor(android.R.color.black));
            prev.setAlpha(1.0f);
        }
        prev.setCompoundDrawablesWithIntrinsicBounds(drawablePrev, null, null, null);
        prev.setEnabled(mIsPreviousStep);

        Button next = findViewById(R.id.step_btn_next);
        Drawable drawableNext = getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp);
        drawableNext = DrawableCompat.wrap(drawableNext);

        if (!mIsNextStep) {
            DrawableCompat.setTint(drawableNext, getResources().getColor(R.color.grey_light));
            next.setAlpha(.5f);
        } else {
            DrawableCompat.setTint(drawableNext, getResources().getColor(android.R.color.black));
            next.setAlpha(1.0f);
        }
        next.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableNext, null);
        next.setEnabled(mIsNextStep);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mCurrentStepData != null) {
            outState.putParcelable(CURRENT_STEP_DATA, Parcels.wrap(mCurrentStepData));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNextButtonClick(View view) {
        Intent i = new Intent();
        i.putExtra(EXT_NEXT_PREV_CLICK_EVENT, 3);
        setResult(RESULT_OK, i);
        finish();
    }

    public void onPrevButtonClick(View view) {
        Intent i = new Intent();
        i.putExtra(EXT_NEXT_PREV_CLICK_EVENT, 2);
        setResult(RESULT_OK, i);
        finish();
    }
}
