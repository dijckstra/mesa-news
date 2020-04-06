package inc.mesa.mesanews.news;

import android.util.Log;

import java.util.List;

import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.NewsDataSource;
import inc.mesa.mesanews.data.source.NewsRepository;

public class NewsPresenter implements NewsContract.Presenter {

    private static final String TAG = "NewsPresenter";
    private static final int ITEMS_PER_PAGE = 10;

    private NewsRepository newsRepository;
    private NewsContract.View view;

    private int currentPage = 1;

    public NewsPresenter(final NewsRepository newsRepository,
                         final NewsContract.View view) {
        this.newsRepository = newsRepository;
        this.view = view;
    }

    @Override
    public void start() {
        loadNews(true);
        loadNews(false);
    }

    /* Presenter contract methods */
    private void loadNews(final boolean highlights) {
        Log.d(TAG, "loadNews");
        if (highlights) {
            this.newsRepository.getHighlights(new NewsDataSource.NewsResult() {
                @Override
                public void onNewsLoaded(final List<News> news) {
                    processNews(true, news);
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        } else {
            this.newsRepository.getNews(currentPage, ITEMS_PER_PAGE, new NewsDataSource.NewsResult() {
                @Override
                public void onNewsLoaded(final List<News> news) {
                    processNews(false, news);
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }
    }

    /* Private methods */
    private void processNews(final boolean highlights, final List<News> news) {
        if (highlights) {
            view.showHighlights(news);
        } else {
            view.showLatestNews(news);
        }
    }
}
