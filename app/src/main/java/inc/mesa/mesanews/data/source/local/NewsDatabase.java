package inc.mesa.mesanews.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import inc.mesa.mesanews.data.News;

@Database(entities = {News.class}, exportSchema = false, version = 1)
public abstract class NewsDatabase extends RoomDatabase {

    public abstract NewsDao newsDao();
}
