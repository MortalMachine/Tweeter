package client.view.home.story;

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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import client.net.Model;
import response.StoryResponse;
import client.view.IView;
import edu.byu.cs.app.R;
import model.domain.Status;
import model.domain.User;
import request.StoryRequest;
import client.presenter.StoryPresenter;
//import edu.byu.cs.tweeter.view.asyncTasks.GetStoryTask;


public class StoryFragment extends Fragment implements IView, Serializable {
    private boolean isLoaded = false;
    private int hostType;

    private static final int HOST_HOME_FRAGMENT = 0;
    private static final int HOST_PERSON_ACTIVITY = 1;
    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;
    private static final int PAGE_SIZE = 10;

    private StoryPresenter presenter;
    private StoryRecyclerViewAdapter storyRecyclerViewAdapter;
    private RecyclerView storyRecyclerView;
    private GetStoryTask getStoryTask;
    private User user;

    public void setHostType(int type) {
        hostType = type;
    }
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
        storyRecyclerView.setLayoutManager(layoutManager);

        storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
        storyRecyclerView.setAdapter(storyRecyclerViewAdapter);

        storyRecyclerView.addOnScrollListener(new StatusRecyclerViewPaginationScrollListener(layoutManager));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        presenter = new StoryPresenter(this);

        storyRecyclerView = view.findViewById(R.id.storyRecyclerView);

        if (hostType == HOST_PERSON_ACTIVITY) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
            storyRecyclerView.setLayoutManager(layoutManager);

            storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
            storyRecyclerView.setAdapter(storyRecyclerViewAdapter);

            storyRecyclerView.addOnScrollListener(new StatusRecyclerViewPaginationScrollListener(layoutManager));
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getStoryTask != null) {
            getStoryTask.cancel(true);
        }
    }

    private static class GetStoryTask extends AsyncTask<StoryRequest, Void, StoryResponse> {
        private final StoryPresenter presenter;
        private final StoryPresenter.GetStoryAdapter adapter;
        private User taskUser;

        public GetStoryTask(StoryPresenter presenter, StoryPresenter.GetStoryAdapter adapter) {
            this.presenter = presenter;
            this.adapter = adapter;
            this.presenter.setAdapter(this.adapter);
        }

        @Override
        protected StoryResponse doInBackground(StoryRequest... requests) {
            taskUser = requests[0].getUser();

            if (!Model.getInstance().getObservers().contains(presenter)/*!taskUser.getObservers().contains(presenter)*/) {
                //taskUser.addObserver(presenter);
                Model.getInstance().addObserver(presenter);
            }

            StoryResponse response = null;
            try {
                response = presenter.getStory(requests[0]);
            }
            catch (IOException e) {
                response = new StoryResponse(e.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(StoryResponse response) {
            if (response.isSuccess()) {
                //taskUser.notifyObservers();
                Model.getInstance().notifyObservers();
            }
            else {
                presenter.sendErrorMessage(response);
            }
            Model.getInstance().removeObserver(presenter);
        }
    }

    private class StoryHolder extends RecyclerView.ViewHolder implements Serializable {
        private final TextView tvDate;
        private final TextView tvStatus;

        StoryHolder(@NonNull View itemView) {
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

    private class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryHolder> implements Serializable, StoryPresenter.GetStoryAdapter {

        private final List<Status> statuses = new ArrayList<>();

        private Status lastStatus;

        private boolean hasMorePages;
        private boolean isLoading = false;

        StoryRecyclerViewAdapter() {
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
        public StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StoryFragment.this.getContext());
            View view;

            if(isLoading) {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new StoryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryHolder storyHolder, int position) {
            if(!isLoading) {
                storyHolder.bindStatus(statuses.get(position));
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

            getStoryTask = new GetStoryTask(presenter, this);
            StoryRequest request = new StoryRequest(user, PAGE_SIZE, lastStatus);
            getStoryTask.execute(request);
        }

        @Override
        public void statusesRetrieved(List<Status> statuses, StoryResponse storyResponse) {
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() -1) : null;
            hasMorePages = storyResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            storyRecyclerViewAdapter.addItems(statuses);
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

            if (!storyRecyclerViewAdapter.isLoading && storyRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    storyRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }

}
