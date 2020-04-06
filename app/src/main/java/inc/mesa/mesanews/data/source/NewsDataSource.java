package inc.mesa.mesanews.data.source;

import java.util.List;

import inc.mesa.mesanews.data.News;

public interface NewsDataSource {

    interface NewsResult {
        void onNewsLoaded(final List<News> news);
        void onDataNotAvailable();
    }
    interface ArticleResult {
        void onArticleLoaded(News news);
    }

    void refreshNews();

    void getNews(int currentPage, int perPage, final NewsResult callback);

    void getHighlights(final NewsResult callback);

    void getNewsArticle(final String newsId, final ArticleResult callback);

    void addNewsArticle(final News news);

    void toggleFavorite(final String newsId);
}
