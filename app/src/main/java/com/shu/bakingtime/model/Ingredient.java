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

    @ParcelProperty("quantity")
    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    @ParcelProperty("measure")
    public String getMeasure() {
        return measure;
    }

    @ParcelProperty("measure")
    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @ParcelProperty("ingredient")
    public String getIngredient() {
        return ingredient;
    }

    @ParcelProperty("ingredient")
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

}




