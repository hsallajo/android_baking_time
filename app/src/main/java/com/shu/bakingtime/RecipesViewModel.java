package com.shu.bakingtime;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.shu.bakingtime.database.RecipeDatabase;
import com.shu.bakingtime.model.Recipe;
import com.shu.bakingtime.sync.BakingTimeSyncService;
import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    private final LiveData<List<Recipe>> mRecipeList;
    private final RecipeDatabase mDatabase;

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
