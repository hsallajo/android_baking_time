package com.shu.bakingtime.sync;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import com.shu.bakingtime.widget.BakingTimeWidget;
import com.shu.bakingtime.database.RecipeDatabase;
import com.shu.bakingtime.model.Recipe;
import static com.shu.bakingtime.RecipesMainActivity.KEY_BAKING_TIME_LAST_RECIPE_ID;
import static com.shu.bakingtime.RecipesMainActivity.KEY_BAKING_TIME_RECIPES_CNT;
import static com.shu.bakingtime.RecipesMainActivity.SHARED_PREF_BAKING_TIME;

public class RecipeSyncService extends IntentService {
    final static String ACTION_UPDATE_RECIPE = "com.shu.baking_time.action.update_recipe";
    public static final String TAG = RecipeSyncService.class.getSimpleName();

    public RecipeSyncService(){
        super("RecipeSyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        handleRecipeUpdate();
    }

    public static void StartRecipeUpdate(Context context){

        Intent intent = new Intent(context, RecipeSyncService.class);
        intent.setAction(ACTION_UPDATE_RECIPE);
        context.startService(intent);

    }

    private void handleRecipeUpdate(){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingTimeWidget.class));
        Recipe recipe = getLastRecipeIngredients();
        BakingTimeWidget.updateRecipeWidget(this, appWidgetManager, recipe, appWidgetIds);

    }

    private Recipe getLastRecipeIngredients(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_BAKING_TIME, Context.MODE_PRIVATE);
        int recipeId = sharedPreferences.getInt(KEY_BAKING_TIME_LAST_RECIPE_ID, 0);
        int recipeCnt = sharedPreferences.getInt(KEY_BAKING_TIME_RECIPES_CNT, 0);

        if(recipeId < 0 && recipeId > recipeCnt)
            return null;

        return requestRecipe(recipeId);
    }

    private Recipe requestRecipe(int recipeId) {

        final RecipeDatabase db = RecipeDatabase.getInstance(this);
        return db.recipesDao().loadRecipe(recipeId + 1 );
    }
}
