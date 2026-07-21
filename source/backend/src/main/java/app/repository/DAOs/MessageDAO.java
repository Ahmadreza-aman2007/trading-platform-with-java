package app.repository.DAOs;

import app.entities.conversation.Message;
import app.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageDAO {
    public static void save(Message message) throws Exception {
            String query = "INSERT INTO messages (sender_id, content,conversation_id,time,is_read) VALUES (?, ?, ?, ?, ?)";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(query)){
            s.setLong(1, message.getSenderId());
            s.setString(2, message.getContent());
            s.setLong(3, message.getConversationId());
            s.setObject(4,LocalDateTime.now().toString());
            s.setInt(5, message.isRead() ? 1 : 0);
            int e=s.executeUpdate();
            if(e!=1){
                throw new Exception("Error in method save");
            }
        }
    }
    public static void update(Message message) throws Exception {
     if(message==null||message.getId()==null||message.getSenderId()==null||message.getConversationId()==null){
         throw new Exception("message is null");
     }
     Message message1=find(message.getId());
        if(!Objects.equals(message1.getConversationId(), message.getConversationId()) ||message1.getTime()!=message.getTime()|| !Objects.equals(message1.getSenderId(), message.getSenderId())){
         throw new Exception("conversationId or senderId or time are not match");
     }
        String query="UPDATE messages SET content = ? WHERE id = ?";
     try(Connection c=DatabaseConnection.getConnection();
     PreparedStatement s=c.prepareStatement(query)){
         s.setString(1,message.getContent());
         s.setLong(2, message1.getId());
         int e=s.executeUpdate();
         if(e!=1){
             throw new Exception("Error in method update");
         }
     }

    }
    public static void delete(Message message) throws Exception {
        if(message==null||message.getId()==null||message.getSenderId()==null||message.getConversationId()==null){
            throw new Exception("message is null");
        }
        Message message1=find(message.getId());
        if(!Objects.equals(message1.getConversationId(), message.getConversationId()) ||message1.getTime()!=message.getTime()|| !Objects.equals(message1.getSenderId(), message.getSenderId())){
            throw new Exception("conversationId or senderId or time are not match");
        }
        String query="DELETE FROM messages WHERE id = ?";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(query)){
            s.setLong(1, message1.getId());
            int e=s.executeUpdate();
            if(e!=1){
                throw new Exception("Error in method delete");
            }
        }
    }
    public static Message find(Long id) throws Exception {
        String query = "SELECT *  FROM messages WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {
            s.setLong(1, id);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                Message message = new Message();
                message.setId(rs.getLong("id"));
                message.setSenderId(rs.getLong("sender_id"));
                message.setContent(rs.getString("content"));
                message.setConversationId(rs.getLong("conversation_id"));
                message.setTime(rs.getString("time"));
                message.setRead(rs.getInt("is_read") == 1);
                return message;
            }
            throw new Exception("error in method find");
        }
    }
    public static List<Message> findByConversationId(Long conversationId) throws Exception {
        String query = "SELECT * FROM messages WHERE conversation_id = ? ORDER BY time ASC";
        List<Message> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {
            s.setLong(1, conversationId);
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                Message m = new Message();
                m.setId(rs.getLong("id"));
                m.setSenderId(rs.getLong("sender_id"));
                m.setContent(rs.getString("content"));
                m.setConversationId(rs.getLong("conversation_id"));
                m.setTime(rs.getString("time"));  // ← String
                m.setRead(rs.getInt("is_read") == 1);
                list.add(m);
            }
        }
        return list;
    }
}
