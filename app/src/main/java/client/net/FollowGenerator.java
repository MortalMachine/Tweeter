package client.net;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import model.domain.Follow;
import model.domain.User;

/**
 * A temporary class that generates and returns Follow objects. This class may be removed when the
 * server is created and the ServerFacade no longer needs to return dummy data.
 */
public class FollowGenerator {

    private static FollowGenerator followGenerator;

    /**
     * An enum used to specify the sort order of {@link Follow} object returned by this generator.
     * {@link #FOLLOWER_FOLLOWEE} specifies a primary sort by follower alias with a secondary sort
     * of followee alias. {@link #FOLLOWEE_FOLLOWER} specifies the opposite.
     */
    public enum Sort {
        FOLLOWER_FOLLOWEE, FOLLOWEE_FOLLOWER
    }

    /**
     * A private constructor that ensures no instances of this class can be created from outside
     * the class.
     */
    private FollowGenerator() {}

    /**
     * Returns the singleton instance of the class
     *
     * @return the instance.
     */
    public static FollowGenerator getInstance() {
        if(followGenerator == null) {
            followGenerator = new FollowGenerator();
        }

        return followGenerator;
    }

    /**
     * Randomly generates the specified number of {@link User} objects, then randomly generates
     * {@link Follow} objects for the generated users. Ensures that each {@link User} has between
     * 'minFollowersPerUser' and 'maxFollowersPerUser'. Makes no guarantees about how many users a
     * user follows.
     *
     * @param userCount the number of users to generate.
     * @param minFollowersPerUser the minimum number of followers each user will have.
     * @param maxFollowersPerUser the maximum number of followers each user will have.
     * @return the generated {@link Follow} objects.
     */
    public List<Follow> generateUsersAndFollowers(User follower, int userCount, int minFollowersPerUser,
                                                  int maxFollowersPerUser, Sort sortOrder) {
        List<User> users = UserGenerator.getInstance().generateUsers(userCount);
        users.add(follower);
        return genFollowersHandler(users, minFollowersPerUser, maxFollowersPerUser, sortOrder);
    }

    public List<Follow> generateUsersAndFollowees(User followee, int userCount, int minFolloweesPerUser,
                                                  int maxFolloweesPerUser, Sort sortOrder) {
        List<User> users = UserGenerator.getInstance().generateUsers(userCount);
        users.add(followee);
        return genFolloweesHandler(users, minFolloweesPerUser, maxFolloweesPerUser, sortOrder);
    }

    /**
     * Randomly Generates {@link Follow} objects from the specified list of users. Ensures that each
     * {@link User} has between 'minFollowersPerUser' and 'maxFollowersPerUser'. Makes no guarantees
     * about how many users a user follows.
     *
     * @param users the list of users to be used to generate follow objects.
     * @param minFollowersPerUser the minimum number of followers each user will have.
     * @param maxFollowersPerUser the maximum number of followers each user will have.
     * @param sortOrder specifies the sort order or returned results.
     * @return the generated {@link Follow} objects.
     */
    @SuppressWarnings("WeakerAccess")
    public List<Follow> genFollowersHandler( List<User> users,
                                            int minFollowersPerUser,
                                            int maxFollowersPerUser,
                                            Sort sortOrder) {
        List<Follow> follows = new ArrayList<>();

        if(users.size() == 0) {
            return follows;
        }

        assert minFollowersPerUser >= 0 : minFollowersPerUser;
        assert maxFollowersPerUser < users.size() : maxFollowersPerUser;

        // For each user, generate a random number of followers between the specified min and max
        Random random = new Random();
        for(User user : users) {
            int numbFollowersToGenerate = random.nextInt(
                    maxFollowersPerUser - minFollowersPerUser) + minFollowersPerUser;

            generateFollowersForUser(numbFollowersToGenerate, user, users, follows);
        }

        // Add the test user and make him follow everyone
        //User testUser = new User("Test", "User", "@test-user", UserGenerator.MALE_IMAGE_URL);

/*        for(User user : users) {
            Follow follow = new Follow(testUser, user);
            follows.add(follow);
        }*/

        // Sort by the specified sort order
        switch (sortOrder) {
            case FOLLOWEE_FOLLOWER:
                Collections.sort(follows, new Comparator<Follow>() {
                    @Override
                    public int compare(Follow follow1, Follow follow2) {
                        int result = follow1.getFollowee().compareTo(
                                follow2.getFollowee());

                        if(result == 0) {
                            return follow1.getFollower().compareTo(
                                    follow2.getFollower());
                        }

                        return result;
                    }
                });
                break;
            case FOLLOWER_FOLLOWEE:
                Collections.sort(follows, new Comparator<Follow>() {
                    @Override
                    public int compare(Follow follow1, Follow follow2) {
                        int result = follow1.getFollower().compareTo(
                                follow2.getFollower());

                        if(result == 0) {
                            return follow1.getFollowee().compareTo(
                                    follow2.getFollowee());
                        }

                        return result;
                    }
                });
                break;
            default:
                // It should be impossible to get here
                assert false;
        }

        return follows;
    }

