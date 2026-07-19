package app.controllers;

import app.entities.Advertisement;
import app.entities.categories.City;
import app.entities.categories.ProductCategory;
import app.repository.DAOs.AdvertisementDAO;
import app.repository.DAOs.UserDAO;
import app.services.AdvertisementService;
import app.services.CategoryService;
import app.services.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/public")
public class SharedController {
    @PostMapping("/get-categories")
    public ResponseEntity<ArrayList<ProductCategory>> getCategories(){
        try{
            ArrayList<ProductCategory> categories = CategoryService.getCategories();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/get-cities")
    public ResponseEntity<ArrayList<City>> getCities(){
        try{
            ArrayList<City> cities = CityService.getCities();
            return new ResponseEntity<>(cities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/get-approved-ads")
    public ResponseEntity<ArrayList<Advertisement>> getApprovedAds(){
        try{
            ArrayList<Advertisement> ads = AdvertisementService.getApprovedAds();
            return new ResponseEntity<>(ads, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/by-username/{username}")
    public ResponseEntity<Long> getUserIdByUsername(@PathVariable String username) {
        try {
            Long userId = UserDAO.findIdByUsername(username);
            return ResponseEntity.ok(userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
