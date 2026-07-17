package app.dto.manager;

public class GetUserResponse {
    private Long id;
    private String username;
    private String phoneNumber;
    private boolean isBlocked;
    private String fullname;
    private String createdDate;
    public GetUserResponse(Long id,String username,  String phoneNumber, String fullname,boolean isBlocked,String createdDate) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.fullname = fullname;
        this.isBlocked = isBlocked;
        this.createdDate = createdDate;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
