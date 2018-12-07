package com.shu.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.shu.bakingtime.utils.BakingTimeUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity {

    public static final String TAG = RecipesActivity.class.getSimpleName();
    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";

    private RecyclerView mRecycleView;
    private RecipesRecyclerViewAdapter mRecipesAdapter;

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

        loadRecipes();

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

    private void loadRecipes() {

        if(!BakingTimeUtils.isOnline(this)) {
            Toast.makeText(this, getString(R.string.msg_no_network), Toast.LENGTH_LONG).show();
            return;
        }

        BakingTimeUtils.getBakingTimeService().getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {

                    mRecipesAdapter.refreshData(response.body());

                } else{
                    Log.d(TAG, "onResponse: response.code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    private void onRecipesAdapterViewHolderClick(int position){
        Toast.makeText(this, "clicked " + position, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, RecipeStepsActivity.class);
        i.putExtra(EXTRA_RECIPE, Parcels.wrap(mRecipesAdapter.mData.get(position)));
        startActivity(i);
    };

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
            loadRecipes();
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
            viewHolder.mIdView.setText(mData.get(i).getName());
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
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.tv_step_id);
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
