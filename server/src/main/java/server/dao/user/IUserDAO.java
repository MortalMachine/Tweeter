package server.dao.user;

import model.domain.User;
import server.dto.UserDTO;

public interface IUserDAO {
    UserDTO getUserItem(String alias);
    UserDTO getUserItem(String alias, String firstname);
    void putUser(User user, String imageUrl, byte[] password, byte[] salt);
}
