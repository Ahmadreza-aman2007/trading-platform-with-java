package app.models.entities;


import java.time.LocalDateTime;

public class Rating {

    private Long id;

    private Long raterId;

    private Long sellerId;

    private Long adId;

    private int score;

    private String comment;

    private LocalDateTime createdAt;

    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Rating() {}

    public Rating(Long raterId, Long sellerId, Long adId, int score, String comment) {
        this.raterId = raterId;
        this.sellerId = sellerId;
        this.adId = adId;
        this.score = score;
        this.comment = comment;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRaterId() { return raterId; }
    public void setRaterId(Long raterId) { this.raterId = raterId; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public Long getAdId() { return adId; }
    public void setAdId(Long adId) { this.adId = adId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}