package app.models.entities;

public class User {
    private Long id;
    private String username;
    private String phoneNumber;
    private String role;
    private String fullname;

    public User(Long id,String username,String phoneNumber,String role,String fullname){
        this.username=username;
        this.phoneNumber=phoneNumber;
        this.role=role;
        this.fullname=fullname;
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getFullname() {
        return fullname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
