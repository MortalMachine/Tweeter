package client.view.home.feed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import client.view.IView;
import edu.byu.cs.app.R;
import model.domain.Status;
import model.domain.User;
import client.net.Model;
import request.FeedRequest;
import response.FeedResponse;
import client.presenter.FeedPresenter;
//import edu.byu.cs.tweeter.view.asyncTasks.GetFeedTask;

public class FeedFragment extends Fragment implements IView, Serializable {
    private static boolean isLoaded = false;
    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;
    private static final int PAGE_SIZE = 10;

    private FeedPresenter presenter;
    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;
    private RecyclerView feedRecyclerView;
    private GetFeedTask getFeedTask;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() { return user; }
    @Override
    public void displayToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean isAlreadyLoaded() {
        return isLoaded;
    }
    @Override
    public void lazyLoad() {
        if (!isLoaded) {
            isLoaded = true;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        feedRecyclerView.setLayoutManager(layoutManager);

        feedRecyclerViewAdapter = new FeedRecyclerViewAdapter();
        feedRecyclerView.setAdapter(feedRecyclerViewAdapter);

        feedRecyclerView.addOnScrollListener(new StatusRecyclerViewPaginationScrollListener(layoutManager));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        presenter = new FeedPresenter(this);

        feedRecyclerView = view.findViewById(R.id.feedRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        feedRecyclerView.setLayoutManager(layoutManager);

        feedRecyclerViewAdapter = new FeedRecyclerViewAdapter();
        feedRecyclerView.setAdapter(feedRecyclerViewAdapter);

        feedRecyclerView.addOnScrollListener(new StatusRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getFeedTask != null) {
            getFeedTask.cancel(true);
        }
    }

    private static class GetFeedTask extends AsyncTask<FeedRequest, Void, FeedResponse> implements Serializable {
        private final FeedPresenter presenter;
        private final FeedPresenter.GetFeedAdapter adapter;
        private User taskUser;

        public GetFeedTask(FeedPresenter presenter, FeedPresenter.GetFeedAdapter adapter) {
            this.presenter = presenter;
            this.adapter = adapter;
            this.presenter.setAdapter(adapter);
        }

        @Override
        protected FeedResponse doInBackground(FeedRequest... requests) {
            taskUser = requests[0].getUser();
            if (!Model.getInstance().getObservers().contains(presenter)/*!taskUser.getObservers().contains(presenter)*/) {
                //taskUser.addObserver(presenter);
                Model.getInstance().addObserver(presenter);
            }
            FeedResponse response = null;
            try {
                response = presenter.getFeed(requests[0]);
            } catch (Exception e) {
                response = new FeedResponse(e.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(FeedResponse response) {
            if (response.isSuccess()) {
                Model.getInstance().notifyObservers();
            }
            else {
                presenter.sendErrorMessage(response);
            }
            Model.getInstance().removeObserver(presenter);
        }
    }

    private class FeedHolder extends RecyclerView.ViewHolder implements Serializable {
        private final TextView tvDate;
        private final TextView tvStatus;

        FeedHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_date);
            tvStatus = itemView.findViewById(R.id.tv_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "You selected '" + tvStatus.getText() + ".", Toast.LENGTH_SHORT).show();
                }
            });
        }

        void bindStatus(Status status) {
            Date date = new Date(status.getMilliseconds());
            tvDate.setText(date.toString());
            tvStatus.setText(status.getMessage());
        }
    }

    private class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedHolder> implements Serializable, FeedPresenter.GetFeedAdapter {

        private final List<Status> statuses = new ArrayList<>();

        private Status lastStatus;

        private boolean hasMorePages;
        private boolean isLoading = false;

        FeedRecyclerViewAdapter() {
            loadMoreItems();
        }

        void addItems(List<Status> newStatuses) {
            int startInsertPosition = statuses.size();
            statuses.addAll(newStatuses);
            this.notifyItemRangeInserted(startInsertPosition, newStatuses.size());
        }

        void addItem(Status status) {
            statuses.add(status);
            this.notifyItemInserted(statuses.size() - 1);
        }

        void removeItem(Status status) {
            int position = statuses.indexOf(status);
            statuses.remove(position);
            this.notifyItemRemoved(position);
        }

        @NonNull
        @Override
        public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FeedFragment.this.getContext());
            View view;

            if(isLoading) {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new FeedHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FeedHolder feedHolder, int position) {
            if(!isLoading) {
                feedHolder.bindStatus(statuses.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return statuses.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == statuses.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            getFeedTask = new GetFeedTask(presenter, this);
            FeedRequest request = new FeedRequest(user, PAGE_SIZE, lastStatus);
            getFeedTask.execute(request);
        }

        @Override
        public void statusesRetrieved(List<Status> statuses, FeedResponse feedResponse) {
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() -1) : null;
            hasMorePages = feedResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            feedRecyclerViewAdapter.addItems(statuses);
        }

        private void addLoadingFooter() {
            Status loadingFooter = new Status();
            loadingFooter.setMessage("Dummy");
            addItem(loadingFooter);
        }

        private void removeLoadingFooter() {
            removeItem(statuses.get(statuses.size() - 1));
        }
    }

    private class StatusRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener implements Serializable {

        private final LinearLayoutManager layoutManager;

        StatusRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!feedRecyclerViewAdapter.isLoading && feedRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    feedRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }

}
