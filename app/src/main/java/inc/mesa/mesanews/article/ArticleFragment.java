package inc.mesa.mesanews.article;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import inc.mesa.mesanews.R;
import inc.mesa.mesanews.data.source.NewsRepository;

public class ArticleFragment extends Fragment implements ArticleContract.View {

    private WebView webView;
    private ArticleContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_article, container, false);

        // Create the presenter
        int newsId = ArticleFragmentArgs.fromBundle(getArguments()).getNewsId();
        presenter = new ArticlePresenter(NewsRepository.getInstance(), this, newsId);

        // Configure WebView
        webView = root.findViewById(R.id.wv_container);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void displayArticle(final String url) {
        webView.loadUrl(url);
    }

    @Override
    public void showMarkedAsFavorite(final boolean favorite) {

    }
}
