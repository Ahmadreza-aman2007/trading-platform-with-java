package app.repository.DAOs;

import app.entities.token.Token;
import app.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static app.repository.DAOs.UserDAO.isUsernameExist;


public class TokenDAO {

    public static int saveToken(Token token){
        if(isTokenExist(token.getToken())){
            return -1;
        }
        String sqlQuery="INSERT INTO tokens(token,username,expires_at,created_at,is_revoked) VALUES(?,?,?,?,?)";
        try(Connection c= DatabaseConnection.getConnection();
            PreparedStatement s=c.prepareStatement(sqlQuery)){
            s.setString(1,token.getToken());
            s.setString(2,token.getUsername());
            s.setString(3,token.getExpiresAt().toString());
            s.setString(4,token.getCreatedAt().toString());
            s.setInt(5, token.isRevoked() ?1:0);
            int e=s.executeUpdate();
            if (e==0){
                return 0;
            }
            return 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean revokeToken(String token){
        Token t=findByToken(token);
        if(t==null){
            return false;
        }
        String sqlQuery="UPDATE tokens SET is_revoked = 1 WHERE token = ?" ;
        try(Connection c= DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(sqlQuery)){
            s.setString(1,token);
            int e = s.executeUpdate();
            return e!=0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean revokeAllTokensForAUser(String username){
        if (!isUsernameExist(username)){
            return false;
        }
        String sqlQuery="UPDATE tokens SET is_revoked = 1 WHERE username=?";
        try(Connection c= DatabaseConnection.getConnection();PreparedStatement s=c.prepareStatement(sqlQuery)) {
            s.setString(1,username);
            int e=s.executeUpdate();
            return e!=0;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static boolean deleteAllExpireTokens(){
        String sqlQuery="DELETE FROM tokens WHERE is_revoked=1";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(sqlQuery)){
            int e=s.executeUpdate();
            return e>0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isTokenExist(String token){
        String sqlQuery="SELECT token FROM tokens WHERE token = ?";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(sqlQuery)) {
            s.setString(1,token);
            ResultSet res=s.executeQuery();
            return res.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Token findByToken(String token){
        if (!isTokenExist(token)){
            return null;
        }
        String sqlQuery="SELECT * FROM tokens WHERE token = ?";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(sqlQuery)){
            s.setString(1,token);
            ResultSet res=s.executeQuery();
            if (res.next()){
                return new Token(res.getString("token"),res.getString("username"), LocalDateTime.parse(res.getString("created_at")),LocalDateTime.parse(res.getString("expires_at")),res.getInt("is_revoked")==1);
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
