package inc.mesa.mesanews.data.source;

import java.util.List;

import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.local.NewsLocalDataSource;
import inc.mesa.mesanews.data.source.remote.NewsRemoteDataSource;

public class NewsRepository implements NewsDataSource {

    private static NewsRepository INSTANCE = null;

    private final NewsDataSource newsRemoteDataSource;
    private final NewsDataSource newsLocalDataSource;

    private NewsRepository(final NewsDataSource newsRemoteDataSource,
                           final NewsDataSource newsLocalDataSource) {
        this.newsRemoteDataSource = newsRemoteDataSource;
        this.newsLocalDataSource = newsLocalDataSource;
    }

    public static NewsRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewsRepository(new NewsRemoteDataSource(), new NewsLocalDataSource());
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

    @Override
    public void getNewsArticle(final int newsId, final ArticleResult callback) {
        this.newsLocalDataSource.getNewsArticle(newsId, callback::onArticleLoaded);
    }

    @Override
    public void toggleFavorite(final int newsId) {
        this.newsLocalDataSource.toggleFavorite(newsId);
    }
}
