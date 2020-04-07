package inc.mesa.mesanews.news;

import java.util.List;

import inc.mesa.mesanews.data.News;

public interface NewsContract {

    interface View {
        void showHighlights(final List<News> highlights);

        void showLatestNews(final List<News> latestNews, boolean shouldReplace);

        void showArticle(final String newsId);

        void showLoadingNewsError();
    }

    interface Presenter {
        void start();

        void refreshNews();

        void loadHighlights();

        void loadNewsPage(final int listSize);

        void toggleFavorite(final String newsId);
    }
}
