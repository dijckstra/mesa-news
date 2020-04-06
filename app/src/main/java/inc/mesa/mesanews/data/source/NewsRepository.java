package inc.mesa.mesanews.data.source;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import inc.mesa.mesanews.data.News;

public class NewsRepository implements NewsDataSource {

    private final NewsDataSource newsLocalDataSource;
    private final NewsDataSource newsRemoteDataSource;

    private Map<String, News> cache;
    private boolean isCacheDirty = false;

    public NewsRepository(final NewsDataSource newsLocalDataSource,
                           final NewsDataSource newsRemoteDataSource) {
        this.newsLocalDataSource = newsLocalDataSource;
        this.newsRemoteDataSource = newsRemoteDataSource;
    }

    @Override
    public void refreshNews() {
        isCacheDirty = true;
    }

    @Override
    public void getNews(final int currentPage, final int perPage, final NewsResult callback) {
        if (cache != null && !isCacheDirty) {
            Map<String, News> cachedNews = filterHighlights(cache, false);
            callback.onNewsLoaded(new ArrayList<>(cachedNews.values()));
            return;
        }

        newsLocalDataSource.getNews(currentPage, perPage, new NewsResult() {
            @Override
            public void onNewsLoaded(final List<News> news) {
                refreshCache(news);
                callback.onNewsLoaded(news);
            }

            @Override
            public void onDataNotAvailable() {
                getNewsFromRemoteDataSource(currentPage, perPage, callback);
            }
        });
    }

    private Map<String, News> filterHighlights(final Map<String, News> news, final boolean highlight) {
        Map<String, News> filtered = new LinkedHashMap<>();
        for (Map.Entry<String, News> entry : news.entrySet()) {
            if (entry.getValue().isHighlight() == highlight) {
                filtered.put(entry.getValue().getId(), entry.getValue());
            }
        }
        
        return filtered;
    }

    @Override
    public void getHighlights(final NewsResult callback) {
        if (cache != null) {
            Map<String, News> cachedHighlights = filterHighlights(cache, true);
            callback.onNewsLoaded(new ArrayList<>(cachedHighlights.values()));
            return;
        }

        newsLocalDataSource.getHighlights(new NewsResult() {
            @Override
            public void onNewsLoaded(final List<News> news) {
                refreshCache(news);
                callback.onNewsLoaded(news);
            }

            @Override
            public void onDataNotAvailable() {
                getHighlightsFromRemoteDataSource(callback);
            }
        });
    }

    private void getHighlightsFromRemoteDataSource(final NewsResult callback) {
        this.newsRemoteDataSource.getHighlights(new NewsResult() {
            @Override
            public void onNewsLoaded(final List<News> news) {
                refreshCache(news);
                refreshLocalDataSource(news);
                callback.onNewsLoaded(news);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getNewsFromRemoteDataSource(final int currentPage, final int perPage, final NewsResult callback) {
        this.newsRemoteDataSource.getNews(currentPage, perPage, new NewsResult() {
            @Override
            public void onNewsLoaded(final List<News> news) {
                refreshCache(news);
                refreshLocalDataSource(news);
                callback.onNewsLoaded(news);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(final List<News> newsList) {
        if (cache == null) {
            cache = new LinkedHashMap<>();
        }

        for (News news : newsList) {
            cache.put(news.getId(), news);
        }

        isCacheDirty = false;
    }

    private void refreshLocalDataSource(final List<News> newsList) {
        for (News news : newsList) {
            newsLocalDataSource.addNewsArticle(news);
        }
    }

    @Override
    public void getNewsArticle(final String newsId, final ArticleResult callback) {
        this.newsLocalDataSource.getNewsArticle(newsId, callback::onArticleLoaded);
    }

    @Override
    public void addNewsArticle(final News news) {
        // no-op
    }

    @Override
    public void toggleFavorite(final String newsId) {
        this.newsLocalDataSource.toggleFavorite(newsId);
    }
}
