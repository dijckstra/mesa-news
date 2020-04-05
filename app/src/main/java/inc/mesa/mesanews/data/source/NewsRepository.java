package inc.mesa.mesanews.data.source;

import java.util.List;

import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.remote.NewsRemoteDataSource;

public class NewsRepository implements NewsDataSource {

    private static NewsRepository INSTANCE = null;

    private final NewsDataSource newsRemoteDataSource;

    private NewsRepository(final NewsDataSource newsRemoteDataSource) {
        this.newsRemoteDataSource = newsRemoteDataSource;
    }

    public static NewsRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewsRepository(new NewsRemoteDataSource());
        }

        return INSTANCE;
    }

    @Override
    public void getNews(final boolean highlights, final NewsResult callback) {
        this.newsRemoteDataSource.getNews(highlights, new NewsResult() {
            @Override
            public void onNewsLoaded(final List<News> news) {
                callback.onNewsLoaded(news);
            }
        });
    }
}
