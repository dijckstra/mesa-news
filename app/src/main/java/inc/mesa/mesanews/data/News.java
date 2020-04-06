package inc.mesa.mesanews.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class News {

    @PrimaryKey
    private String id;
    private String title;
    private String description;
    private String url;
    @ColumnInfo(name = "image_url")
    private String imageUrl;
    private boolean highlight;
    private boolean favorite;

    public News(final String id,
                final String title,
                final String description,
                final String url,
                final String imageUrl,
                final boolean highlight,
                final boolean favorite) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.highlight = highlight;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
