package app.controllers;

import app.dto.user.*;
import app.entities.Advertisement;
import app.entities.users.enums.UserRole;
import app.repository.DAOs.AdvertisementDAO;
import app.services.RatingService;
import app.services.AdvertisementService;
import app.services.FavoriteService;
import app.utils.TokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @PostMapping("/add-ad")
    public ResponseEntity<String> addAd(@RequestBody AddAdRequest addAdRequest){
        try{
            AdvertisementService.addAdvertisement(addAdRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/edit-ad")
    public ResponseEntity<String> editAd(@RequestBody EditAdRequest editAdRequest){
        try{
            AdvertisementService.editAdvertisement(editAdRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/remove-ad")
    public  ResponseEntity<String> removeAdd(@RequestBody RemoveAdRequset removeAdRequset){
        try{
            AdvertisementService.removeAdvertisement(removeAdRequset);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/advanced-search")
    public ResponseEntity<?> advancedSearch(@RequestBody CustomSearchRequest customSearchRequest){
        System.out.println(customSearchRequest.getCity());
        System.out.println(customSearchRequest.getCategory());
        System.out.println(customSearchRequest.getKeyword());
        System.out.println(customSearchRequest.getPriceCeiling());
        System.out.println(customSearchRequest.getPriceFloor());
        try{
            ArrayList<Advertisement> res=AdvertisementDAO.advancedSearch(customSearchRequest);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/add-favorite")
    public ResponseEntity<String> addFavorite(@RequestBody AddFavoriteRequest addFavoriteRequest){
        try{
            FavoriteService.addFavorite(addFavoriteRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/get-favorites")
    public ResponseEntity<?> getFavorites(@RequestBody GetFavoritesRequest getFavoritesRequest){
        try{
            ArrayList<Advertisement> res= FavoriteService.getFavorites(getFavoritesRequest);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/remove-favorite")
    public ResponseEntity<String> removeFavorite(@RequestBody RemoveFavoriteRequest removeFavoriteRequest){
        try{
            FavoriteService.removeFavorite(removeFavoriteRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/add-rating")
    public ResponseEntity<String> addRating(@RequestBody AddRatingRequest addRatingRequest){
        try{
            RatingService.addRating(addRatingRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/my-ads")
    public ResponseEntity<ArrayList<Advertisement>> getMyAds(@RequestBody GetMyAdsRequest request) {
        try {
            TokenUtil.isTokenValid(request.getUsername(), request.getToken(), UserRole.COMMON_USER);
            ArrayList<Advertisement> ads = AdvertisementDAO.getAdsByUsername(request.getUsername());
            return ResponseEntity.ok(ads);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
