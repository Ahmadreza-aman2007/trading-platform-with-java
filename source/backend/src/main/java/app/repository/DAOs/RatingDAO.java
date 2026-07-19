package app.repository.DAOs;

import app.entities.Rating;
import app.entities.users.User;
import app.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO {
    public static void save(Rating rating) throws Exception {
        if(isRatingExist(rating.getRaterId(),rating.getAdId())){
            throw new Exception("Rating already exists");
        }
        String query ="INSERT INTO ratings(rater_id,ad_id,seller_id,created_at,score,comment) VALUES(?,?,?,?,?,?)";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1,rating.getRaterId());
            preparedStatement.setLong(2,rating.getAdId());
            preparedStatement.setLong(3,rating.getSellerId());
            preparedStatement.setObject(4,LocalDateTime.now());
            preparedStatement.setInt(5,rating.getScore());
            preparedStatement.setString(6,rating.getComment());
            int e=preparedStatement.executeUpdate();
            if(e==0){
                throw new Exception("error in method save");
            }
        }
    }
    public static boolean isRatingExist(Long raterId ,Long ad_id) throws Exception {
        if (raterId == null||ad_id == null){
            throw new Exception("raterId or adId is null");
        }
        String query="SELECT id FROM ratings WHERE rater_id=? AND ad_id=?";
        try(Connection c= DatabaseConnection.getConnection();
            PreparedStatement ps=c.prepareStatement(query)){
            ps.setLong(1,raterId);
            ps.setLong(2,ad_id);
            ResultSet rs=ps.executeQuery();
            return rs.next();
        }

    }
    public static List<Rating> findByAdId(Long adId) throws Exception {
        String query = "SELECT * FROM ratings WHERE ad_id = ? ORDER BY created_at DESC";
        List<Rating> ratings = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();

             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, adId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Rating r = new Rating();
                r.setId(rs.getLong("id"));
                r.setRaterId(rs.getLong("rater_id"));
                r.setSellerId(rs.getLong("seller_id"));
                r.setAdId(rs.getLong("ad_id"));
                r.setScore(rs.getInt("score"));
                r.setComment(rs.getString("comment"));
                r.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                ratings.add(r);
            }
        }
        return ratings;
    }public static double getAverageScore(Long sellerId) throws Exception {
        String query = "SELECT AVG(score) as avg FROM ratings WHERE seller_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("avg");
            }
            return 0.0;
        }
    }
    public static int getCount(Long sellerId) throws Exception {
        String query = "SELECT COUNT(*) as count FROM ratings WHERE seller_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        }
    }

    public static List<Rating> findBySellerUsername(String sellerUsername,Long adId) throws Exception {
        User u=UserDAO.loadUserByUsername(sellerUsername);
        System.out.println("User");
        if (u==null){
            throw new Exception("user not found");
        }
        System.out.println(u.getId());
        String sql = "SELECT * FROM ratings WHERE id = ? AND adId=?  ORDER BY created_at DESC";
        List<Rating> ratings = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, u.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Rating r = new Rating();
                r.setId(rs.getLong("id"));
                r.setRaterId(rs.getLong("rater_id"));
                r.setSellerId(rs.getLong("seller_id"));
                r.setAdId(rs.getLong("ad_id"));
                r.setScore(rs.getInt("score"));
                r.setComment(rs.getString("comment"));
                r.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                ratings.add(r);
            }

        } catch (Exception e) {
            System.err.println("❌ خطا در findBySellerUsername: " + e.getMessage());
        }
        return ratings;
    }


}

