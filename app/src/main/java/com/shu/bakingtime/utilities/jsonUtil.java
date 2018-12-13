package com.shu.bakingtime.utilities;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shu.bakingtime.model.Ingredient;

import java.lang.reflect.Type;
import java.util.List;

public class jsonUtil {

    public static final String TAG = jsonUtil.class.getSimpleName();

    public static List<Ingredient> jsonToList(String json){
        List<Ingredient> ingredients;
        Gson gson = new Gson();

        if(json == null || json.equals("")){
            return null;
        } else {
            Type t = new TypeToken<List<Ingredient>>(){}.getType();
            ingredients = gson.fromJson(json, t);
            return ingredients;
        }
    }

    public static String listToJson(List<Ingredient> list){
        String s;
        Gson gson = new Gson();

        if(list == null || list.isEmpty()){
            return null;
        } else {
            Type t = new TypeToken<List<Ingredient>>(){}.getType();
            s = gson.toJson(list, t);
            //Log.d(TAG, "listToJson: " + s);
            return s;
        }
    }

    public static String trim(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }
}
