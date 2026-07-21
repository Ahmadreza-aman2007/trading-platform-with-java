package app.repository.DAOs;

import app.entities.conversation.Conversation;
import app.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ConversationDAO {
    public static void save(Conversation conversation) throws Exception {
        try{
            if (isConversationExist(conversation.getSellerId(), conversation.getAdId(), conversation.getBuyerId())) {
                throw new RuntimeException("this conversation already exists");
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        String query="INSERT INTO conversations(seller_id,buyer_id,ad_id,is_blocked) VALUES(?,?,?,?)";
        try(Connection c= DatabaseConnection.getConnection();
            PreparedStatement s=c.prepareStatement(query)){
            s.setLong(1, conversation.getSellerId());
            s.setLong(2, conversation.getBuyerId());
            s.setLong(3, conversation.getAdId());
            s.setInt(4,conversation.isBlocked() ? 1 : 0);
            int e=s.executeUpdate();
            if(e!=1){
                throw new Exception("error in method save");
            }
        }
    }
    public static boolean isConversationExist(Long sellerId,Long adId,Long buyerId) throws Exception {
        String query = "SELECT id FROM conversations WHERE seller_id = ? AND ad_id = ? AND buyer_id = ?";
        try(Connection c=DatabaseConnection.getConnection();
            PreparedStatement s=c.prepareStatement(query)){
            s.setLong(1, sellerId);
            s.setLong(2, adId);
            s.setLong(3, buyerId);
            ResultSet rs=s.executeQuery();
            return rs.next();
        }catch(Exception e){
            throw  new Exception("Error in method isConversationExist");
        }
    }

    public static Conversation find(Long sellerId, Long adId, Long buyerId) throws Exception {
        if(sellerId==null || adId==null || buyerId==null){
            throw new Exception("sellerId and adId and buyerId cannot be null");
        }
        if(!isConversationExist(sellerId,adId,buyerId)){
            throw new Exception("this conversation does not exist");
        }
        String query="SELECT * FROM conversations WHERE seller_id = ? AND ad_id = ? AND buyer_id = ?";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(query)){
            s.setLong(1, sellerId);
            s.setLong(2, adId);
            s.setLong(3, buyerId);
            ResultSet rs=s.executeQuery();
            if(rs.next()){
                Conversation conversation=new Conversation();
                conversation.setId(rs.getLong("id"));
                conversation.setSellerId(sellerId);
                conversation.setAdId(adId);
                conversation.setBuyerId(buyerId);
                conversation.setBlocked(rs.getBoolean("is_blocked"));
                return conversation;
            }
            throw new Exception("error in method find");
        }
    }
    public static void update(Conversation conversation) throws Exception {
        if(conversation==null){
            throw new Exception("conversation cannot be null");
        }
        if(!isConversationExist(conversation.getSellerId(), conversation.getAdId(), conversation.getBuyerId())) {
            throw new Exception("this conversation does not exist");
        }
        String query="UPDATE conversations SET is_blocked = ? WHERE id = ?";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(query)){
            s.setInt(1, conversation.isBlocked() ? 1 : 0);
            s.setLong(2, conversation.getId());
            int e=s.executeUpdate();
            if(e!=1){
                throw new Exception("error in method update");
            }
        }
    }
    public static void delete(Long sellerId, Long adId, Long buyerId) throws Exception {
        if(sellerId==null || adId==null || buyerId==null){
            throw new Exception("sellerId and adId and buyerId cannot be null");
        }
        if(!isConversationExist(sellerId,adId,buyerId)){
            throw new Exception("this conversation does not exist");
        }
        String query="DELETE FROM conversations WHERE seller_id = ? AND ad_id = ? AND buyer_id = ?";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(query)) {
            s.setLong(1, sellerId);
            s.setLong(2, adId);
            s.setLong(3, buyerId);
            int e=s.executeUpdate();
            if(e!=1){
                throw new Exception("error in method delete");
            }
        }
    }

    public static List<Conversation> findByUserId(Long userId) throws Exception {
        String query = "SELECT * FROM conversations WHERE seller_id = ? OR buyer_id = ? ORDER BY id DESC";
        List<Conversation> list = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {

            s.setLong(1, userId);
            s.setLong(2, userId);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                Conversation conv = new Conversation();
                conv.setId(rs.getLong("id"));
                conv.setAdId(rs.getLong("ad_id"));
                conv.setBuyerId(rs.getLong("buyer_id"));
                conv.setSellerId(rs.getLong("seller_id"));
                conv.setBlocked(rs.getInt("is_blocked") == 1);
                list.add(conv);
            }
        } catch (SQLException e) {
            throw new Exception("Error in findByUserId: " + e.getMessage());
        }

        return list;
    }
}
