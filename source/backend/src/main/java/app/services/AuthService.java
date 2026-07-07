package app.services;

import app.dto.login.LoginRequest;
import app.dto.register.RegisterRequest;
import app.entities.users.*;
import app.repository.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public static void registerUser(RegisterRequest request) throws Exception {
        if (UserDAO.isUsernameExist(request.getUsername())) {
            throw new Exception("این نام کاربری قبلا ثبت شده است");
        }
        if (UserDAO.isPhoneNumberExist(request.getPhoneNumber())) {
            throw new Exception("این شماره تلفن قبلا ثبت شده است");
        }
        User user = new CommonUser(request.getUsername(), request.getPassword(), request.getPhoneNumber(),
                request.getFullname());
        UserDAO.saveUser(user);
    }

    public static User login(LoginRequest loginRequest) {
        try {
            if (!UserDAO.isUsernameExist(loginRequest.getUsername())) {
                throw new Exception("user notFound");
            }
            if (!UserDAO.checkPasswordByUsername(loginRequest.getUsername(), loginRequest.getPassword())) {
                throw new Exception("wrong pass");
            }
            return UserDAO.loadUserByUsername(loginRequest.getUsername());
        } catch (Exception e) {
            System.err.println("error in login " + e.getMessage());
            return null;
        }
    }
}
