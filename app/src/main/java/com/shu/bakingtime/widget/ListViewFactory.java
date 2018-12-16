package com.shu.bakingtime.widget;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.shu.bakingtime.R;
import com.shu.bakingtime.model.Ingredient;
import com.shu.bakingtime.utilities.jsonUtil;
import java.util.List;

import static com.shu.bakingtime.widget.BakingTimeWidget.KEY_BAKING_LAST_RECIPE_INGREDIENTS;

public class ListViewFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String TAG = ListViewFactory.class.getSimpleName();
    private final Context context;
    private List<Ingredient> mIngredients;

    public ListViewFactory(Context applicationContext, Intent intent) {
        this.context = applicationContext;

        if(intent.hasExtra(KEY_BAKING_LAST_RECIPE_INGREDIENTS)){

            String json = intent.getStringExtra(KEY_BAKING_LAST_RECIPE_INGREDIENTS);
            mIngredients = jsonUtil.jsonToList(json);

        }

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        // todo
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient);
        v.setTextViewText(R.id.tv_ingredient, mIngredients.get(position).getIngredient());

        String s = jsonUtil.trim( mIngredients.get(position).getQuantity())
                    + " "
                    + mIngredients.get(position).getMeasure();

        v.setTextViewText(R.id.tv_quantity, s);
        return v;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
