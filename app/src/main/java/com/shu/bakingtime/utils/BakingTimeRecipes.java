package com.shu.bakingtime.utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingTimeRecipes {

    @GET("baking.json")
    Call<List<com.shu.bakingtime.model.Recipe>> getRecipes();
}
