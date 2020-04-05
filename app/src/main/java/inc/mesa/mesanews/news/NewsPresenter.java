package inc.mesa.mesanews.news;

import java.util.List;

import androidx.annotation.NonNull;
import inc.mesa.mesanews.auth.AuthManager;
import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.NewsRepository;

public class NewsPresenter implements NewsContract.Presenter {

    private static final String TAG = "NewsPresenter";

    private NewsRepository newsRepository;
    private AuthManager authManager;
    private NewsContract.View view;

    public NewsPresenter(final NewsRepository newsRepository,
                         final AuthManager authManager,
                         @NonNull final NewsContract.View view) {
        this.newsRepository = newsRepository;
        this.authManager = authManager;
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

    /* Private methods */
    private void processNews(final boolean highlights, final List<News> news) {
        if (highlights) {
            view.showHighlights(news);
        } else {
            view.showLatestNews(news);
        }
    }
}
