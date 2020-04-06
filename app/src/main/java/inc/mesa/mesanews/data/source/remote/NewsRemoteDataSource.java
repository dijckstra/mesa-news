package inc.mesa.mesanews.data.source.remote;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import inc.mesa.mesanews.auth.AuthManager;
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

    private AuthManager authManager;
    private NewsClient newsClient;

    public NewsRemoteDataSource(final AuthManager authManager,
                                final RetrofitManager retrofitManager) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(new TypeToken<List<News>>() {}.getType(),
                                    new GsonDeserializer());

        Retrofit retrofit = retrofitManager.getClientInstance(GsonConverterFactory.create(builder.create()));

        this.authManager = authManager;
        newsClient = retrofit.create(NewsClient.class);
    }

    @Override
    public void getHighlights(final NewsResult callback) {
        String token = authManager.getAuthToken();
        Call<List<News>> call = newsClient.getHighlights(token);

        requestNews(call, callback);
    }

    @Override
    public void getNews(final int currentPage, final int perPage, final NewsResult callback) {
        String token = authManager.getAuthToken();
        Call<List<News>> call = newsClient.getNews(token, currentPage, perPage);

        requestNews(call, callback);
    }

    private void requestNews(final Call<List<News>> call, final NewsResult callback) {
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
    public void getNewsArticle(final String newsId, final ArticleResult callback) {
        // no-op
    }

    @Override
    public void addNewsArticle(final News news) {
        // no-op
    }

    @Override
    public void toggleFavorite(final String newsId) {
        // no-op
    }
}
