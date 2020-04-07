package inc.mesa.mesanews.data.source.local;

import java.util.List;

import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.NewsDataSource;
import inc.mesa.mesanews.executors.AppExecutors;

public class NewsLocalDataSource implements NewsDataSource {

    private AppExecutors executors;
    private NewsDao newsDao;

    public NewsLocalDataSource(final AppExecutors executors, final NewsDao newsDao) {
        this.executors = executors;
        this.newsDao = newsDao;
    }

    @Override
    public void getNews(final int currentPage, final int perPage, final NewsResult callback) {
        executors.diskIO().execute(() -> {
            int offset = (currentPage - 1) * perPage;

            List<News> news = newsDao.getNews(perPage, offset);
            handleResult(news, callback);
        });
    }

    @Override
    public void getHighlights(final NewsResult callback) {
        executors.diskIO().execute(() -> {
            List<News> news = newsDao.getHighlights();
            handleResult(news, callback);
        });
    }

    @Override
    public void getAllNews(final NewsResult callback) {
        executors.diskIO().execute(() -> {
            List<News> news = newsDao.getAllNews();
            handleResult(news, callback);
        });
    }

    private void handleResult(final List<News> news, final NewsResult callback) {
        executors.mainThread().execute(() -> {
            if (news.isEmpty()) {
                callback.onDataNotAvailable();
            } else {
                callback.onNewsLoaded(news);
            }
        });
    }

    @Override
    public void getNewsArticle(final String newsId, final ArticleResult callback) {
        executors.diskIO().execute(() -> {
            News news = newsDao.getNewsById(newsId);
            executors.mainThread().execute(() -> {
                if (news != null) {
                    callback.onArticleLoaded(news);
                }
            });
        });
    }

    @Override
    public void addNewsArticle(final News news) {
        executors.diskIO().execute(() -> newsDao.insertNewsArticle(news));
    }

    @Override
    public void toggleFavorite(final String newsId) {
        executors.diskIO().execute(() -> newsDao.toggleFavorite(newsId));
    }

    @Override
    public void refreshNews() {
        // no-op
    }
}
