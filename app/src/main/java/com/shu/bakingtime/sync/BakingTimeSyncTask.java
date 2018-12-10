package com.shu.bakingtime.sync;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.shu.bakingtime.R;
import com.shu.bakingtime.database.RecipeDatabase;
import com.shu.bakingtime.model.Recipe;
import com.shu.bakingtime.utilities.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BakingTimeSyncTask {

    public static final String TAG = BakingTimeSyncTask.class.getSimpleName();

    synchronized public static void syncRecipes(final Context context){

        if(!NetworkUtils.isOnline(context)) {
            Log.i(TAG, "No network. Sync halted.");
            return;
        }

        NetworkUtils.getBakingTimeService().getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {

                    final RecipeDatabase database = RecipeDatabase.getInstance(context);

                    if(response.body() != null){
                        final List<Recipe> recipes = response.body();

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                for (Recipe recipe: recipes
                                        ) {
                                    database.recipesDao().insert(recipe);
                                }
                            }
                        });


                    }

                } else{
                    Log.i(TAG, "BakingTime recipe loading did not succeed, response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "BakingTime recipe loading error: " + t);
            }
        });
    }
}
