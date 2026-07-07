package app.controllers;

import app.dto.login.LoginRequest;
import app.dto.login.LoginResponse;
import app.dto.register.RegisterRequest;
import app.services.AuthService;
import app.utils.Token;
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
    private Token token;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = AuthService.login(loginRequest);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
            }
            String sToken = token.generateToken(user.getUsername(), user.getUserRole().name());
            LoginResponse loginResponse = new LoginResponse(sToken, user.getUsername(), user.getFullname(),
                    user.getUserRole().name(), user.getPhoneNumber());
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
