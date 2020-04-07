package inc.mesa.mesanews.filter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import androidx.annotation.StringDef;
import inc.mesa.mesanews.data.News;

public interface FilterContract {

    @StringDef({
            NONE,
            FAVORITES,
            DATE
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface NewsFilterType {}

    String NONE = "none";
    String FAVORITES = "favorites";
    String DATE = "date";

    interface View {
        void showNews(final List<News> news);

        void showArticle(final String newsId);
    }

    interface Presenter {
        void start();

        void loadNews();

        void setFiltering(@NewsFilterType String filterType);

        void toggleFavorite(final String newsId);
    }
}
