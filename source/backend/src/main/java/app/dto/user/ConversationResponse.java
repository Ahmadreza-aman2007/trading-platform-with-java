package app.dto.user;

public class ConversationResponse {
    private Long id;
    private Long adId;
    private Long buyerId;
    private Long sellerId;
    private boolean isBlocked;

    public ConversationResponse() {}

    public ConversationResponse(Long id, Long adId, Long buyerId, Long sellerId, boolean isBlocked) {
        this.id = id;
        this.adId = adId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.isBlocked = isBlocked;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAdId() { return adId; }
    public void setAdId(Long adId) { this.adId = adId; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }
}