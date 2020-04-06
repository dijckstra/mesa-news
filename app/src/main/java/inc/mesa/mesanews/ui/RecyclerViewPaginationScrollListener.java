package inc.mesa.mesanews.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = "ScrollListener";

    private int visibleThreshold = 5;
    private int previousTotalItemCount = 0;
    private boolean isLoading = true;

    private RecyclerView.LayoutManager mLayoutManager;

    public RecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition;
        int totalItemCount = mLayoutManager.getItemCount();

        lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount;

            if (totalItemCount == 0) {
                this.isLoading = true;
            }
        }

        if (this.isLoading && (totalItemCount > previousTotalItemCount)) {
            this.isLoading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!this.isLoading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            onLoadMore(totalItemCount);
            this.isLoading = true;
        }
    }

    public void resetState() {
        this.previousTotalItemCount = 0;
        this.isLoading = true;
    }

    public abstract void onLoadMore(int totalItemsCount);
}
