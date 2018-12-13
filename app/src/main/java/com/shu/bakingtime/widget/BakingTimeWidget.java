package com.shu.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.shu.bakingtime.R;
import com.shu.bakingtime.RecipesMainActivity;
import com.shu.bakingtime.model.Recipe;
import com.shu.bakingtime.sync.RecipeSyncService;
import com.shu.bakingtime.utilities.jsonUtil;

public class BakingTimeWidget extends AppWidgetProvider {

    public static final String KEY_BAKING_LAST_RECIPE_INGREDIENTS = "key_baking_last_recipe_ingredients";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager
            , Recipe recipe
            , int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_time_widget);

        Intent activityIntent = new Intent(context, RecipesMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
        views.setOnClickPendingIntent(R.id.tv_widget_recipe, pendingIntent);
        views.setTextViewText(R.id.tv_widget_recipe, recipe.getName());

        Intent widgetServiceIntent = new Intent(context, widgetService.class);
        String json = jsonUtil.listToJson(recipe.getIngredients());
        widgetServiceIntent.putExtra(KEY_BAKING_LAST_RECIPE_INGREDIENTS, json);

        views.setRemoteAdapter(R.id.widget_list_view, widgetServiceIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeSyncService.StartRecipeUpdate(context);
    }

    public static void updateRecipeWidget(Context context
            , AppWidgetManager appWidgetManager
            , Recipe recipe, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

