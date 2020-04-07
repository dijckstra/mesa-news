package inc.mesa.mesanews.filter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import inc.mesa.mesanews.MainNavDirections;
import inc.mesa.mesanews.R;
import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.dep.DependencyProvider;
import inc.mesa.mesanews.ui.NewsItemListener;
import inc.mesa.mesanews.ui.NewsListAdapter;

import static inc.mesa.mesanews.filter.FilterContract.DATE;
import static inc.mesa.mesanews.filter.FilterContract.FAVORITES;
import static inc.mesa.mesanews.filter.FilterContract.NONE;

public class FilterFragment extends Fragment implements FilterContract.View, AdapterView.OnItemSelectedListener {

    private static final String TAG = "FilterFragment";
    private static final String BUNDLE_NEWS_RECYCLER_LAYOUT = "inc.mesa.mesanews.newsrecycler.layout";

    private RecyclerView newsRecyclerView;
    private NewsListAdapter newsListAdapter;

    private FilterContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_filter, container, false);

        // Create the presenter
        presenter = new FilterPresenter(DependencyProvider.getNewsRepository(),
                                        this);

        NewsItemListener newsItemListener = new NewsItemListener() {
            @Override
            public void onNewsClick(final String newsId) {
                showArticle(newsId);
            }

            @Override
            public void onNewsFavoriteClick(final String newsId) {
                presenter.toggleFavorite(newsId);
                presenter.loadNews();
            }
        };

        newsRecyclerView = root.findViewById(R.id.rv_news);
        newsListAdapter = new NewsListAdapter(getContext(), new ArrayList<>(), newsItemListener);

        newsRecyclerView.setAdapter(newsListAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final Spinner spFilter = root.findViewById(R.id.sp_filter);
        spFilter.setAdapter(getArrayAdapter());
        spFilter.setOnItemSelectedListener(this);

        return root;
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
            Parcelable savedNewsRecyclerState = savedInstanceState.getParcelable(BUNDLE_NEWS_RECYCLER_LAYOUT);
            newsRecyclerView.getLayoutManager().onRestoreInstanceState(savedNewsRecyclerState);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_NEWS_RECYCLER_LAYOUT,
                               newsRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    /* View contract methods */
    @Override
    public void showNews(final List<News> news) {
        newsListAdapter.replaceData(news);
    }

    @Override
    public void showArticle(final String newsId) {
        MainNavDirections.ActionDisplayArticle action = MainNavDirections.actionDisplayArticle(newsId);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(action);
    }

    /* Spinner selection listener methods */
    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int pos, final long id) {
        String filterType;
        switch (pos) {
            case 1:
                filterType = FAVORITES;
                break;
            case 2:
                filterType = DATE;
                break;
            default:
                filterType = NONE;
                break;
        }

        presenter.setFiltering(filterType);
        presenter.loadNews();
    }

    @Override
    public void onNothingSelected(final AdapterView<?> parent) {
        // no-op
    }

    /* Private methods */
    private SpinnerAdapter getArrayAdapter() {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),
                                                                                  R.array.filter_types,
                                                                                  android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }
}
