package server.service;

import java.util.ArrayList;
import java.util.List;

import server.dto.FollowsDTO;
import model.domain.User;
import model.service.FollowerService;
import request.FollowersRequest;
import response.FollowersResponse;
import server.dao.follows.FollowsDynamoDAO;
import server.dao.follows.IFollowsDAO;
import server.dao.user.IUserDAO;
import server.dao.user.UserDynamoDAO;
import server.dto.UserDTO;
import server.s3.S3AO;

public class FollowerServiceImpl implements FollowerService {
    @Override
    public FollowersResponse getFollowers(FollowersRequest request) {
        IFollowsDAO followsDAO = new FollowsDynamoDAO();
        IUserDAO userDAO = new UserDynamoDAO();
        S3AO s3AO = new S3AO();
        User followee = request.getFollowee();

        /* Query user's followers from follows table */
        try {
            FollowsDTO followsDTO = followsDAO.getFollowers(followee.getAlias(), request.getLastFollower());
            List<User> followers = followsDTO.getUsers();
            /* Fill list of followers for page */
            for (User follower : followers) {
                UserDTO userDTO = userDAO.getUserItem(follower.getAlias(), follower.getFirstName());
                User followerFromUserTable = userDTO.getUser();
                String imageUrl = s3AO.getProfilePicFromS3(followerFromUserTable.getImageUrl());
                /* follower has a null lastname and imageurl needs to change the s3 file path
                 to a client-friendly url */
                follower.setLastName(followerFromUserTable.getLastName());
                follower.setImageUrl(imageUrl);
            }

            System.out.println("Sending followers response");
            return new FollowersResponse(followers, followsDTO.hasMorePages());

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new FollowersResponse(e.getMessage());
        }
    }
}
