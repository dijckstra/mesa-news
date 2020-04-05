package inc.mesa.mesanews.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import inc.mesa.mesanews.R;
import inc.mesa.mesanews.data.News;
import inc.mesa.mesanews.data.source.NewsRepository;

public class NewsFragment extends Fragment implements NewsContract.View {

    private static final String TAG = "NewsFragment";

    private RecyclerView highlightsRecyclerView;
    private RecyclerView latestNewsRecyclerView;
    private NewsListAdapter highlightsListAdapter;
    private NewsListAdapter latestNewsAdapter;

    private NewsContract.Presenter presenter;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        // Create the presenter
        presenter = new NewsPresenter(NewsRepository.getInstance(), this);

        // Set up RecyclerViews
        highlightsRecyclerView = root.findViewById(R.id.rv_highlights);
        latestNewsRecyclerView = root.findViewById(R.id.rv_latest_news);


        highlightsListAdapter = new NewsListAdapter(getContext(), new ArrayList<>(), this, true);
        highlightsRecyclerView.setAdapter(highlightsListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                                                                    LinearLayoutManager.HORIZONTAL,
                                                                    false);
        highlightsRecyclerView.setLayoutManager(layoutManager);

        latestNewsAdapter = new NewsListAdapter(getContext(), new ArrayList<>(), this, false);
        latestNewsRecyclerView.setAdapter(latestNewsAdapter);
        latestNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    /* View contract methods */
    @Override
    public void showHighlights(final List<News> highlights) {
        highlightsListAdapter.replaceData(highlights);
        highlightsRecyclerView.setAdapter(highlightsListAdapter);
    }

    @Override
    public void showLatestNews(final List<News> latestNews) {
        latestNewsAdapter.replaceData(latestNews);
        latestNewsRecyclerView.setAdapter(latestNewsAdapter);
    }

    @Override
    public void showArticle(final View view, final int newsId) {
        NewsFragmentDirections.OpenArticle action = NewsFragmentDirections.openArticle(newsId);
        Navigation.findNavController(view).navigate(action);
    }
}
