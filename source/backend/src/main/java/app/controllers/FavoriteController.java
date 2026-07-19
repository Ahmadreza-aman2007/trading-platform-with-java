package app.controllers;

import app.dto.user.AddFavoriteRequest;
import app.dto.user.GetFavoritesRequest;
import app.dto.user.RemoveFavoriteRequest;
import app.entities.Advertisement;
import app.entities.users.User;
import app.entities.users.enums.UserRole;
import app.repository.DAOs.FavoriteDAO;
import app.repository.DAOs.UserDAO;
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
@RequestMapping("/api/favorites")
public class FavoriteController {

    @PostMapping("/add")
    public ResponseEntity<String> addFavorite(@RequestBody AddFavoriteRequest request) {
        try {
            FavoriteService.addFavorite(request);
            return ResponseEntity.ok("✅ آگهی به علاقه‌مندی‌ها اضافه شد");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ " + e.getMessage());
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFavorite(@RequestBody RemoveFavoriteRequest request) {
        try {
            FavoriteService.removeFavorite(request);
            return ResponseEntity.ok("✅ آگهی از علاقه‌مندی‌ها حذف شد");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ " + e.getMessage());
        }
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> checkFavorite(@RequestBody GetFavoritesRequest request) {
        try {
            TokenUtil.isTokenValid(request.getUsername(),request.getToken(), UserRole.COMMON_USER);
            User u= UserDAO.loadUserByUsername(request.getUsername());
            if (u==null) {
                return ResponseEntity.badRequest().body(false);
            }
            boolean isFavorite = FavoriteDAO.isFavoriteExists(
                    u.getId(),
                    request.getUserId()
            );
            return ResponseEntity.ok(isFavorite);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PostMapping("/list")
    public ResponseEntity<ArrayList<Advertisement>> getFavorites(@RequestBody GetFavoritesRequest request) {
        try {
            ArrayList<Advertisement> ads = FavoriteService.getFavorites(request);
            return ResponseEntity.ok(ads);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}