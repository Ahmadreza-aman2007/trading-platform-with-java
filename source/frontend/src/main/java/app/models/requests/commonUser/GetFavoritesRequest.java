package app.models.requests.commonUser;

public class GetFavoritesRequest {
    private String username;
    private String token;
    private Long userId;
    public GetFavoritesRequest(String username, String token, Long userId) {
        this.username = username;
        this.token = token;
        this.userId = userId;
    }
    public GetFavoritesRequest(){}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
