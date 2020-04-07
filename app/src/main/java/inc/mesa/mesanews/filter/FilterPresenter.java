package inc.mesa.mesanews.filter;

import java.util.ArrayList;
import java.util.List;

import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.NewsDataSource;
import inc.mesa.mesanews.data.source.NewsRepository;

import static inc.mesa.mesanews.filter.FilterContract.FAVORITES;
import static inc.mesa.mesanews.filter.FilterContract.NONE;

public class FilterPresenter implements FilterContract.Presenter {

    private NewsRepository newsRepository;
    private FilterContract.View view;

    private String currentFiltering = NONE;

    public FilterPresenter(final NewsRepository newsRepository,
                           final FilterContract.View view) {
        this.newsRepository = newsRepository;
        this.view = view;
    }

    public void start() {
        loadNews();
    }

    @Override
    public void loadNews() {
        this.newsRepository.getAllNews(new NewsDataSource.NewsResult() {
            @Override
            public void onNewsLoaded(final List<News> news) {
                List<News> newsToShow = processNews(news);
                view.showNews(newsToShow);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void setFiltering(final String filterType) {
        this.currentFiltering = filterType;
    }

    @Override
    public void toggleFavorite(final String newsId) {
        newsRepository.toggleFavorite(newsId);
        loadNews();
    }

    private List<News> processNews(final List<News> newsList) {
        List<News> newsToShow = new ArrayList<>();

        loop: for (News news : newsList) {
            switch (currentFiltering) {
                case FAVORITES:
                    if (news.isFavorite()) {
                        newsToShow.add(news);
                    }

                    break;
                case NONE:
                default:
                    break loop;
            }
        }

        return newsToShow;
    }
}
