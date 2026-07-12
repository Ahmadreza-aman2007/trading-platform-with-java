package app.services;

import app.dto.manager.GetUserResponse;
import app.entities.users.User;
import app.entities.users.enums.UserRole;
import app.repository.UserDAO;
import app.utils.TokenUtil;

import java.util.ArrayList;

public class ManagerService {
    public static ArrayList<GetUserResponse> getUsers(String username, String token) throws Exception {
        TokenUtil.isTokenValid(username,token);
        ArrayList<User> users=UserDAO.getAllUsers();
        ArrayList<GetUserResponse> result=new ArrayList<GetUserResponse>();
        if(users.isEmpty()){
            throw new Exception("there are no users in the system");
        }
        for(User user:users){
            if(!user.getUserRole().equals(UserRole.MANAGER)){
                result.add(new GetUserResponse(user.getId(),user.getUsername(),user.getPhoneNumber(),user.getFullname(),user.isBlocked(),user.getCreatedDate()));
            }
        }
        return result;
    }
    public static void editUser(String username, String token, GetUserResponse user) throws Exception {
        TokenUtil.isTokenValid(username,token);
        UserDAO.loadUserById(user);
    }
}
