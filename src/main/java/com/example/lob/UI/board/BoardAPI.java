package com.example.lob.UI.board;
import com.example.lob.DTO.BoardDto;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BoardAPI<CALL> {
    @POST("/board/")
    Call<BoardDto> post_boards(@Body BoardDto board);

    @PATCH("/board/{pk}/")
    Call<BoardDto> patch_posts(@Path("pk") int pk, @Body BoardDto board);

    @DELETE("/board/{pk}/")
    Call<BoardDto> delete_posts(@Path("pk") int pk);

    @GET("/board/")
    Call<List<BoardDto>> get_posts();

    @GET("/board/{pk}/")
    Call<BoardDto> get_post_pk(@Path("pk") int pk);
}
