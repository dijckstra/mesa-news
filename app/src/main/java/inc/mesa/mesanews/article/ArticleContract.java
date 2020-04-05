package inc.mesa.mesanews.article;

public interface ArticleContract {

    interface View {
        void displayArticle(final String url);

        void showMarkedAsFavorite(final boolean favorite);
    }

    interface Presenter {
        void start();

        void toggleFavorite(final boolean toggle);
    }
}
