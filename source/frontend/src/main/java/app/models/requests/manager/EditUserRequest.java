package app.models.requests.manager;

public class EditUserRequest {
    private String senderUsername;
    private String token;
    private int id;
    private boolean isBlocked;

    public EditUserRequest(String senderUsername, String token, int id, boolean isBlocked) {
        this.senderUsername = senderUsername;
        this.token = token;
        this.id = id;
        this.isBlocked = isBlocked;
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

    public void setBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
}
