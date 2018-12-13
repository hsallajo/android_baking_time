package com.shu.bakingtime.database;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shu.bakingtime.model.Ingredient;
import com.shu.bakingtime.model.Step;
import java.lang.reflect.Type;
import java.util.List;

public class RecipeTypeConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromIngredientList(List<Ingredient> ingredients){
        if(ingredients == null)
            return null;
        Type type = new TypeToken<List<Ingredient>>(){}.getType();
        return gson.toJson(ingredients, type);
    }

    @TypeConverter
    public static List<Ingredient> fromIngredientString(String ingredients){
        if(ingredients == null)
            return null;
        Type type = new TypeToken<List<Ingredient>>(){}.getType();
        return gson.fromJson(ingredients, type);
    }

    @TypeConverter
    public static String fromStepList(List<Step> steps){
        if(steps == null)
            return null;
        Type type = new TypeToken<List<Step>>(){}.getType();
        return gson.toJson(steps, type);
    }

    @TypeConverter
    public static List<Step> fromStepString(String steps){
        if(steps == null)
            return null;
        Type type = new TypeToken<List<Step>>(){}.getType();
        return gson.fromJson(steps, type);
    }
}
