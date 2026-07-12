package app.models.response.manager;

public class UserResponseForManager {
    private int id;
    private String username;
    private String phoneNumber;
    private String fullname;
    private boolean blocked;
    private String createdDate;
    public UserResponseForManager(){}
    public UserResponseForManager(int id, String username, String phoneNumber, String fullname,  String createdDate,boolean blocked) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.fullname = fullname;
        this.blocked = blocked;
        this.createdDate = createdDate;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getFullname() {
        return fullname;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreatedDate(String createdAt) {
        this.createdDate = createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }
}
