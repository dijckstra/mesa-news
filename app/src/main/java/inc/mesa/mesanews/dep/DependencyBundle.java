package inc.mesa.mesanews.dep;

import android.content.Context;

import androidx.room.Room;
import inc.mesa.mesanews.auth.AuthManager;
import inc.mesa.mesanews.client.RetrofitManager;
import inc.mesa.mesanews.data.source.NewsDataSource;
import inc.mesa.mesanews.data.source.NewsRepository;
import inc.mesa.mesanews.data.source.local.NewsDatabase;
import inc.mesa.mesanews.data.source.local.NewsLocalDataSource;
import inc.mesa.mesanews.data.source.remote.NewsRemoteDataSource;
import inc.mesa.mesanews.executors.AppExecutors;

final class DependencyBundle {

    private AuthManager authManager;
    private RetrofitManager retrofitManager;
    private NewsRepository newsRepository;

    DependencyBundle(final Context context) {
        authManager = new AuthManager(context);
        retrofitManager = new RetrofitManager();

        AppExecutors executors = new AppExecutors();
        NewsDatabase newsDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                                         NewsDatabase.class,
                                                         "news.db").build();

        NewsDataSource localDataSource = new NewsLocalDataSource(executors, newsDatabase.newsDao());
        NewsDataSource remoteDataSource = new NewsRemoteDataSource(authManager, retrofitManager);
        newsRepository = new NewsRepository(localDataSource, remoteDataSource);
    }


    AuthManager getAuthManager() {
        return authManager;
    }

    RetrofitManager getRetrofitManager() {
        return retrofitManager;
    }

    NewsRepository getNewsRepository() {
        return newsRepository;
    }
}
