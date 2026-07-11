package app.controllers;

import app.dto.login.LoginRequest;
import app.dto.login.LoginResponse;
import app.dto.register.RegisterRequest;
import app.repository.UserDAO;
import app.services.AuthService;
import app.utils.TokenUtil;
import app.entities.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("done");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @Autowired
    private static TokenUtil token;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String res=AuthService.login(loginRequest);
            User user=  UserDAO.loadUserByUsername(loginRequest.getUsername());
            LoginResponse loginResponse=new LoginResponse(res, user.getUsername(),user.getFullname(),user.getUserRole().name(), user.getPhoneNumber());
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            String message=e.getMessage();
            if(message.equals("user notFound")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("کاربر یافت نشد");
            } else if (message.equals("wrong pass")) {
                return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("رمز عبور اشتباه است");
            } else if (message.equals("user is blocked")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("کاربر مسدود است");
            }
            else {
                System.out.println(e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("خطای سرور :"+message);}
        }
    }
}
