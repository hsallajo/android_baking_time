package com.shu.bakingtime.database;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shu.bakingtime.model.Ingredient;
import com.shu.bakingtime.model.Step;
import java.lang.reflect.Type;
import java.util.List;

public class RecipeTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static String fromIngredientList(List<Ingredient> ingredients){
        if(ingredients == null)
            return null;
        Type type = new TypeToken<List<Ingredient>>(){}.getType();
        String json = gson.toJson(ingredients, type);
        return json;
    }

    @TypeConverter
    public static List<Ingredient> fromIngredientString(String ingredients){
        if(ingredients == null)
            return null;
        Type type = new TypeToken<List<Ingredient>>(){}.getType();
        List<Ingredient> ingredientList = gson.fromJson(ingredients, type);
        return ingredientList;
    }

    @TypeConverter
    public static String fromStepList(List<Step> steps){
        if(steps == null)
            return null;
        Type type = new TypeToken<List<Step>>(){}.getType();
        String json = gson.toJson(steps, type);
        return json;
    }

    @TypeConverter
    public static List<Step> fromStepString(String steps){
        if(steps == null)
            return null;
        Type type = new TypeToken<List<Step>>(){}.getType();
        List<Step> stepsList = gson.fromJson(steps, type);
        return stepsList;
    }
}
