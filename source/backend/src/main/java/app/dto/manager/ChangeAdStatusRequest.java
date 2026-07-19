package app.dto.manager;

import app.entities.users.enums.AdStatus;

public class ChangeAdStatusRequest {
    private int adId;
    private String status;
    private String username;
    private String token;
    public ChangeAdStatusRequest(){}

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public int getAdId() {
        return adId;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
