package app.models.requests.commonUser;

public class AddRatingRequest {
    private Long raterId;
    private Long sellerId;
    private Long adId;
    private int score;
    private String comment;
    private String username;
    private String token;

    public AddRatingRequest() {}

    public AddRatingRequest(Long raterId, Long sellerId, Long adId, int score, String comment, String username, String token) {
        this.raterId = raterId;
        this.sellerId = sellerId;
        this.adId = adId;
        this.score = score;
        this.comment = comment;
        this.username = username;
        this.token = token;
    }

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

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}