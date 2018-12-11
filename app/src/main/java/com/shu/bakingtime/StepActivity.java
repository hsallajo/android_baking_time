package com.shu.bakingtime;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

/**
 * An activity representing a single RecipeStep detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeActivity}.
 */
public class StepActivity extends AppCompatActivity {

    public static final String TAG = StepActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_step);

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
            arguments.putParcelable(StepFragment.ARG_STEP_DETAIL,
                    getIntent().getParcelableExtra(StepFragment.ARG_STEP_DETAIL));

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                StepFragment fragment = new StepFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_container, fragment)
                        .commit();
            }

            PlayerFragment playerFragment = new PlayerFragment();
            playerFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_player_container, playerFragment)
                    .commit();
        }
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
}
