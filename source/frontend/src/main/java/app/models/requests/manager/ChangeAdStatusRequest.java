package app.models.requests.manager;

public class ChangeAdStatusRequest {
    private Long adId;
    private String status;
    private String username;
    private String token;
    private String note;
    public ChangeAdStatusRequest(Long adId, String status,String username, String token) {
        this.adId = adId;
        this.status = status;
        this.username = username;
        this.token = token;
    }

    public ChangeAdStatusRequest(Long adId, String status, String note, String username, String token) {
        this.adId = adId;
        this.status = status;
        this.note = note;
        this.username = username;
        this.token = token;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    public String getStatus() {
        return status;
    }
}
