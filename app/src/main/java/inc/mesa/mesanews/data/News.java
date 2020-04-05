package inc.mesa.mesanews.data;

public class News {

    private int id;
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private boolean favorite;

    public News(final int id,
                final String title,
                final String description,
                final String url,
                final String thumbnail,
                final boolean favorite) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = thumbnail;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(final boolean favorite) {
        this.favorite = favorite;
    }
}
