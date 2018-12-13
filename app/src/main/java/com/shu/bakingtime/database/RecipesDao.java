package com.shu.bakingtime.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.shu.bakingtime.model.Recipe;

import java.util.List;

@Dao
public interface RecipesDao {
    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> loadAllRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Recipe recipe);

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    Recipe loadRecipe(int recipeId);
}
