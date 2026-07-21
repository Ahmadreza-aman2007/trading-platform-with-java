package app.services;

import app.dto.login.LoginRequest;
import app.dto.register.RegisterRequest;
import app.entities.token.Token;
import app.entities.users.*;
import app.entities.users.enums.UserRole;
import app.repository.DAOs.TokenDAO;
import app.repository.DAOs.UserDAO;
import app.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private static TokenUtil tokenUtil=new TokenUtil();
    public static void registerUser(RegisterRequest request) throws Exception {
        // چک طول رمز سمت سرور هم انجام می شود که قابل دور زدن نباشد
        if (request.getPassword() == null || request.getPassword().length() <= 6) {
            throw new Exception("رمز عبور باید بیشتر از 6 کاراکتر باشد");
        }
        if (UserDAO.isUsernameExist(request.getUsername())) {
            throw new Exception("این نام کاربری قبلا ثبت شده است");
        }
        if (UserDAO.isPhoneNumberExist(request.getPhoneNumber())) {
            throw new Exception("این شماره تلفن قبلا ثبت شده است");
        }
        User user = new User(request.getUsername(), request.getPassword(), request.getPhoneNumber(), UserRole.COMMON_USER,
                request.getFullname());
        UserDAO.saveUser(user);
    }

    public  static String login(LoginRequest loginRequest) throws Exception {
        if (!UserDAO.isUsernameExist(loginRequest.getUsername())) {
            throw new Exception("user notFound");
        }
        if (!UserDAO.checkPasswordByUsername(loginRequest.getUsername(), loginRequest.getPassword())) {
            throw new Exception("wrong pass");
        }
        TokenDAO.deleteAllExpireTokens();
        User user=UserDAO.loadUserByUsername(loginRequest.getUsername());
        if (user.isBlocked()){
            throw new Exception("user is blocked");
        }
        String t=tokenUtil.generateToken(user.getUsername(),user.getUserRole().name());
        LocalDateTime expiresAt=LocalDateTime.now().plusSeconds(tokenUtil.getExpiration()/1000);
        Token newToken=new Token(t,user.getUsername(),LocalDateTime.now(),expiresAt,false);
        TokenDAO.saveToken(newToken);
        return t;
    }
}
