package inc.mesa.mesanews.article;

import inc.mesa.mesanews.data.source.NewsRepository;

public class ArticlePresenter implements ArticleContract.Presenter {

    private static final String TAG = "ArticlePresenter";

    private NewsRepository repository;
    private ArticleContract.View view;
    private String newsId;

    public ArticlePresenter(final NewsRepository repository,
                            final ArticleContract.View view,
                            final String newsId) {
        this.repository = repository;
        this.view = view;
        this.newsId = newsId;
    }

    @Override
    public void start() {
        repository.getNewsArticle(newsId, news -> {
            view.displayArticle(news.getUrl());
            view.showMarkedAsFavorite(news.isFavorite());
        });
    }

    @Override
    public void toggleFavorite(final boolean toggle) {
        repository.toggleFavorite(newsId);
        view.showMarkedAsFavorite(!toggle);
    }
}
