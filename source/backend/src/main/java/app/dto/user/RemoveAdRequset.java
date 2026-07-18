package app.dto.user;

public class RemoveAdRequset {
    private Long adId;
    private String username;
    private String token;
    public RemoveAdRequset() {}

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Long getAdId() {
        return adId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
