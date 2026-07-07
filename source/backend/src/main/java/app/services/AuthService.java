package app.services;

import app.dto.login.LoginRequest;
import app.dto.register.RegisterRequest;
import app.entities.token.Token;
import app.entities.users.*;
import app.repository.TokenDAO;
import app.repository.UserDAO;
import app.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private static TokenUtil tokenUtil=new TokenUtil();
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

    public  static String login(LoginRequest loginRequest) throws Exception {
        if (!UserDAO.isUsernameExist(loginRequest.getUsername())) {
            throw new Exception("user notFound");
        }
        if (!UserDAO.checkPasswordByUsername(loginRequest.getUsername(), loginRequest.getPassword())) {
            throw new Exception("wrong pass");
        }
        TokenDAO.deleteAllExpireTokens();
        User user=UserDAO.loadUserByUsername(loginRequest.getUsername());
        String t=tokenUtil.generateToken(user.getUsername(),user.getUserRole().name());
        LocalDateTime expiresAt=LocalDateTime.now().plusSeconds(tokenUtil.getExpiration()/1000);
        Token newToken=new Token(t,user.getUsername(),LocalDateTime.now(),expiresAt,false);
        TokenDAO.saveToken(newToken);
        return t;
    }
}
