package com.shu.bakingtime;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import static com.shu.bakingtime.RecipeActivity.ARG_STEP_DATA;
import static com.shu.bakingtime.RecipeActivity.EXTRA_IS_NEXT_STEP;
import static com.shu.bakingtime.RecipeActivity.EXTRA_IS_PREV_STEP;
import static com.shu.bakingtime.RecipeActivity.EXTRA_NEXT_PREV_CLICK_EVENT;

public class StepActivity extends AppCompatActivity {

    public static final String TAG = StepActivity.class.getSimpleName();

    private boolean mIsPreviousStep;
    private boolean mIsNextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_step);

        mIsPreviousStep = false;
        mIsNextStep = false;

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(EXTRA_IS_NEXT_STEP))
                mIsNextStep = getIntent().getExtras().getBoolean(EXTRA_IS_NEXT_STEP);
            if(getIntent().getExtras().containsKey(EXTRA_IS_PREV_STEP)) {
                mIsPreviousStep = getIntent().getExtras().getBoolean(EXTRA_IS_PREV_STEP);
                Log.d(TAG, "onCreate: mIsPreviousStep is: " + mIsPreviousStep);
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View v = getWindow().getDecorView();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            v.setSystemUiVisibility(v.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            v.setSystemUiVisibility(0);
        }

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(ARG_STEP_DATA,
                    getIntent().getParcelableExtra(ARG_STEP_DATA));

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                InstructionsFragment fragment = new InstructionsFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.instructions_fragment_container, fragment)
                        .commit();

                setupBottomNavigationButtons();
            }

            PlayerFragment playerFragment = new PlayerFragment();
            playerFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_player_fragment_container, playerFragment)
                    .commit();
        }

    }

    private void setupBottomNavigationButtons() {
        Button prev = findViewById(R.id.step_btn_prev);
        prev.setClickable(mIsPreviousStep);
        Button next = findViewById(R.id.step_btn_next);
        next.setClickable(mIsNextStep);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //navigateUpTo(new Intent(this, RecipeActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNextButtonClick(View view) {
        Intent i = new Intent();
        i.putExtra(EXTRA_NEXT_PREV_CLICK_EVENT, 3);
        setResult(RESULT_OK, i);
        finish();
    }

    public void onPrevButtonClick(View view) {
        Intent i = new Intent();
        Log.d(TAG, "onPrevButtonClick: ");
        i.putExtra(EXTRA_NEXT_PREV_CLICK_EVENT, 2);
        setResult(RESULT_OK, i);
        finish();
    }
}
