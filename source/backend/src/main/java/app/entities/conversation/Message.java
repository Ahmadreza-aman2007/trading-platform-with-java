package app.entities.conversation;

import app.entities.users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content",nullable = false)
    private String content;
    @Column(name = "time",nullable = false)
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "conversation_id",nullable = false)
    private Conversation conversation;
    @ManyToOne
    @JoinColumn(name = "sender_id",nullable = false)
    private User sender;
    @Column(name = "is_read")
    private boolean isRead;
    public Message(String content,LocalDateTime time ,Conversation conversation,User sender,boolean isRead){
        this.content=content;
        this.isRead=isRead;
        this.sender=sender;
        this.time=time;
        this.conversation=conversation;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
