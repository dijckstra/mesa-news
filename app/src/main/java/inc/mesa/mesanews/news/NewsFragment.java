package inc.mesa.mesanews.news;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import inc.mesa.mesanews.MainNavDirections;
import inc.mesa.mesanews.R;
import inc.mesa.mesanews.auth.AuthManager;
import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.dep.DependencyProvider;
import inc.mesa.mesanews.ui.NewsItemListener;
import inc.mesa.mesanews.ui.NewsListAdapter;
import inc.mesa.mesanews.ui.RecyclerViewPaginationScrollListener;

public class NewsFragment extends Fragment implements NewsContract.View {

    private static final String BUNDLE_HIGHLIGHTS_RECYCLER_LAYOUT = "inc.mesa.mesanews.highlightsrecycler.layout";
    private static final String BUNDLE_NEWS_RECYCLER_LAYOUT = "inc.mesa.mesanews.newsrecycler.layout";

    private static final String TAG = "NewsFragment";

    private RecyclerView highlightsRecyclerView;
    private RecyclerView latestNewsRecyclerView;
    private NewsListAdapter highlightsListAdapter;
    private NewsListAdapter latestNewsAdapter;
    private RecyclerViewPaginationScrollListener scrollListener;

    private AuthManager authManager;
    private NewsContract.Presenter presenter;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        authManager = DependencyProvider.getAuthManager();

        // Create the presenter
        presenter = new NewsPresenter(DependencyProvider.getNewsRepository(),
                                      this);

        NewsItemListener newsItemListener = new NewsItemListener() {
            @Override
            public void onNewsClick(final String newsId) {
                showArticle(newsId);
            }

            @Override
            public void onNewsFavoriteClick(final String newsId) {
                presenter.toggleFavorite(newsId);
                presenter.refreshNews();
            }
        };

        // Set up RecyclerViews
        highlightsRecyclerView = root.findViewById(R.id.rv_highlights);
        latestNewsRecyclerView = root.findViewById(R.id.rv_latest_news);


        highlightsListAdapter = new NewsListAdapter(getContext(), new ArrayList<>(), newsItemListener, true);
        highlightsRecyclerView.setAdapter(highlightsListAdapter);
        LinearLayoutManager highlightsLayoutManager = new LinearLayoutManager(getContext(),
                                                                    LinearLayoutManager.HORIZONTAL,
                                                                    false);
        highlightsRecyclerView.setLayoutManager(highlightsLayoutManager);

        latestNewsAdapter = new NewsListAdapter(getContext(), new ArrayList<>(), newsItemListener, false);
        LinearLayoutManager newsLayoutManager = new LinearLayoutManager(this.getContext());
        scrollListener = getScrollListener(newsLayoutManager);
        latestNewsRecyclerView.setAdapter(latestNewsAdapter);
        latestNewsRecyclerView.setLayoutManager(newsLayoutManager);
        latestNewsRecyclerView.addOnScrollListener(scrollListener);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController controller = Navigation.findNavController(view);
        String authToken = authManager.getAuthToken();

        if (authToken == null) {
            controller.navigate(R.id.action_display_splash);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null) {
            Parcelable savedHighlightsRecyclerState = savedInstanceState.getParcelable(BUNDLE_HIGHLIGHTS_RECYCLER_LAYOUT);
            highlightsRecyclerView.getLayoutManager().onRestoreInstanceState(savedHighlightsRecyclerState);

            Parcelable savedNewsRecyclerState = savedInstanceState.getParcelable(BUNDLE_NEWS_RECYCLER_LAYOUT);
            latestNewsRecyclerView.getLayoutManager().onRestoreInstanceState(savedNewsRecyclerState);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_HIGHLIGHTS_RECYCLER_LAYOUT,
                               highlightsRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(BUNDLE_NEWS_RECYCLER_LAYOUT,
                               latestNewsRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
        inflater.inflate(R.menu.news_fragment_menu, menu);
    }

    /* View contract methods */
    @Override
    public void showHighlights(final List<News> highlights) {
        highlightsListAdapter.replaceData(highlights);
    }

    @Override
    public void showLatestNews(final List<News> latestNews, boolean shouldReplace) {
        if (shouldReplace) {
            latestNewsAdapter.replaceData(latestNews);
        } else {
            latestNewsAdapter.appendData(latestNews);
        }
    }

    @Override
    public void showArticle(final String newsId) {
        MainNavDirections.ActionDisplayArticle action = MainNavDirections.actionDisplayArticle(newsId);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(action);
    }

    @Override
    public void showLoadingNewsError() {
        scrollListener.resetState();
    }

    /* Private methods */
    private RecyclerViewPaginationScrollListener getScrollListener(final LinearLayoutManager newsLayoutManager) {
        return new RecyclerViewPaginationScrollListener(newsLayoutManager) {
            @Override
            public void onLoadMore(final int totalItemsCount) {
                presenter.loadNewsPage(totalItemsCount);
            }
        };
    }
}
