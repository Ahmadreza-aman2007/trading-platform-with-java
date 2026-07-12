package app.services;

import app.dto.manager.EditUserStatusRequest;
import app.dto.manager.GetUserResponse;
import app.entities.users.User;
import app.entities.users.enums.UserRole;
import app.repository.UserDAO;
import app.utils.TokenUtil;

import java.util.ArrayList;

public class ManagerService {
    public static ArrayList<GetUserResponse> getUsers(String username, String token) throws Exception {
        TokenUtil.isTokenValid(username, token);
        ArrayList<User> users = UserDAO.getAllUsers();
        ArrayList<GetUserResponse> result = new ArrayList<GetUserResponse>();
        if (users.isEmpty()) {
            throw new Exception("there are no users in the system");
        }
        for (User user : users) {
            if (!user.getUserRole().equals(UserRole.MANAGER)) {
                result.add(new GetUserResponse(user.getId(), user.getUsername(), user.getPhoneNumber(),
                        user.getFullname(), user.isBlocked(), user.getCreatedDate()));
            }
        }
        return result;
    }

    public static void editUser(EditUserStatusRequest editUserStatusRequest) throws Exception {
        TokenUtil.isTokenValid(editUserStatusRequest.getSenderUsername(), editUserStatusRequest.getToken());
        User u = UserDAO.loadUserById(editUserStatusRequest.getId());
        u.setId(editUserStatusRequest.getId());
        u.setBlocked(editUserStatusRequest.isBlocked());
        UserDAO.editUser(u);
    }
}
