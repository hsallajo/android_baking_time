package com.shu.bakingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.shu.bakingtime.model.Ingredient;
import com.shu.bakingtime.sync.RecipeSyncService;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingTimeWidget extends AppWidgetProvider {

    public static final String TAG = BakingTimeWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager
                        , List<Ingredient> ingredients
                        , String recipeName
                        , int appWidgetId) {

        Log.d(TAG, "updateAppWidget:" + recipeName);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_time_widget);

        Intent intent = new Intent(context, RecipesMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_img, pendingIntent);
        views.setTextViewText(R.id.appwidget_text, Integer.toString(ingredients.size()));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate:");
        RecipeSyncService.StartRecipeUpdate(context);
    }

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager
            , List<Ingredient> ingredients, String recipeName, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, ingredients, recipeName, appWidgetId);
        }
    };

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

