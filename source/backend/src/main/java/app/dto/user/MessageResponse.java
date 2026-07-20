package app.dto.user;

public class MessageResponse {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private String content;
    private String time;  // ← String
    private boolean isRead;

    public MessageResponse() {}

    public MessageResponse(Long id, Long senderId, String senderUsername, String content, String time, boolean isRead) {
        this.id = id;
        this.senderId =

                senderId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.time = time;
        this.isRead = isRead;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderUsername() { return senderUsername; }
    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
