package com.kholoud.bakingapp.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient implements Serializable {

    @SerializedName("quantity")
    @Expose
    private Double quantity;
    @SerializedName("measure")
    @Expose
    private String measure;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;
    private final static long serialVersionUID = 3472564338439438511L;

    // Test
    private Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    private String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    private String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }


    @Override
    public String toString() {
        return " \t◘ " + getQuantity() +" " + getMeasure() + " | " + getIngredient();
    }

    public static String getIngredientsString(List<Ingredient> ingredients) {

        StringBuilder ingredientsStr = new StringBuilder();
        for (Ingredient ing: ingredients) {
            ingredientsStr.append(ing.toString()).append("\n");
        }
        return ingredientsStr.toString();
    }
}
