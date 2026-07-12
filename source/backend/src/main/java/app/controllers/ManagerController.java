package app.controllers;

import app.dto.CheckTokenValidationRequest;
import app.dto.manager.EditUserStatusRequest;
import app.dto.manager.GetUserResponse;
import app.services.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    @PostMapping("/get-all-users")
    public ResponseEntity<ArrayList<GetUserResponse>> getAllUsers(
            @RequestBody CheckTokenValidationRequest checkTokenValidationRequest) {
        try {
            ArrayList<GetUserResponse> users = ManagerService.getUsers(checkTokenValidationRequest.getUsername(),
                    checkTokenValidationRequest.getToken());
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return switch (e.getMessage()) {
                case "token not found" -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
                case "this username does not match", "token expired" -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                default -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            };
        }
    }

    @PostMapping("/editUser")
    public ResponseEntity<String> editUser(@RequestBody EditUserStatusRequest editUserStatusRequest) {
        try {
            ManagerService.editUser(editUserStatusRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            String message = e.getMessage();
            // switch (message) {
            //
            // }
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
