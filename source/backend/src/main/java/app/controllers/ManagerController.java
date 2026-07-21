package app.controllers;

import app.dto.CheckTokenValidationRequest;
import app.dto.manager.*;
import app.dto.user.RemoveAdRequset;
import app.entities.Advertisement;
import app.services.AdvertisementService;
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
    @PostMapping("/add-city")
    public ResponseEntity<String> addCity(@RequestBody AddCityRequest addCityRequest) {
        try{
            ManagerService.addCity(addCityRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (e.getMessage().equals("token expired")||e.getMessage().equals("this username does not match")||e.getMessage().equals("token not found")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/add-product-category")
    public  ResponseEntity<String> addProductCategory(@RequestBody AddCategoryRequest addCategoryRequest){
        try{
            ManagerService.addProductCategory(addCategoryRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (e.getMessage().equals("token expired")||e.getMessage().equals("this username does not match")||e.getMessage().equals("token not found")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/get-pending-ads")
    public ResponseEntity<ArrayList<Advertisement>> getPendingAds(@RequestBody GetPendingAdsRequest getPendingAdsRequest) {
        try{
            ArrayList<Advertisement>res=AdvertisementService.getPendingAdvertisements(getPendingAdsRequest);
            return ResponseEntity.ok(res);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/change-ad-status")
    public ResponseEntity<String> changeAdStatus(@RequestBody ChangeAdStatusRequest changeAdStatusRequest) {
        try{
            AdvertisementService.changeAdStatus(changeAdStatusRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/remove-ad")
    public ResponseEntity<String> removeAd(@RequestBody RemoveAdRequset removeAdRequset){
        try{
            AdvertisementService.removeAdvertisement(removeAdRequset);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
