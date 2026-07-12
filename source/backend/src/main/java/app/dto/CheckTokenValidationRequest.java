package app.dto;

public class CheckTokenValidationRequest {
    private String username;
    private String token;
    public CheckTokenValidationRequest(String username, String token) {
        this.username = username;
        this.token = token;
    }
    public CheckTokenValidationRequest() {}
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
