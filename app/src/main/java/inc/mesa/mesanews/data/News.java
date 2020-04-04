package inc.mesa.mesanews.data;

public class News {

    private String title;
    private String description;
    private String imageUrl;
    private String url;

    public News(final String title,
                final String description,
                final String thumbnail,
                final String url) {
        this.title = title;
        this.description = description;
        this.imageUrl = thumbnail;
        this.url = url;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
