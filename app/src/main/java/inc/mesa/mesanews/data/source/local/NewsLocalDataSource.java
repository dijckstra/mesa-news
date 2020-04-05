package inc.mesa.mesanews.data.source.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.NewsDataSource;

public class NewsLocalDataSource implements NewsDataSource {

    private static List<News> mockNews = Collections.singletonList(
            new News(1,
                     "Amazon, Walmart and others battle price gouging on coronavirus-related products - CNBC",
                     "Amazon, eBay, Walmart and Etsy have all struggled to curb price gouging and products that make unverified claims about the coronavirus.",
                     "https://www.cnbc.com/2020/03/03/amazon-walmart-e-retailers-battle-price-gouging-on-coronavirus-products.html",
                     "https://image.cnbcfm.com/api/v1/image/106422372-1583253995153rtx7ashh.jpg?v=1583254080",
                     false)
    );

    @Override
    public void getNews(final boolean highlights, final NewsResult callback) {
        callback.onNewsLoaded(new ArrayList<>());
    }

    @Override
    public void getNewsArticle(final int newsId, final ArticleResult callback) {
        callback.onArticleLoaded(mockNews.get(newsId));
    }

    @Override
    public void toggleFavorite(final int newsId) {
        News news = mockNews.get(newsId);
        boolean b = news.isFavorite();
        news.setFavorite(!b);
    }
}
