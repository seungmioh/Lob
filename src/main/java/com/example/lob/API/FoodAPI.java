package com.example.lob.API;

import com.example.lob.DTO.BoardDto;
import com.example.lob.DTO.FoodDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FoodAPI<CALL> {
    @POST("/food/")
    Call<Void> post_foods(@Body List<FoodDTO> foodDTOS);

}