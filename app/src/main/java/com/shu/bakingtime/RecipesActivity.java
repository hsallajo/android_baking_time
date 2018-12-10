package com.shu.bakingtime;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shu.bakingtime.model.Recipe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    public static final String TAG = RecipesActivity.class.getSimpleName();
    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";

    private RecyclerView mRecycleView;
    private RecipesRecyclerViewAdapter mRecipesAdapter;
    private RecipesViewModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_recipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecycleView = (RecyclerView) findViewById(R.id.receipe_list_container);
        mRecycleView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.columns)));

        final List<Recipe> recipeList = new ArrayList<>();
        mRecipesAdapter = new RecipesRecyclerViewAdapter(this, recipeList);
        mRecycleView.setAdapter(mRecipesAdapter);

        mModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        mModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes == null || recipes.size() == 0){
                    Log.i(TAG, "no recipes found. Starting db sync.");
                    mModel.loadRecipes();}
                 else
                    mRecipesAdapter.refreshData(recipes);
            }
        });

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

    private void onRecipesAdapterViewHolderClick(int position){

        Toast.makeText(this, "clicked " + position, Toast.LENGTH_SHORT).show();
        createStepsActivity(position);
    };

    private void createStepsActivity(int position){
        Intent i = new Intent(this, RecipeStepsActivity.class);
        i.putExtra(EXTRA_RECIPE, Parcels.wrap(mRecipesAdapter.mData.get(position)));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receipes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            mModel.loadRecipes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class RecipesRecyclerViewAdapter extends RecyclerView.Adapter<RecipesRecyclerViewAdapter.ViewHolder> {

        List<Recipe> mData;
        RecipesActivity mListener;

        public RecipesRecyclerViewAdapter(RecipesActivity parent, List<Recipe> data) {
            mData = data;
            mListener = parent;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_item_content, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.mStepNumberView.setText(mData.get(i).getName());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void refreshData(List<com.shu.bakingtime.model.Recipe> data){
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            final TextView mStepNumberView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mStepNumberView = (TextView) view.findViewById(R.id.tv_step_number);
                mContentView = (TextView) view.findViewById(R.id.tv_step_description);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                mListener.onRecipesAdapterViewHolderClick(getAdapterPosition());
            };
        }
    }
}
