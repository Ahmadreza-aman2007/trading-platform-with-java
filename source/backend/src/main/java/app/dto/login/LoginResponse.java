package app.dto.login;

public class LoginResponse {
    private Long id;
    private String token;
    private String username;
    private String fullname;
    private String role;
    private String phoneNumber;

    public LoginResponse(Long id,String token, String username, String fullname, String role, String phoneNumber) {
        this.role = role;
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.token = token;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
