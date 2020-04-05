package inc.mesa.mesanews.article;

import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.NewsDataSource;
import inc.mesa.mesanews.data.source.NewsRepository;

public class ArticlePresenter implements ArticleContract.Presenter {

    private NewsRepository repository;
    private ArticleContract.View view;
    private int newsId;

    public ArticlePresenter(final NewsRepository repository,
                            final ArticleContract.View view,
                            final int newsId) {
        this.repository = repository;
        this.view = view;
        this.newsId = newsId;
    }

    @Override
    public void start() {
        repository.getNewsArticle(newsId, new NewsDataSource.ArticleResult() {
            @Override
            public void onArticleLoaded(final News news) {
                view.displayArticle(news.getUrl());
                view.showMarkedAsFavorite(news.isFavorite());
            }
        });
    }

    @Override
    public void toggleFavorite(final boolean toggle) {
        repository.toggleFavorite(newsId);
        view.showMarkedAsFavorite(!toggle);
    }
}
