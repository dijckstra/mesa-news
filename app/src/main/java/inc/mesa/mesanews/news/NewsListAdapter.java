package inc.mesa.mesanews.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import inc.mesa.mesanews.R;
import inc.mesa.mesanews.data.News;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private static final String TAG = "NewsListAdapter";

    private Context context;
    private List<News> newsList;
    private View.OnClickListener onClickListener;
    private boolean highlight;

    private RecyclerView recyclerView;

    public NewsListAdapter(final Context context,
                           final List<News> newsList,
                           final NewsContract.View newsView,
                           final boolean highlight) {
        this.context = context;
        this.newsList = newsList;
        this.onClickListener = view -> {
            int pos = recyclerView.getChildAdapterPosition(view);
            News news = this.newsList.get(pos);
            String newsId = news.getId();

            newsView.showArticle(view, newsId);
        };
        this.highlight = highlight;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        int layoutResId = this.highlight
                    ? R.layout.item_carousel_news
                    : R.layout.item_list_news;

        View v = LayoutInflater.from(this.context).inflate(layoutResId, parent, false);
        v.setOnClickListener(this.onClickListener);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {
        News news = newsList.get(pos);

        holder.tvTitle.setText(news.getTitle());
        holder.tvDescription.setText(news.getDescription());

        Picasso.get()
                .load(news.getImageUrl())
                .into(holder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        return this.newsList.size();
    }

    public void appendData(final List<News> news) {
        this.newsList.addAll(news);
        notifyItemRangeChanged(0, getItemCount());
    }

    public void replaceData(final List<News> news) {
        this.newsList = news;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivThumbnail;
        private TextView tvTitle;
        private TextView tvDescription;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
