package client.view.home.followers;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.List;

import client.view.IView;
import edu.byu.cs.app.R;
import model.domain.User;
import client.net.Model;
import request.FollowersRequest;
import response.FollowersResponse;
import client.presenter.FollowersPresenter;
import client.presenter.IPresenter;
//import edu.byu.cs.tweeter.view.asyncTasks.GetFollowersTask;
import client.view.activity.PersonActivity;
import client.view.cache.ImageCache;
import client.view.util.ImageUtils;

public class FollowersFragment extends Fragment implements IView, Serializable {
    private static boolean isLoaded = false;
    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;
    private static final int PAGE_SIZE = 10;

    private FollowersPresenter presenter;
    private FollowersRecyclerViewAdapter followersRecyclerViewAdapter;
    private RecyclerView followersRecyclerView;
    private GetFollowersTask getFollowersTask;
    private User user;
    private User loggedInUser;

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
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
        followersRecyclerView.setLayoutManager(layoutManager);

        followersRecyclerViewAdapter = new FollowersRecyclerViewAdapter();
        followersRecyclerView.setAdapter(followersRecyclerViewAdapter);

        followersRecyclerView.addOnScrollListener(new FollowRecyclerViewPaginationScrollListener(layoutManager));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        presenter = new FollowersPresenter(this);

        followersRecyclerView = view.findViewById(R.id.followersRecyclerView);

/*
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followersRecyclerView.setLayoutManager(layoutManager);

        followersRecyclerViewAdapter = new FollowersRecyclerViewAdapter();
        followersRecyclerView.setAdapter(followersRecyclerViewAdapter);

        followersRecyclerView.addOnScrollListener(new FollowRecyclerViewPaginationScrollListener(layoutManager));
*/

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getFollowersTask != null) {
            getFollowersTask.cancel(true);
        }
    }

    private static class GetFollowersTask extends AsyncTask<FollowersRequest, Void, FollowersResponse> {
        private final FollowersPresenter presenter;
        private final FollowersPresenter.GetFollowersAdapter adapter;
        private User taskUser;
        private Exception exception;

        public GetFollowersTask(FollowersPresenter presenter, FollowersPresenter.GetFollowersAdapter adapter) {
            this.presenter = presenter;
            this.adapter = adapter;
            this.presenter.setAdapter(adapter);
        }

        @Override
        protected FollowersResponse doInBackground(FollowersRequest... requests) {
            taskUser = requests[0].getFollowee();
            if (Model.getInstance().getObservers() == null/*taskUser.getObservers() == null*/) {
                //taskUser.updateObservers(new ArrayList<IPresenter>());
                Model.getInstance().updateObservers(new ArrayList<IPresenter>());
            }
            if (!Model.getInstance().getObservers().contains(presenter)/*!taskUser.getObservers().contains(presenter)*/) {
                //taskUser.addObserver(presenter);
                Model.getInstance().addObserver(presenter);
            }

            FollowersResponse response = null;

            try {
                response = presenter.getFollowers(requests[0]);
                loadImages(response);
            }
            catch (IOException e) {
                exception = e;
            }
            return response;
        }

        private void loadImages(FollowersResponse response) {
            for(User user : Model.getInstance().getPageFollowers(taskUser)/*taskUser.getPageFollowers()*//*response.getFollowers()*/) {

                Drawable drawable;

                try {
                    drawable = ImageUtils.drawableFromUrl(user.getImageUrl());
                } catch (IOException e) {
                    Log.e(this.getClass().getName(), e.toString(), e);
                    drawable = null;
                }

                ImageCache.getInstance().cacheImage(user, drawable);
            }
        }

        @Override
        protected void onPostExecute(FollowersResponse response) {
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

    private class FollowersHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Serializable {
        private User follower;
        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;

        FollowersHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);
            itemView.setOnClickListener(this);
/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "You selected '" + userName.getText() + "'.", Toast.LENGTH_SHORT).show();
                }
            });
*/
        }

        void bindUser(User user) {
            follower = user;
            userImage.setImageDrawable(ImageCache.getInstance().getImageDrawable(user));
            userAlias.setText(user.getAlias());
            userName.setText(user.getName());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.putExtra("Person", follower);
            intent.putExtra("LoggedInUser", loggedInUser);
            startActivity(intent);
        }
    }

    private class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<FollowersHolder> implements Serializable, FollowersPresenter.GetFollowersAdapter {

        private final List<User> users = new ArrayList<>();

        private User lastFollower;

        private boolean hasMorePages;
        private boolean isLoading = false;

        FollowersRecyclerViewAdapter() {
            loadMoreItems();
        }

        void addItems(List<User> newUsers) {
            int startInsertPosition = users.size();
            users.addAll(newUsers);
            this.notifyItemRangeInserted(startInsertPosition, newUsers.size());
        }

        void addItem(User user) {
            users.add(user);
            this.notifyItemInserted(users.size() - 1);
        }

        void removeItem(User user) {
            int position = users.indexOf(user);
            users.remove(position);
            this.notifyItemRemoved(position);
        }

        @NonNull
        @Override
        public FollowersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FollowersFragment.this.getContext());
            View view;

            if(isLoading) {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.user_row, parent, false);
            }

            return new FollowersHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FollowersHolder followersHolder, int position) {
            if(!isLoading) {
                followersHolder.bindUser(users.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == users.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }


        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            getFollowersTask = new GetFollowersTask(presenter, this);
            FollowersRequest request = new FollowersRequest(user, PAGE_SIZE, lastFollower);
            getFollowersTask.execute(request);
        }

        @Override
        public void followersRetrieved(List<User> followers, FollowersResponse followersResponse) {
            //List<User> followers = followersResponse.getFollowers();

            lastFollower = (followers.size() > 0) ? followers.get(followers.size() -1) : null;
            hasMorePages = followersResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            followersRecyclerViewAdapter.addItems(followers);
        }

        private void addLoadingFooter() {
            addItem(new User("Dummy", "User", ""));
        }

        private void removeLoadingFooter() {
            if (users.size() > 0) {
                removeItem(users.get(users.size() - 1));
            }
        }
    }

    private class FollowRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener implements Serializable {

        private final LinearLayoutManager layoutManager;

        FollowRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!followersRecyclerViewAdapter.isLoading && followersRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    followersRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
