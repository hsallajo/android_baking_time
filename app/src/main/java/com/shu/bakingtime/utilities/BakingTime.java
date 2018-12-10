package com.shu.bakingtime.utilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingTime {

    @GET("baking.json")
    Call<List<com.shu.bakingtime.model.Recipe>> getRecipes();
}
