package inc.mesa.mesanews.ui;

import inc.mesa.mesanews.data.News;

public interface NewsItemListener {

    void onNewsClick(final News news);

    void onNewsFavoriteClick(final News news);
}
