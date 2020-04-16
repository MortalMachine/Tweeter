package server.service;

import java.util.List;

import server.dto.FollowsDTO;
import model.domain.User;
import model.service.FollowingService;
import request.FollowingRequest;
import response.FollowingResponse;
import server.dao.follows.FollowsDynamoDAO;
import server.dao.follows.IFollowsDAO;
import server.dao.user.IUserDAO;
import server.dao.user.UserDynamoDAO;
import server.dto.UserDTO;
import server.s3.S3AO;

public class FollowingServiceImpl implements FollowingService {
    public FollowingResponse getFollowees(FollowingRequest request) {
        IFollowsDAO followsDAO = new FollowsDynamoDAO();
        IUserDAO userDAO = new UserDynamoDAO();
        S3AO s3AO = new S3AO();
        User follower = request.getFollower();

        /* Query user's followees from follows table */

        try {
            FollowsDTO followsDTO = followsDAO.getFollowees(follower.getAlias(), request.getLastFollowee());
            List<User> followees = followsDTO.getUsers();
            /* Fill list of followees for page */
            for (User followee : followees) {
                UserDTO userDTO = userDAO.getUserItem(followee.getAlias(), followee.getFirstName());
                User followeeFromUserTable = userDTO.getUser();
                String imageUrl = s3AO.getProfilePicFromS3(followeeFromUserTable.getImageUrl());
                /* followee has a null lastname and imageurl needs to change the s3 file path
                 to a client-friendly url */
                followee.setLastName(followeeFromUserTable.getLastName());
                followee.setImageUrl(imageUrl);
            }

            System.out.println(followees);
            System.out.println("Sending followees response");
            return new FollowingResponse(followees, followsDTO.hasMorePages());

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new FollowingResponse(e.getMessage());
        }
    }
}