    @SuppressWarnings("WeakerAccess")
    public List<Follow> genFolloweesHandler( List<User> users,
                                            int minFolloweesPerUser,
                                            int maxFolloweesPerUser,
                                            Sort sortOrder) {
        List<Follow> follows = new ArrayList<>();

        if(users.size() == 0) {
            return follows;
        }

        assert minFolloweesPerUser >= 0 : minFolloweesPerUser;
        assert maxFolloweesPerUser < users.size() : maxFolloweesPerUser;

        // For each user, generate a random number of followers between the specified min and max
        Random random = new Random();
        for(User user : users) {
            int numbFolloweesToGenerate = random.nextInt(
                    maxFolloweesPerUser - minFolloweesPerUser) + minFolloweesPerUser;

            generateFolloweesForUser(numbFolloweesToGenerate, user, users, follows);
        }

        // Sort by the specified sort order
        switch (sortOrder) {
            case FOLLOWEE_FOLLOWER:
                Collections.sort(follows, new Comparator<Follow>() {
                    @Override
                    public int compare(Follow follow1, Follow follow2) {
                        int result = follow1.getFollowee().compareTo(
                                follow2.getFollowee());

                        if(result == 0) {
                            return follow1.getFollower().compareTo(
                                    follow2.getFollower());
                        }

                        return result;
                    }
                });
                break;
            case FOLLOWER_FOLLOWEE:
                Collections.sort(follows, new Comparator<Follow>() {
                    @Override
                    public int compare(Follow follow1, Follow follow2) {
                        int result = follow1.getFollower().compareTo(
                                follow2.getFollower());

                        if(result == 0) {
                            return follow1.getFollowee().compareTo(
                                    follow2.getFollowee());
                        }

                        return result;
                    }
                });
                break;
            default:
                // It should be impossible to get here
                assert false;
        }

        return follows;
    }

    private void generateFollowersForUser(int numbFollowersToGenerate, User user,  List<User> users, List<Follow> follows) {

        Random random = new Random();
        Set<User> selectedFollowers = new HashSet<>();

        while(selectedFollowers.size() < numbFollowersToGenerate) {
            User follower;
            do {
                follower = users.get(random.nextInt(users.size()));
            } while (user == follower || selectedFollowers.contains(follower));

            selectedFollowers.add(follower);
            follows.add(new Follow(follower, user));
        }
    }

    private void generateFolloweesForUser(int numbFolloweesToGenerate, User user,  List<User> users, List<Follow> follows) {

        Random random = new Random();
        Set<User> selectedFollowees = new HashSet<>();

        while(selectedFollowees.size() < numbFolloweesToGenerate) {
            User followee;
            do {
                followee = users.get(random.nextInt(users.size()));
            } while (user == followee || selectedFollowees.contains(followee));

            selectedFollowees.add(followee);
            follows.add(new Follow(user, followee));
        }
    }

}
