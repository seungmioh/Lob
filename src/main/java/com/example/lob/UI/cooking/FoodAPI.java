package com.example.lob.UI.cooking;

import com.example.lob.DTO.FoodDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FoodAPI {


    @POST("/food/")
    Call<FoodDTO> post_food(@Body FoodDTO post);

    @PATCH("/food/{pk}/")
    Call<FoodDTO> patch_food(@Path("pk") int pk, @Body FoodDTO post);

    @DELETE("/food/{pk}/")
    Call<FoodDTO> delete_food(@Path("pk") int pk);

    @GET("/food/")
    Call<List<FoodDTO>> get_food();

    @GET("/food/{pk}/")
    Call<FoodDTO> get_food_pk(@Path("pk") int pk);
}
