package app.models.requests.manager;

public class GetPendingAdsRequest {
    private String token;
    private String username;

    public GetPendingAdsRequest(String username, String token) {
        this.token = token;
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

