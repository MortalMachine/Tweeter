package server.lambda;

import java.util.ArrayList;
import java.util.List;

import model.domain.User;

public class UserGenerator {
    private static UserGenerator instance;

    private UserGenerator() {}

    public static UserGenerator getInstance() {
        if (instance == null) {
            instance = new UserGenerator();
        }
        return instance;
    }

    public List<User> getNUsers(int n){
        List<User> users = new ArrayList<>();
        for(int i=0; i < n; i++){
            users.add(new User("fname" + Integer.toString(i),
                    "lname" + Integer.toString(i),
                    "@tempAlias" + Integer.toString(i),
                    "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"));
        }
        return users;
    }

}
