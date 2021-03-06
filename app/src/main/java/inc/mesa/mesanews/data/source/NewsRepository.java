package inc.mesa.mesanews.data.source;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import inc.mesa.mesanews.data.News;

public class NewsRepository implements NewsDataSource {

    private static final String TAG = "NewsRepository";

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

    @Override
    public void getAllNews(final NewsResult callback) {
        if (cache != null && !cache.isEmpty()) {
            callback.onNewsLoaded(new ArrayList<>(cache.values()));
            return;
        }

        newsLocalDataSource.getAllNews(new NewsResult() {
            @Override
            public void onNewsLoaded(final List<News> news) {
                refreshCache(news);
                callback.onNewsLoaded(news);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void getNewsArticle(final String newsId, final ArticleResult callback) {
        News cachedNews = getNewsWithId(newsId);

        if (cachedNews != null) {
            callback.onArticleLoaded(cachedNews);
            return;
        }

        this.newsLocalDataSource.getNewsArticle(newsId, news -> {
            if (cache == null) {
                cache = new LinkedHashMap<>();
            }

            cache.put(newsId, news);
        });
    }

    @Override
    public void toggleFavorite(final String newsId) {
        this.newsLocalDataSource.toggleFavorite(newsId);

        if (cache == null) {
            cache = new LinkedHashMap<>();
        }

        News cachedNews = getNewsWithId(newsId);
        News favoritedNews = new News.Builder(cachedNews)
                .favorite(!cachedNews.isFavorite()).build();

        cache.put(newsId, favoritedNews);
    }

    private News getNewsWithId(final String newsId) {
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            return cache.get(newsId);
        }
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
    public void addNewsArticle(final News news) {
        // no-op
    }
}
