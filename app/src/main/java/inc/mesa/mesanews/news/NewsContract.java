package inc.mesa.mesanews.news;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import androidx.annotation.StringDef;
import inc.mesa.mesanews.data.News;

public interface NewsContract {

    @StringDef({ALL,
                FAVORITES})
    @Retention(RetentionPolicy.SOURCE)
    @interface NewsFilterType {}

    String ALL = "all";
    String FAVORITES = "favorites";

    interface View {
        void showHighlights(final List<News> highlights);

        void showLatestNews(final List<News> latestNews, boolean shouldReplace);

        void showArticle(final android.view.View view, final String newsId);

        void showFilteringMenu();

        void showLoadingNewsError();
    }

    interface Presenter {
        void start();

        void loadNews();

        void loadHighlights();

        void loadAllNews();

        void loadNews(final int page);

        void loadNewsPage(final int listSize);

        void setFiltering(@NewsFilterType String filterType);
    }
}
