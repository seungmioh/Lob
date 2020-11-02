package com.example.lob.UI.board;

import com.example.lob.DTO.CommentDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentAPI {
    @POST("/comment/")
    Call<CommentDTO> post_comment(@Body CommentDTO comment);

    @PATCH("/comment/{pk}/")
    Call<CommentDTO> patch_comment(@Path("pk") int pk, @Body CommentDTO comment);

    @DELETE("/comment/{pk}/")
    Call<CommentDTO> delete_comment(@Path("pk") int pk);

    @GET("/comment/")
    Call<List<CommentDTO>> get_comment();

    @GET("/comment/{pk}/")
    Call<CommentDTO> get_comment_pk(@Path("pk") int pk);
}
