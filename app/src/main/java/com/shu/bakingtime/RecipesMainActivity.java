package com.shu.bakingtime;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.shu.bakingtime.model.Recipe;
import com.shu.bakingtime.sync.RecipeSyncService;
import com.shu.bakingtime.utilities.UIUtils;
import com.shu.bakingtime.utilities.jsonUtil;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class RecipesMainActivity extends AppCompatActivity {

    private static final String TAG = RecipesMainActivity.class.getSimpleName();
    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";
    public static final String KEY_BAKING_TIME_RECIPES_CNT = "key_baking_time_recipes_cnt";
    public final static String KEY_BAKING_TIME_LAST_RECIPE_ID = "key_baking_time_last_recipe_id";
    public final static String SHARED_PREF_BAKING_TIME = "shared_pref_baking_time";

    private RecipesRecyclerViewAdapter mRecipesAdapter;
    private RecipesViewModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_recipes_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView mRecycleView = findViewById(R.id.recipe_list_container);
        mRecycleView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.columns)));

        final List<Recipe> recipeList = new ArrayList<>();
        mRecipesAdapter = new RecipesRecyclerViewAdapter(this, recipeList);
        mRecycleView.setAdapter(mRecipesAdapter);

        mModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        mModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes == null || recipes.size() == 0) {
                    Log.i(TAG, "no recipes found. Starting db sync.");
                    mModel.loadRecipes();
                } else
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

    private void onRecipesAdapterViewHolderClick(int position) {

        updateLastRecipe(position);
        createStepsActivity(position);
    }

    private void updateLastRecipe(int position) {

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_BAKING_TIME, this.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(KEY_BAKING_TIME_LAST_RECIPE_ID, position);
        edit.putInt(KEY_BAKING_TIME_RECIPES_CNT, mRecipesAdapter.getItemCount());
        edit.commit();

        RecipeSyncService.StartRecipeUpdate(this);

    }

    private void createStepsActivity(int position) {
        Intent i = new Intent(this, RecipeActivity.class);
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

        final List<Recipe> mData;
        final RecipesMainActivity mListener;

        RecipesRecyclerViewAdapter(RecipesMainActivity parent, List<Recipe> data) {
            mData = data;
            mListener = parent;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipes_main_item_content, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            viewHolder.mContentView.setText(mData.get(i).getName());
            viewHolder.mAdditionalInfoView.setText(mListener.getString(R.string.str_service) + mData.get(i).getServings());
            
            if (mData.get(i).getImage() == null || mData.get(i).getImage().equals("")) {
                
                viewHolder.mImage.setImageDrawable(UIUtils.createInitialLetterDrawable(
                        UIUtils.getFirstLetter(mData.get(i).getName()), mListener.getApplicationContext()));
                
            } else {
                Picasso.get().load(mData.get(i).getImage()).centerCrop().fit().into(viewHolder.mImage);
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        void refreshData(List<com.shu.bakingtime.model.Recipe> data) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final TextView mAdditionalInfoView;
            final TextView mContentView;
            final ImageView mImage;

            ViewHolder(View view) {
                super(view);
                mAdditionalInfoView = view.findViewById(R.id.tv_recipe_servings);
                mContentView = view.findViewById(R.id.tv_recipe_name);
                mImage = (ImageView) view.findViewById(R.id.recipe_image);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                mListener.onRecipesAdapterViewHolderClick(getAdapterPosition());
            }
        }

    }
}
