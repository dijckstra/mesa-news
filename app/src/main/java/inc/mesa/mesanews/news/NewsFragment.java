package inc.mesa.mesanews.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import inc.mesa.mesanews.R;
import inc.mesa.mesanews.data.News;

public class NewsFragment extends Fragment implements NewsContract.View {

    private static final String TAG = "NewsFragment";

    private RecyclerView highlightsRecyclerView;
    private RecyclerView latestNewsRecyclerView;
    private NewsListAdapter newsListAdapter;

    private NewsContract.Presenter presenter;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_news, container, false);

        // Create the presenter
        presenter = new NewsPresenter(this);

        // Set up RecyclerViews
        highlightsRecyclerView = fragment.findViewById(R.id.rv_highlights);
        latestNewsRecyclerView = fragment.findViewById(R.id.rv_latest_news);

        highlightsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        latestNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        newsListAdapter = new NewsListAdapter(getContext(), new ArrayList<News>(), presenter);
        highlightsRecyclerView.setAdapter(newsListAdapter);
        latestNewsRecyclerView.setAdapter(newsListAdapter);

        return fragment;
    }
}
