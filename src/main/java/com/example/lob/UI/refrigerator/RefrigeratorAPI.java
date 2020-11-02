package com.example.lob.UI.refrigerator;

import com.example.lob.DTO.FoodDTO;
import com.example.lob.DTO.FoodDTO2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RefrigeratorAPI {
    //음식 입력
    @POST("/food/")
    Call<FoodDTO2> post_food(@Body FoodDTO2 food);
    //음식 경로
    @PATCH("/food/{pk}/")
    Call<FoodDTO2> patch_food(@Path("pk") int pk, @Body FoodDTO2 food);
    //음식삭제
    @DELETE("/food/{pk}/")
    Call<FoodDTO2> delete_food(@Path("pk") int pk);
    //음식버림체크
    @PUT("/food/{pk}/")
    Call<FoodDTO2> update_foodDelInfo();
    //음식정보불러옴
    @GET("/food/")
    Call<List<FoodDTO2>> get_food();
    //음식 수정
    @PUT("/food/{pk}/")
    Call<FoodDTO2> update_food();
}
