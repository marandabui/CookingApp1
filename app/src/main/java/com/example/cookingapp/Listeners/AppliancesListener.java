package com.example.cookingapp.Listeners;

import com.example.cookingapp.Models.RecipeAppliancesResponse;

public interface AppliancesListener {
    void didFetch(RecipeAppliancesResponse response, String message);
    void didError(String message);
}
