package app.dto.manager;

public class GetUsersRequest {
    private String username;
    private String token;
    public GetUsersRequest(String username, String token) {
        this.username = username;
        this.token = token;
    }
    public GetUsersRequest() {}
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
