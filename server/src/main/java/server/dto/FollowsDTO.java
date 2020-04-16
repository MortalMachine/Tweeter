package server.dto;

import java.util.List;

import model.domain.User;

public class FollowsDTO {
    private List<User> users;
    private boolean hasMorePages;
    public FollowsDTO(List<User> users, boolean hasMorePages) {
        this.users = users;
        this.hasMorePages = hasMorePages;
    }
    public List<User> getUsers() {
        return users;
    }
    public boolean hasMorePages() {
        return hasMorePages;
    }
}
