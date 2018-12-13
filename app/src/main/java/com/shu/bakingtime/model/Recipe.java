package com.shu.bakingtime.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shu.bakingtime.database.RecipeTypeConverter;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

@Parcel
@Entity(tableName = "recipe")
public class Recipe {

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    @TypeConverters(RecipeTypeConverter.class)
    private List<Ingredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    @TypeConverters(RecipeTypeConverter.class)
    private List<Step> steps;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    @Ignore
    public Recipe(String name
            , List<Ingredient> ingredients
            , List<Step> steps
            , int servings
            , String image){
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    @ParcelConstructor
    public Recipe(Integer id
            , String name
            , List<Ingredient> ingredients
            , List<Step> steps
            , Integer servings
            , String image){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    @ParcelProperty("id")
    public Integer getId() {
        return id;
    }

    @ParcelProperty("name")
    public String getName() {
        return name;
    }


    @ParcelProperty("ingredients")
    public List<Ingredient> getIngredients() {
        return ingredients;
    }


    @ParcelProperty("steps")
    public List<Step> getSteps() {
        return steps;
    }

    @ParcelProperty("servings")
    public Integer getServings() {
        return servings;
    }


    @ParcelProperty("image")
    public String getImage() {
        return image;
    }


}

