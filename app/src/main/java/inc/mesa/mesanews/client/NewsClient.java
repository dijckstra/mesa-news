package inc.mesa.mesanews.client;

import java.util.List;

import inc.mesa.mesanews.client.model.request.LogInRequest;
import inc.mesa.mesanews.client.model.request.SignUpRequest;
import inc.mesa.mesanews.client.model.response.AuthResponse;
import inc.mesa.mesanews.data.News;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NewsClient {

    @POST("auth/signin")
    Call<AuthResponse> signIn(@Body LogInRequest request);

    @POST("auth/signup")
    Call<AuthResponse> signUp(@Body SignUpRequest request);

    @GET("news")
    Call<List<News>> getNews(@Header("Authorization") String authHeader,
                             @Query("current_page") int currentPage,
                             @Query("per_page") int newsPerPage);

    @GET("news/highlights")
    Call<List<News>> getHighlights(@Header("Authorization") String authHeader);
}
