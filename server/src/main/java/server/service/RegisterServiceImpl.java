package server.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import model.domain.AuthToken;
import model.domain.User;
import model.service.RegisterService;
import request.RegisterRequest;
import response.RegisterResponse;
import server.dao.auth.AuthDynamoDAO;
import server.dao.auth.IAuthDAO;
import server.dao.user.IUserDAO;
import server.dao.user.UserDynamoDAO;
import server.dto.UserDTO;
import server.s3.AbstractS3AO;
import server.s3.S3AO;

public class RegisterServiceImpl implements RegisterService {
    @Override
    public RegisterResponse register(RegisterRequest request) {
        IUserDAO userDAO = new UserDynamoDAO();
        IAuthDAO authDAO = new AuthDynamoDAO();
        User user = request.getUser();
        UserDTO userDTO = null;

        /* Check if user is already registered */
        try {
            userDTO = userDAO.getUserItem(user.getAlias(), user.getFirstName());
        }
        catch (Exception e) {
            System.err.printf("ERROR: %s\n", e.getMessage());
            return new RegisterResponse(false, e.getMessage(), user, null);
        }

        if (userDTO != null && userDTO.getUser().getAlias().equals(user.getAlias())) {
            return new RegisterResponse(false, "Alias already taken, choose another", user, null);
        }

        /* Put the item to the user table */
        RegisterResponse response = null;
        try {
            /* Upload photo to s3 */
            String imageS3FilePath = makeS3FilePath(user.getAlias(), user.getImageUrl());
            AbstractS3AO s3ao = new S3AO();
            s3ao.uploadPhoto(user.getAlias(), imageS3FilePath, new URL(user.getImageUrl()));
            //uploadPhotoToS3(user.getAlias(), user.getImageUrl());

            /* Create salt */
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[32];
            random.nextBytes(salt);

            /* Hash the password using PBKDF2 algorithm */
            KeySpec keySpec = new PBEKeySpec(request.getPassword().toCharArray(), salt, 10000, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hashedPswd = factory.generateSecret(keySpec).getEncoded();

            AuthToken token = new AuthToken(user.getAlias());
            authDAO.putAuthToken(token);
            //BatchWriteItemResult result = writeToTables(dynamoDB, user, token, salt, hashedPswd);
            userDAO.putUser(user, imageS3FilePath, hashedPswd, salt);
            //System.out.println("BatchWriteItem succeeded:\n" + result);
            response = new RegisterResponse(true, null, user, token);
        }
        catch (Exception e) {
            System.out.printf("Error: %s\n", e.getMessage());
            response = new RegisterResponse(false, e.getMessage(), user, null);
        }

        return response;
    }

    private String makeS3FilePath(String alias, String urlStr) throws MalformedURLException {
        URL url = new URL(urlStr);
        String extension = url.getFile().substring(url.getFile().lastIndexOf('.'));
        return "profile-pics/" + alias + extension;
    }

}
