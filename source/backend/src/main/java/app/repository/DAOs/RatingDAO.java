package app.repository.DAOs;

import app.entities.Rating;
import app.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
            preparedStatement.setObject(4,rating.getCreatedAt());
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
}
