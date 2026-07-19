package app.models.requests.commonUser;

public class RemoveFavoriteRequest {
    private Long adId;
    private String username;
    private String token;
    public RemoveFavoriteRequest(Long adId, String username, String token) {
        this.adId = adId;
        this.username = username;
        this.token = token;
    }
    public RemoveFavoriteRequest() {}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Long getAdId() {
        return adId;
    }
}
