package inc.mesa.mesanews.news;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

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
import inc.mesa.mesanews.R;
import inc.mesa.mesanews.auth.AuthManager;
import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.dep.DependencyProvider;
import inc.mesa.mesanews.ui.RecyclerViewPaginationScrollListener;

import static inc.mesa.mesanews.news.NewsContract.ALL;
import static inc.mesa.mesanews.news.NewsContract.FAVORITES;

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

        // Set up RecyclerViews
        highlightsRecyclerView = root.findViewById(R.id.rv_highlights);
        latestNewsRecyclerView = root.findViewById(R.id.rv_latest_news);


        highlightsListAdapter = new NewsListAdapter(getContext(), new ArrayList<>(), this, true);
        highlightsRecyclerView.setAdapter(highlightsListAdapter);
        LinearLayoutManager highlightsLayoutManager = new LinearLayoutManager(getContext(),
                                                                    LinearLayoutManager.HORIZONTAL,
                                                                    false);
        highlightsRecyclerView.setLayoutManager(highlightsLayoutManager);

        latestNewsAdapter = new NewsListAdapter(getContext(), new ArrayList<>(), this, false);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if (item.getItemId() == R.id.menu_filter) {
            showFilteringMenu();
        }

        return true;
    }

    /* View contract methods */
    @Override
    public void showFilteringMenu() {
        PopupMenu filterMenu = new PopupMenu(getContext(),
                                             getActivity().findViewById(R.id.menu_filter));
        filterMenu.getMenuInflater().inflate(R.menu.filter_news, filterMenu.getMenu());

        filterMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.favorites:
                     presenter.setFiltering(FAVORITES);
                    break;
                case R.id.all:
                default:
                    presenter.setFiltering(ALL);
                    break;
            }

            scrollListener.resetState();
            presenter.loadAllNews();

            return true;
        });

        filterMenu.show();
    }

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
    public void showArticle(final View view, final String newsId) {
        NewsFragmentDirections.OpenArticle action = NewsFragmentDirections.openArticle(newsId);
        Navigation.findNavController(view).navigate(action);
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
