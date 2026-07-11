package app.services;

import app.dto.manager.GetUsersResponse;
import app.entities.users.User;
import app.entities.users.enums.UserRole;
import app.repository.UserDAO;
import app.utils.TokenUtil;

import java.util.ArrayList;

public class ManagerService {
    public static ArrayList<GetUsersResponse> getUsers(String username,String token) throws Exception {
        TokenUtil.isTokenValid(username,token);
        ArrayList<User> users=UserDAO.getAllUsers();
        ArrayList<GetUsersResponse> result=new ArrayList<GetUsersResponse>();
        if(users.isEmpty()){
            throw new Exception("there are no users in the system");
        }
        for(User user:users){
            if(!user.getUserRole().equals(UserRole.MANAGER)){
                result.add(new GetUsersResponse(user.getId(),user.getUsername(),user.getPhoneNumber(),user.getFullname(),user.isBlocked(),user.getCreatedDate()));
            }
        }
        return result;
    }
}
