package app.dto.user;

public class AddFavoriteRequest {
    private String username;
    private String token;
    private Long addId;
    private Long id;

    public AddFavoriteRequest(String username, String token, Long addId,Long id) {
        this.username = username;
        this.token = token;
        this.id=id;
        this.addId = addId;
    }
    public AddFavoriteRequest() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAddId(Long addId) {
        this.addId = addId;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Long getAddId() {
        return addId;
    }
}
