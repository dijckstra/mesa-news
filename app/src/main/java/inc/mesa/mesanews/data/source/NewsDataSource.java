package inc.mesa.mesanews.data.source;

import java.util.List;

import inc.mesa.mesanews.data.News;

public interface NewsDataSource {

    interface NewsResult {
        void onNewsLoaded(final List<News> news);
    }

    void getNews(final boolean highlights, final NewsResult callback);
}
