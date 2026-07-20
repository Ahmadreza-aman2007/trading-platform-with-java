package app.dto.user;

public class CreateConversationRequest {
    private Long adId;
    private Long buyerId;
    private Long sellerId;
    private String username;
    private String token;

    public CreateConversationRequest() {}

    public CreateConversationRequest(Long adId, Long buyerId, Long sellerId, String username, String token) {
        this.adId = adId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.username = username;
        this.token = token;
    }

    public Long getAdId() { return adId; }
    public void setAdId(Long adId) { this.adId = adId; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}