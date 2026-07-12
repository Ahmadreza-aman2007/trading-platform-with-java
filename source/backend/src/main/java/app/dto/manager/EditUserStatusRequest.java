package app.dto.manager;

public class EditUserStatusRequest {
    private String senderUsername;
    private String token;
    private int id;
    private boolean isBlocked;
    public EditUserStatusRequest() {}
    public EditUserStatusRequest(String senderUsername, String token, int id, boolean isBlocked) {
        this.senderUsername = senderUsername;
        this.token = token;
        this.id = id;
        this.isBlocked = isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
}
