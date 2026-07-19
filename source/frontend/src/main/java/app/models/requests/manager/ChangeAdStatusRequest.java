package app.models.requests.manager;

public class ChangeAdStatusRequest {
    private Long adId;
    private String newStatus;
    private String username;
    private String token;
    public ChangeAdStatusRequest(Long adId, String newStatus,String username, String token) {
        this.adId = adId;
        this.newStatus = newStatus;
        this.username = username;
        this.token = token;
    }

    public Long getAdId() {
        return adId;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }
}
