package server.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import model.domain.AuthToken;
import model.domain.User;
import model.service.LoginService;
import request.LoginRequest;
import response.LoginResponse;
import server.dao.auth.AuthDynamoDAO;
import server.dao.auth.IAuthDAO;
import server.dao.user.IUserDAO;
import server.dao.user.UserDynamoDAO;
import server.dto.UserDTO;
import server.s3.AbstractS3AO;
import server.s3.S3AO;

public class LoginServiceImpl implements LoginService {
    @Override
    public LoginResponse login(LoginRequest request) {
        User user = request.getUser();
        IUserDAO userDAO = new UserDynamoDAO();
        IAuthDAO authDAO = new AuthDynamoDAO();
        AbstractS3AO s3AO = new S3AO();
        UserDTO userDTO = null;

        try {
            userDTO = userDAO.getUserItem(user.getAlias());
            boolean isPswdEqual = false;

            if (userDTO != null) {
                isPswdEqual = isEqualPasswords(userDTO, request.getPassword());
            }

            if (userDTO != null
                    && userDTO.getUser().getAlias().equals(user.getAlias())
                    && isPswdEqual) {
                User userFromUserTable = userDTO.getUser();
                String imageUrl = s3AO.getProfilePicFromS3(userFromUserTable.getImageUrl());
                String firstname = userFromUserTable.getFirstName();
                String lastname = userFromUserTable.getLastName();
                String alias = userFromUserTable.getAlias();
                User respUser = new User(firstname, lastname, alias, imageUrl);
                AuthToken token = new AuthToken(alias);
                authDAO.putAuthToken(token);
                return new LoginResponse(true, null, respUser, token);
            } else {
                return new LoginResponse(false, "Alias/Password incorrect", user, null);
            }

        } catch (IOException e) {
            return new LoginResponse(false, e.getMessage(), user, null);
        }
        catch (Exception e) {
            System.err.printf("Unable to read item: %s\n", user.getAlias());
            System.err.println(e.getMessage());
            return new LoginResponse(false, e.getMessage(), user, null);
        }
    }

    private boolean isEqualPasswords(UserDTO userDTO, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = userDTO.getSalt();
        byte[] qPswd = userDTO.getPassword();

        /* Hash the password using PBKDF2 algorithm */
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashedPswd = factory.generateSecret(keySpec).getEncoded();

        if (Arrays.equals(qPswd, hashedPswd)) {
            System.out.println("Passwords equal!");
            System.out.println(Arrays.toString(qPswd));
            System.out.println(Arrays.toString(hashedPswd));
            return true;
        }
        else {
            System.out.println("Passwords not equal!");
            System.out.println(Arrays.toString(qPswd));
            System.out.println(Arrays.toString(hashedPswd));
            return false;
        }
    }

}
