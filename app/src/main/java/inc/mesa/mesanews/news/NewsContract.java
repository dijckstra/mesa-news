package inc.mesa.mesanews.news;

import java.util.List;

import inc.mesa.mesanews.data.News;

public interface NewsContract {

    interface View {
        void showHighlights(final List<News> highlights);

        void showLatestNews(final List<News> latestNews);

        void showArticle(final android.view.View view, final String newsId);

        void showLoadingNewsError();
    }

    interface Presenter {
        void start();

        void loadNewsPage(final int listSize);
    }
}
