package com.shu.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.shu.bakingtime.dummy.DummyContent;
import com.shu.bakingtime.model.Recipe;
import com.shu.bakingtime.model.Step;

import org.parceler.Parcels;

import java.util.List;

import static com.shu.bakingtime.RecipesActivity.EXTRA_RECIPE;

public class RecipeStepsActivity extends AppCompatActivity {

    public static final String TAG = RecipeStepsActivity.class.getSimpleName();

    private boolean mTwoPane;

    private Recipe mRecipeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_recipe_steps);

        if(getIntent().hasExtra(EXTRA_RECIPE)){
            Parcelable p = getIntent().getParcelableExtra(EXTRA_RECIPE);
            mRecipeData = Parcels.unwrap(p);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(mRecipeData.getName());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.recipe_step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.recipe_step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecipeStepsRecyclerViewAdapter(this, mRecipeData.getSteps(), mTwoPane));
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

    public static class RecipeStepsRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeStepsRecyclerViewAdapter.ViewHolder> {

        private final RecipeStepsActivity mParentActivity;
        private final List<Step> mSteps;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step stepData = (Step) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(RecipeStepDetailFragment.ARG_ITEM_ID, Parcels.wrap(stepData));
                    RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                    intent.putExtra(RecipeStepDetailFragment.ARG_ITEM_ID, Parcels.wrap(stepData));

                    context.startActivity(intent);
                }
            }
        };

        RecipeStepsRecyclerViewAdapter(RecipeStepsActivity parent,
                                       List<Step> steps,
                                       boolean twoPane) {
            mSteps = steps;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_steps_item_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mStepId.setText("#" + Integer.toString(mSteps.get(position).getId()) + ":");
            holder.mStepDescription.setText(mSteps.get(position).getShortDescription());

            holder.itemView.setTag(mSteps.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mStepId;
            final TextView mStepDescription;

            ViewHolder(View view) {
                super(view);
                mStepId = (TextView) view.findViewById(R.id.tv_step_id);
                mStepDescription = (TextView) view.findViewById(R.id.tv_step_description);
            }
        }
    }
}
