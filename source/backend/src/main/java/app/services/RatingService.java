package app.services;

import app.dto.user.AddRatingRequest;
import app.entities.Rating;
import app.entities.users.enums.UserRole;
import app.repository.DAOs.RatingDAO;
import app.utils.TokenUtil;

public class RatingService {

    public static void addRating(AddRatingRequest addRatingRequest)throws Exception{
        TokenUtil.isTokenValid(addRatingRequest.getUsername(), addRatingRequest.getToken(), UserRole.COMMON_USER);
        RatingDAO.save(new Rating(addRatingRequest.getRaterId(),addRatingRequest.getSellerId(),addRatingRequest.getAdId(),addRatingRequest.getScore(),addRatingRequest.getComment()));
    }
}
