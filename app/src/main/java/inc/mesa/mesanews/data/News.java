package inc.mesa.mesanews.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class News {

    @PrimaryKey
    @NonNull
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

    public static class Builder {

        private String id;
        private String title;
        private String description;
        private String url;
        private String imageUrl;
        private boolean highlight;
        private boolean favorite;

        public Builder(final News news) {
            this.id = news.id;
            this.title = news.title;
            this.description = news.description;
            this.url = news.url;
            this.imageUrl = news.imageUrl;
            this.highlight = news.highlight;
            this.favorite = news.favorite;
        }

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        public Builder url(final String url) {
            this.url = url;
            return this;
        }

        public Builder imageUrl(final String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder highlight(final boolean highlight) {
            this.highlight = highlight;
            return this;
        }

        public Builder favorite(final boolean favorite) {
            this.favorite = favorite;
            return this;
        }

        public News build() {
            return new News(id, title, description, url, imageUrl, highlight, favorite);
        }
    }
}
