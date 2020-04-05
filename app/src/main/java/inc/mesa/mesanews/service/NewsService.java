package inc.mesa.mesanews.service;

import java.util.List;

import inc.mesa.mesanews.data.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NewsService {

    @GET("news")
    Call<List<News>> getNews(@Header("Authorization") String authHeader,
                             @Query("current_page") int currentPage,
                             @Query("per_page") int newsPerPage);

    @GET("news/highlights")
    Call<List<News>> getHighlights(@Header("Authorization") String authHeader);
}
