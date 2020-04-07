package inc.mesa.mesanews.news;

import java.util.ArrayList;
import java.util.List;

import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.NewsDataSource;
import inc.mesa.mesanews.data.source.NewsRepository;
import inc.mesa.mesanews.news.NewsContract.NewsFilterType;

import static inc.mesa.mesanews.news.NewsContract.ALL;
import static inc.mesa.mesanews.news.NewsContract.FAVORITES;

public class NewsPresenter implements NewsContract.Presenter {

    private static final String TAG = "NewsPresenter";
    private static final int ITEMS_PER_PAGE = 10;

    private NewsRepository newsRepository;
    private NewsContract.View view;

    private String currentFiltering = ALL;

    public NewsPresenter(final NewsRepository newsRepository,
                         final NewsContract.View view) {
        this.newsRepository = newsRepository;
        this.view = view;
    }

    @Override
    public void start() {
        loadNews();
        loadHighlights();
    }

    /* Presenter contract methods */
    @Override
    public void loadNews() {
        this.newsRepository.getNews(1, ITEMS_PER_PAGE, new NewsDataSource.NewsResult() {
            @Override
            public void onNewsLoaded(final List<News> news) {
                List<News> newsToShow = processNews(news);
                view.showLatestNews(newsToShow, true);
            }

            @Override
            public void onDataNotAvailable() {
                view.showLoadingNewsError();
            }
        });
    }

    @Override
    public void loadNews(final int page) {
        this.newsRepository.getNews(page, ITEMS_PER_PAGE, new NewsDataSource.NewsResult() {
            @Override
            public void onNewsLoaded(final List<News> news) {
                List<News> newsToShow = processNews(news);
                view.showLatestNews(newsToShow, false);
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
            public void onNewsLoaded(final List<News> highlights) {
                view.showHighlights(highlights);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void loadAllNews() {
        if (currentFiltering.equals(FAVORITES)) {
            this.newsRepository.getAllNews(new NewsDataSource.NewsResult() {
                @Override
                public void onNewsLoaded(final List<News> news) {
                    List<News> newsToShow = processNews(news);
                    view.showLatestNews(newsToShow, true);
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        } else {
            loadNews();
        }
    }

    @Override
    public void loadNewsPage(final int listSize) {
        int nextPage = (listSize / ITEMS_PER_PAGE) + 1;

        newsRepository.refreshNews();
        loadNews(nextPage);
    }

    @Override
    public void setFiltering(@NewsFilterType final String filterType) {
        this.currentFiltering = filterType;
    }

    /* Private methods */
    private List<News> processNews(final List<News> newsList) {
        List<News> newsToShow = new ArrayList<>();

        for (News news : newsList) {
            switch (currentFiltering) {
                case FAVORITES:
                    if (news.isFavorite()) {
                        newsToShow.add(news);
                    }

                    break;
                case ALL:
                default:
                    newsToShow.add(news);
                    break;
            }
        }

        return newsToShow;
    }
}
