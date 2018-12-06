package com.shu.bakingtime;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity {

    public static final String TAG = RecipesActivity.class.getSimpleName();

    private RecyclerView mRecycleView;
    private RecipesRecyclerViewAdapter mRecipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecycleView = (RecyclerView) findViewById(R.id.receipe_list_container);
        mRecycleView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.columns)));

        final List<Recipe> recipeList = new ArrayList<>();
        mRecipesAdapter = new RecipesRecyclerViewAdapter(recipeList);
        mRecycleView.setAdapter(mRecipesAdapter);

        loadRecipes();

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

        public RecipesRecyclerViewAdapter(List<Recipe> data) {
            mData = data;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_content, viewGroup, false);
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

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
