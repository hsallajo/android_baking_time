package com.shu.bakingtime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelProperty;

@Parcel
public class Ingredient
{

    @SerializedName("quantity")
    @Expose
    private Float quantity;
    @SerializedName("measure")
    @Expose
    private String measure;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;

    @ParcelProperty("quantity")
    public Float getQuantity() {
        return quantity;
    }

    @ParcelProperty("measure")
    public String getMeasure() {
        return measure;
    }

    @ParcelProperty("ingredient")
    public String getIngredient() {
        return ingredient;
    }


}




