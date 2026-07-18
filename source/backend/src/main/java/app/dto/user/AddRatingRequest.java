package app.dto.user;

public class AddRatingRequest {
    private String username;
    private String token;
    private Long raterId;
    private Long sellerId;
    private Long adId;
    private int score;
    private String comment;
    public AddRatingRequest() {}
    public AddRatingRequest(String username, String token, Long raterId, Long sellerId, Long adId, int score, String comment) {
        this.username = username;
        this.token = token;
        this.raterId = raterId;
        this.sellerId = sellerId;
        this.adId = adId;
        this.score = score;
        this.comment = comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public void setRaterId(Long raterId) {
        this.raterId = raterId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public String getUsername() {
        return username;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public String getToken() {
        return token;
    }

    public int getScore() {
        return score;
    }

    public Long getAdId() {
        return adId;
    }

    public String getComment() {
        return comment;
    }

    public Long getRaterId() {
        return raterId;
    }
}
