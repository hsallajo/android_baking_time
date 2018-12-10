package com.shu.bakingtime;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.shu.bakingtime.database.RecipeDatabase;
import com.shu.bakingtime.model.Recipe;
import com.shu.bakingtime.sync.BakingTimeSyncService;
import com.shu.bakingtime.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesViewModel extends AndroidViewModel {

    private static final String TAG = RecipesViewModel.class.getSimpleName();
    private LiveData<List<Recipe>> mRecipeList;

    private RecipeDatabase mDatabase;
    private Context context;

    public RecipesViewModel(@NonNull Application application) {
        super(application);

        context = application.getApplicationContext();
        mDatabase = RecipeDatabase.getInstance(context);
        mRecipeList = mDatabase.recipesDao().loadAllRecipes();

    }

    public LiveData<List<Recipe>> getRecipes(){return mRecipeList;}

    public void loadRecipes(){
        Intent i = new Intent(context, BakingTimeSyncService.class);
        context.startService(i);
    }

}
