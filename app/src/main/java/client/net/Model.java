package client.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.domain.AuthToken;
import model.domain.Feed;
import model.domain.Story;
import model.domain.User;
import response.GetUserResponse;
import client.presenter.IPresenter;

public class Model extends Subject {
    private static Model instance;
    private ArrayList<IPresenter> observers;
    private Map<User, List<User>> followeesByFollower;
    private Map<User, List<User>> followersByFollowee;
    private Map<User, List<User>> pageFolloweesByFollower;
    private Map<User, List<User>> pageFollowersByFollowee;
    private Map<User, Story> storyByUser;
    private Feed feed;
    private User user;
    private AuthToken token;

    private Model() {
        observers = new ArrayList<>();
        storyByUser = new HashMap<>();
        feed = new Feed();
        followersByFollowee = new HashMap<>();
        followeesByFollower = new HashMap<>();
        pageFollowersByFollowee = new HashMap<>();
        pageFolloweesByFollower = new HashMap<>();
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    @Override
    public void addObserver(IPresenter o) {
        observers.add(o);
    }
    @Override
    public void removeObserver(IPresenter o) {
        observers.remove(o);
    }
    @Override
    public void notifyObservers() {
        for (IPresenter o : observers) {
            o.update();
        }
    }
    public void updateObservers(ArrayList<IPresenter> observers) {
        this.observers = observers;
    }
    public ArrayList<IPresenter> getObservers() { return observers; }

    public void setAuthToken(AuthToken newToken) {
        token = newToken;
    }

    private boolean aliasAvailable(User other) {
        if (user != null && user.equals(other)) {
            return false;
        }
        return true;
    }
    public void setUser(User u) {
        user = u;
    }
    public boolean isUser(User person) {
        return user.equals(person);
    }
    public GetUserResponse getUser() {
        if (user != null) {
            User currentUser = user;
            GetUserResponse response = new GetUserResponse(true, null, currentUser);
            return response;
        }
        else {
            GetUserResponse response = new GetUserResponse(false, "User hasn't signed in yet", null);
            return response;
        }
    }

    public boolean hasFollowee(User person) {
        if (followeesByFollower.get(user) == null) {
            return false;
        }
        return followeesByFollower.get(user).contains(person);
    }
    public void addFollowee(User newFollowee) {
        followeesByFollower.get(user).add(0, newFollowee);
    }
    public void addFollowees(List<User> followees) {
        if (followeesByFollower.get(user) == null) {
            followeesByFollower.put(user, new ArrayList<>());
        }
        followeesByFollower.get(user).addAll(followees);
    }
    public void removeFollowee(User followee) {
        followeesByFollower.get(user).remove(followee);
    }
    public void setPageFollowees(User follower, List<User> pageFollowees) {
        pageFolloweesByFollower.put(follower, pageFollowees);
    }
    public List<User> getPageFollowees(User follower) {
        return pageFolloweesByFollower.get(follower);
    }
    public List<User> getFollowees(User follower) {
        return followeesByFollower.get(follower);
    }
    public void addFollowers(List<User> followers) {
        if (followersByFollowee.get(user) == null) {
            followersByFollowee.put(user, new ArrayList<>());
        }
        followersByFollowee.get(user).addAll(followers);
    }
    public void setPageFollowers(User followee, List<User> pageFollowers) {
        pageFollowersByFollowee.put(followee, pageFollowers);
    }
    public List<User> getPageFollowers(User followee) {
        return pageFollowersByFollowee.get(followee);
    }
    public List<User> getFollowers(User followee) {
        return followersByFollowee.get(followee);
    }
    public void setStory(User user, Story story) {
        storyByUser.put(user, story);
    }
    public Story getStory(User user) {
        return storyByUser.get(user);
    }
    public Feed getFeed() {
        return feed;
    }
    public AuthToken getToken() {
        return token;
    }
    public void clear() {
        user = null;
        followeesByFollower = null;
    }
}
