package app.services;

import app.dto.user.AddFavoriteRequest;
import app.dto.user.GetFavoritesRequest;
import app.dto.user.RemoveFavoriteRequest;
import app.entities.Advertisement;
import app.entities.Favorite;
import app.entities.users.enums.UserRole;
import app.repository.DAOs.AdvertisementDAO;
import app.repository.DAOs.FavoriteDAO;
import app.repository.DAOs.UserDAO;
import app.utils.TokenUtil;

import java.util.ArrayList;

public class FavoriteService {
    public static void addFavorite(AddFavoriteRequest addFavoriteRequest) throws Exception {
        TokenUtil.isTokenValid(addFavoriteRequest.getUsername(),addFavoriteRequest.getToken(), UserRole.COMMON_USER);
        Favorite favorite = new Favorite(addFavoriteRequest.getId(), addFavoriteRequest.getAddId());
        FavoriteDAO.save(favorite);
    }

    public static ArrayList<Advertisement> getFavorites(GetFavoritesRequest getFavoritesRequest) throws Exception {
        TokenUtil.isTokenValid(getFavoritesRequest.getUsername(),getFavoritesRequest.getToken(), UserRole.COMMON_USER);
        ArrayList<Advertisement> res = new ArrayList<>();
        ArrayList<Favorite> favorites=FavoriteDAO.getFavorites(getFavoritesRequest.getUserId());
        for(Favorite favorite:favorites){
            try {
                Advertisement ad=AdvertisementDAO.getAdvertisementById(favorite.getAdId());
                res.add(ad);
            } catch (Exception ignored) {
                // آگهی حذف شده است؛ از لیست علاقه‌مندی صرف نظر می‌شود
            }
        }
        return res;
    }
    public static void removeFavorite(RemoveFavoriteRequest removeFavoriteRequest) throws Exception {
        TokenUtil.isTokenValid(removeFavoriteRequest.getUsername(),removeFavoriteRequest.getToken(), UserRole.COMMON_USER);
        Long userId = UserDAO.findIdByUsername(removeFavoriteRequest.getUsername());
        FavoriteDAO.deleteByUserAndAd(userId, removeFavoriteRequest.getAdId());
    }
}
