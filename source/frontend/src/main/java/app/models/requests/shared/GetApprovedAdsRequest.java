package app.models.requests.shared;

public class GetApprovedAdsRequest {
    private String username;
    private String token;

    public GetApprovedAdsRequest(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() { return username; }
    public String getToken() { return token; }
}