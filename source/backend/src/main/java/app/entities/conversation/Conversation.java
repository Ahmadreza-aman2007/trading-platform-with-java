package app.entities.conversation;

import jakarta.persistence.*;

@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ad_id",nullable = false)
    private Long adId;
    @ManyToOne
    @JoinColumn(name = "buyer_id",nullable = false)
    private  Long buyerId;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Long sellerId;
    @Column(name = "is_blocked")
    private boolean isBlocked;
    public Conversation() {}
    public Conversation(Long productId,Long buyerId,Long sellerId,boolean isBlocked) {
        this.buyerId=buyerId;
        this.adId =productId;
        this.sellerId=sellerId;
        this.isBlocked=isBlocked;
    }

    public Long getId() {
        return id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public Long getAdId() {
        return adId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
