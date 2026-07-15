package app.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "rater_id", nullable = false)
    private int raterId;

    @Column(name = "seller_id", nullable = false)
    private int sellerId;

    @Column(name = "ad_id", nullable = false)
    private int adId;

    @Column(nullable = false)
    private int score;

    @Column(length = 500)
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Rating() {}

    public Rating(int raterId, int sellerId, int adId, int score, String comment) {
        this.raterId = raterId;
        this.sellerId = sellerId;
        this.adId = adId;
        this.score = score;
        this.comment = comment;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRaterId() { return raterId; }
    public void setRaterId(int raterId) { this.raterId = raterId; }

    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }

    public int getAdId() { return adId; }
    public void setAdId(int adId) { this.adId = adId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}