package app.dto.user;

public class SendMessageRequest {
    private Long conversationId;
    private Long senderId;
    private String content;
    private String username;
    private String token;

    public SendMessageRequest() {}

    public SendMessageRequest(Long conversationId, Long senderId, String content, String username, String token) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.username = username;
        this.token = token;
    }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}