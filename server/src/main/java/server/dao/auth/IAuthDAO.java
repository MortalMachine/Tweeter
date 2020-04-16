package server.dao.auth;

import model.domain.AuthToken;

public interface IAuthDAO {
    Object getAuthToken(String alias);
    void putAuthToken(AuthToken token);
    void deleteAuthToken(String alias);
    boolean isTokenValid(AuthToken token);
}
