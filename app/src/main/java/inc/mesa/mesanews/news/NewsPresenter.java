package inc.mesa.mesanews.news;

import androidx.annotation.NonNull;

public class NewsPresenter implements NewsContract.Presenter {

    private NewsContract.View view;

    public NewsPresenter(@NonNull final NewsContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        // TODO fetch tasks from API
    }
}
