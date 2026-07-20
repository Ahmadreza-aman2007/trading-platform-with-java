package app.entities.conversation;

import app.entities.users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    // fields in table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content",nullable = false)
    private String content;
    @Column(name = "time",nullable = false)
    private String time;
    @Column(name = "conversation_id",nullable = false )
    private Long conversationId;
    @Column(name = "sender_id",nullable = false)
    private Long senderId;
    @Column(name = "is_read")
    private boolean isRead;

    //constructors
    public Message() {}
    public Message(String content,String time ,Long conversationId,Long senderId,boolean isRead){
        this.content=content;
        this.isRead=isRead;
        this.senderId=senderId;
        this.time=time;
        this.conversationId=conversationId;
    }
    //getter methods
    public Long getConversationId() {
        return conversationId;
    }

    public String getTime() {
        return time;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public boolean isRead() {
        return isRead;
    }

    //setter methods

    public void setContent(String content) {
        this.content = content;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setSender(Long senderId) {
        this.senderId = senderId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
