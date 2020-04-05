package inc.mesa.mesanews.news;

import java.util.List;

import androidx.annotation.NonNull;
import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.NewsRepository;

public class NewsPresenter implements NewsContract.Presenter {

    private static final String TAG = "NewsPresenter";

    private NewsRepository newsRepository;
    private NewsContract.View view;

    public NewsPresenter(final NewsRepository newsRepository, @NonNull final NewsContract.View view) {
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
        this.newsRepository.getNews(highlights, news -> processNews(highlights, news));
    }

    @Override
    public void openNewsURL(final String url) {
        view.showArticle(url);
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
