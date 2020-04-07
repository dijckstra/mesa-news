package inc.mesa.mesanews.ui;

public interface NewsItemListener {

    void onNewsClick(final String newsId);

    void onNewsFavoriteClick(final String newsId);
}
