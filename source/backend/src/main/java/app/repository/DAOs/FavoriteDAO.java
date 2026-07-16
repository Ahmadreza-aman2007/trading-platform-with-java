package app.repository.DAOs;

import app.entities.Favorite;
import app.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FavoriteDAO {
    public static void save(Favorite favorite) throws Exception{
    if (isFavoriteExists(favorite.getUserId(),favorite.getAdId())) {
        throw new Exception("User already exists");
    }
    String query = "INSERT INTO favorite (user_id, ad_id) VALUES (?, ?)";
    try(Connection connection = DatabaseConnection.getConnection();
    PreparedStatement preparedStatement = connection.prepareStatement(query)){
        preparedStatement.setLong(1, favorite.getUserId());
        preparedStatement.setLong(2, favorite.getAdId());
        int e= preparedStatement.executeUpdate();
        if(e==0){
            throw new Exception("error in method save");
        }
    }
    }
    public static void delete(Long id) throws Exception{
        findById(id);
        String query = "DELETE FROM favorite WHERE id=?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1, id);
            int e= preparedStatement.executeUpdate();
            if(e==0){
                throw new Exception("error in method delete");
            }
        }
    }
    public static boolean isFavoriteExists(Long userId,Long adId) throws Exception{
        String query = "SELECT id FROM favorites WHERE user_id = ? AND ad_id = ?";
        try(Connection c= DatabaseConnection.getConnection();
            PreparedStatement s=c.prepareStatement(query)){
            s.setLong(1, userId);
            s.setLong(2, adId);
            ResultSet rs=s.executeQuery();
            return rs.next();
        }
    }
    public static Favorite findById(Long id) throws Exception{
        String query = "SELECT * FROM favorites WHERE id = ?";
        try(Connection c= DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(query)){
            s.setLong(1, id);
            ResultSet rs=s.executeQuery();
            if(rs.next()){
                Favorite favorite=new Favorite();
                favorite.setId(rs.getLong("id"));
                favorite.setUserId(rs.getLong("user_id"));
                favorite.setAdId(rs.getLong("ad_id"));
                return favorite;
            }
            throw new Exception("user not found");
        }
    }
}
