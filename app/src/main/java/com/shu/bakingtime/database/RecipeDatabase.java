package com.shu.bakingtime.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.shu.bakingtime.model.Recipe;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DB_NAME = "recipe_db";
    private static RecipeDatabase sDbInstance;

    public static RecipeDatabase getInstance(Context c) {
        if (sDbInstance == null) {
            synchronized (LOCK) {
                sDbInstance = Room.databaseBuilder(c.getApplicationContext()
                        , RecipeDatabase.class
                        , RecipeDatabase.DB_NAME)
                        //.allowMainThreadQueries()
                        .build();
            }
        }
        return sDbInstance;
    }

    public abstract RecipesDao recipesDao();
}
