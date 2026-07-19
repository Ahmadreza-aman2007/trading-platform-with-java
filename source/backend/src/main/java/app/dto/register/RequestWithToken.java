package app.dto.register;

public class RequestWithToken {
    private String token;
    private String Username;
    public RequestWithToken(String token, String username) {
        this.token = token;
        this.Username = username;
    }
    public RequestWithToken() {}
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUsername() {
        return Username;
    }
    public void setUsername(String username) {
        Username = username;
    }
}
