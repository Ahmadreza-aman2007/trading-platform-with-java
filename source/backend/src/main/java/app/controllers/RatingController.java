package app.controllers;

import app.dto.user.AddRatingRequest;
import app.dto.user.RatingResponse;
import app.entities.users.User;
import app.repository.DAOs.UserDAO;
import app.services.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @PostMapping("/add")
    public ResponseEntity<String> addRating(@RequestBody AddRatingRequest request) {
        try {
            RatingService.addRating(request);
            return ResponseEntity.ok("✅ امتیاز با موفقیت ثبت شد");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ " + e.getMessage());
        }
    }

    @GetMapping("/seller/{sellerUsername}/count")
    public ResponseEntity<String> getRatingsCount(@PathVariable String sellerUsername)  {
        try{
            User u= UserDAO.loadUserByUsername(sellerUsername);
            if(u==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            int x=RatingService.getRatingCount(u.getId());
            return ResponseEntity.ok(x+"");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/seller/{sellerUsername}/average")
    public ResponseEntity<String> getAverageRatingsByUsername(@PathVariable String sellerUsername) {
        try{
            User u= UserDAO.loadUserByUsername(sellerUsername);
            if(u==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            double x=RatingService.getAverageScore(u.getId());
            return ResponseEntity.ok(x+"");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/seller/get-all-ratings/seller-username/{sellerUsername}/ad-id/{adId}")
    public ResponseEntity<List<RatingResponse>> getAllRatingsByUsername(@PathVariable String sellerUsername,@PathVariable String adId) {
        try{
            User u= UserDAO.loadUserByUsername(sellerUsername);
            if(u==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(RatingService.getSellerRatings(u.getId(),Long.parseLong(adId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}