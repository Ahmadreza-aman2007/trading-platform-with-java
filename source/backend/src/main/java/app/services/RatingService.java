package app.services;

import app.dto.user.AddRatingRequest;
import app.dto.user.RatingResponse;
import app.entities.Rating;
import app.entities.users.enums.UserRole;
import app.repository.DAOs.RatingDAO;
import app.repository.DAOs.UserDAO;
import app.utils.TokenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RatingService {

    public static void addRating(AddRatingRequest request) throws Exception {
        TokenUtil.isTokenValid(request.getUsername(), request.getToken(), UserRole.COMMON_USER);

        if (request.getRaterId() != null && request.getRaterId().equals(request.getSellerId())) {
            throw new Exception("نمی‌توانید به خودتان امتیاز دهید");
        }

        if (RatingDAO.isRatingExist(request.getRaterId(), request.getAdId())) {
            throw new Exception("شما قبلاً به این آگهی امتیاز داده‌اید");
        }

        if (request.getScore() < 1 || request.getScore() > 5) {
            throw new Exception("امتیاز باید بین ۱ تا ۵ باشد");
        }

        Rating rating = new Rating(
                request.getRaterId(),
                request.getSellerId(),
                request.getAdId(),
                request.getScore(),
                request.getComment()
        );

        RatingDAO.save(rating);
    }
    public static List<RatingResponse> getSellerRatings(Long sellerId,Long adId) throws Exception {
        List<Rating> ratings = RatingDAO.findByAdId(adId);
        List<RatingResponse> responses = new ArrayList<>();
        String username= Objects.requireNonNull(UserDAO.loadUserById(sellerId)).getUsername();
        for (Rating r : ratings) {
            RatingResponse resp = new RatingResponse(
                    r.getId(),
                    r.getRaterId(),
                    r.getSellerId(),
                    r.getAdId(),
                    r.getScore(),
                    r.getComment(),
                    username,
                    r.getCreatedAt().toString()
            );
            responses.add(resp);
        }
        return responses;
    }
    public static double getAverageScore(Long sellerId) throws Exception {
        return RatingDAO.getAverageScore(sellerId);
    }


    public static int getRatingCount(Long sellerId) throws Exception {
        return RatingDAO.getCount(sellerId);
    }
}

