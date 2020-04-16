package server.dto;

import model.domain.User;

public class UserDTO {
    private User user;
    private byte[] password;
    private byte[] salt;
    public UserDTO(User user, byte[] password, byte[] salt) {
        this.user = user;
        this.password = password;
        this.salt = salt;
    }
    public User getUser() {
        return user;
    }
    public byte[] getPassword() {
        return password;
    }
    public byte[] getSalt() {
        return salt;
    }
}
