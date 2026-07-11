package app.controllers;

import app.dto.manager.GetUsersRequest;
import app.dto.manager.GetUsersResponse;
import app.entities.users.User;
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
    @PostMapping("get-all-users")
    public ResponseEntity<ArrayList<GetUsersResponse>> getAllUsers(@RequestBody GetUsersRequest getUsersRequest) {
    try{
        ArrayList<GetUsersResponse>users=ManagerService.getUsers(getUsersRequest.getUsername(),getUsersRequest.getToken());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }catch(Exception e){
        System.out.println(e.getMessage());
        return switch (e.getMessage()) {
            case "token not found" -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
            case "this username does not match", "token expired" -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            default ->
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }
    }

}
