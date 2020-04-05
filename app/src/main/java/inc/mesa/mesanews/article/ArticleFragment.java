package inc.mesa.mesanews.article;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import inc.mesa.mesanews.R;
import inc.mesa.mesanews.data.source.NewsRepository;

public class ArticleFragment extends Fragment implements ArticleContract.View {

    private static final String TAG = "ArticleFragment";

    private WebView webView;
    private MenuItem favoriteMenu;

    private ArticleContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_article, container, false);

        // Create the presenter
        int newsId = ArticleFragmentArgs.fromBundle(getArguments()).getNewsId();
        presenter = new ArticlePresenter(NewsRepository.getInstance(), this, 0);

        // Configure WebView
        webView = root.findViewById(R.id.wv_container);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
        inflater.inflate(R.menu.article_menu, menu);

        favoriteMenu = menu.findItem(R.id.menu_favorite);
        presenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if (item.getItemId() == R.id.menu_favorite) {
            boolean favorite = item.isChecked();
            presenter.toggleFavorite(favorite);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayArticle(final String url) {
        webView.loadUrl(url);
    }

    @Override
    public void showMarkedAsFavorite(final boolean favorite) {
        favoriteMenu.setChecked(favorite);

        if (favorite) {
            favoriteMenu.setIcon(android.R.drawable.btn_plus);
        } else {
            favoriteMenu.setIcon(android.R.drawable.btn_minus);
        }
    }
}
