package com.shu.bakingtime;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.shu.bakingtime.model.Recipe;
import com.shu.bakingtime.utils.BakingTimeUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesViewModel extends AndroidViewModel {

    private static final String TAG = RecipesViewModel.class.getSimpleName();
    private MutableLiveData<List<Recipe>> recipeList;

    public RecipesViewModel(@NonNull Application application) {
        super(application);

        if(recipeList == null){
            Log.d(TAG, "RecipesViewModel: recipeList=" + recipeList + ". Starting loading.");
            recipeList = new MutableLiveData<>();
            loadRecipes();
        }
    }

    public LiveData<List<Recipe>> getRecipes(){return recipeList;}

    public void refresh(){loadRecipes();}

    private void loadRecipes() {

        Context context = getApplication().getApplicationContext();
        if(!BakingTimeUtils.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.msg_no_network), Toast.LENGTH_LONG).show();
            return;
        }

        BakingTimeUtils.getBakingTimeService().getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {

                    Log.i(TAG, "BakingTime recipe loading completed. ");
                    // mRecipesAdapter.refreshData(response.body());
                    // todo update cache with new data
                    if(response.body() != null){
                        List<Recipe> recipes = response.body();
                        refreshCache(recipes);
                    }

                } else{
                    Log.d(TAG, "BakingTime recipe loading did not succeed, response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "BakingTime recipe loading error: " + t);
            }
        });
    }

    private void refreshCache(List<Recipe> e ){

        if (e == null || e.isEmpty()) {
            return;
        }

        List<Recipe> t = recipeList.getValue();

        if (t == null) {
            t = new ArrayList<>();
        }

        t.clear();
        t.addAll(e);
        recipeList.setValue(t);
    }
}
