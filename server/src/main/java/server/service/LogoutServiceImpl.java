package server.service;

import model.service.LogoutService;
import request.LogoutRequest;
import response.LogoutResponse;
import server.dao.auth.AuthDynamoDAO;
import server.dao.auth.IAuthDAO;

public class LogoutServiceImpl implements LogoutService {
    @Override
    public LogoutResponse logout(LogoutRequest request) {
        IAuthDAO authDAO = new AuthDynamoDAO();
        try {
            authDAO.deleteAuthToken(request.getUser().getAlias());
            return new LogoutResponse(true, null);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return new LogoutResponse(false, e.getMessage());
        }
    }
}
