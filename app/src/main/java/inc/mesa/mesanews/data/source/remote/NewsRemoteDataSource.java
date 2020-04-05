package inc.mesa.mesanews.data.source.remote;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import inc.mesa.mesanews.client.RetrofitManager;
import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.NewsDataSource;
import inc.mesa.mesanews.client.GsonDeserializer;
import inc.mesa.mesanews.client.NewsClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRemoteDataSource implements NewsDataSource {

    private static final String TAG = "NewsRemoteDataSource";
    private static final String AUTHORIZATION = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6NSwiZW1haWwiOiJqb2huQHNtaXRoLmNvbSJ9.Erfd4aKG3XO610Y8ch6sq4Qccpx9FCCKThJ5i1lnI_Q";

    private final NewsClient newsClient;

    public NewsRemoteDataSource() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(new TypeToken<List<News>>() {}.getType(),
                                    new GsonDeserializer());

        Retrofit retrofit = RetrofitManager.getInstance()
                .getClientInstance(GsonConverterFactory.create(builder.create()));

        newsClient = retrofit.create(NewsClient.class);
    }

    @Override
    public void getNews(final boolean highlights, final NewsResult callback) {
        Call<List<News>> call = highlights
                                ? newsClient.getHighlights(AUTHORIZATION)
                                : newsClient.getNews(AUTHORIZATION, 1, 10);

        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(final Call<List<News>> call, final Response<List<News>> response) {
                callback.onNewsLoaded(response.body());
            }

            @Override
            public void onFailure(final Call<List<News>> call, final Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getNewsArticle(final int newsId, final ArticleResult callback) {
        // no-op
    }

    @Override
    public void toggleFavorite(final int newsId) {
        // no-op
    }
}
