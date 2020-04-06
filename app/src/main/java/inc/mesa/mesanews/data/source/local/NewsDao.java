package inc.mesa.mesanews.data.source.local;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import inc.mesa.mesanews.data.News;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news WHERE highlight = 0 LIMIT :limit OFFSET :offset")
    List<News> getNews(int limit, int offset);

    @Query("SELECT * FROM news WHERE highlight = 1")
    List<News> getHighlights();

    @Query("SELECT * FROM news WHERE id = :newsId")
    News getNewsById(String newsId);

    @Insert
    void insertNewsArticle(News news);

    @Query("UPDATE news SET favorite = NOT favorite WHERE id = :newsId")
    void toggleFavorite(String newsId);
}
