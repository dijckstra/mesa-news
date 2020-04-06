package inc.mesa.mesanews.news;

import java.util.List;

import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.NewsDataSource;
import inc.mesa.mesanews.data.source.NewsRepository;

public class NewsPresenter implements NewsContract.Presenter {

    private static final String TAG = "NewsPresenter";
    private static final int ITEMS_PER_PAGE = 10;

    private NewsRepository newsRepository;
    private NewsContract.View view;

    public NewsPresenter(final NewsRepository newsRepository,
                         final NewsContract.View view) {
        this.newsRepository = newsRepository;
        this.view = view;
    }

    @Override
    public void start() {
        loadNews(1);
        loadHighlights();
    }

    /* Presenter contract methods */
    @Override
    public void loadNews(final int page) {
        this.newsRepository.getNews(page, ITEMS_PER_PAGE, new NewsDataSource.NewsResult() {
            @Override
            public void onNewsLoaded(final List<News> news) {
                processNews(news);
            }

            @Override
            public void onDataNotAvailable() {
                view.showLoadingNewsError();
            }
        });
    }

    @Override
    public void loadHighlights() {
        this.newsRepository.getHighlights(new NewsDataSource.NewsResult() {
            @Override
            public void onNewsLoaded(final List<News> news) {
                processHighlights(news);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void loadNewsPage(final int listSize) {
        int nextPage = (listSize / ITEMS_PER_PAGE) + 1;

        newsRepository.refreshNews();
        loadNews(nextPage);
    }

    /* Private methods */
    private void processNews(final List<News> newsList) {
        view.showLatestNews(newsList);
    }

    private void processHighlights(final List<News> highlights) {
        view.showHighlights(highlights);
    }
}
