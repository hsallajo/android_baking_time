package com.shu.bakingtime.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelProperty;

@Parcel
public class Recipe {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    @ParcelProperty("id")
    public Integer getId() {
        return id;
    }

    @ParcelProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @ParcelProperty("name")
    public String getName() {
        return name;
    }

    @ParcelProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @ParcelProperty("ingredients")
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @ParcelProperty("ingredients")
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @ParcelProperty("steps")
    public List<Step> getSteps() {
        return steps;
    }

    @ParcelProperty("steps")
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @ParcelProperty("servings")
    public Integer getServings() {
        return servings;
    }

    @ParcelProperty("servings")
    public void setServings(Integer servings) {
        this.servings = servings;
    }

    @ParcelProperty("image")
    public String getImage() {
        return image;
    }

    @ParcelProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

}

