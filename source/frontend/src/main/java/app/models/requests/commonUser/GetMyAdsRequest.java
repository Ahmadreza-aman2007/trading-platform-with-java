package app.models.requests.commonUser;

public class GetMyAdsRequest {
    private String username;
    private String token;

    public GetMyAdsRequest(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() { return username; }
    public String getToken() { return token; }
}
