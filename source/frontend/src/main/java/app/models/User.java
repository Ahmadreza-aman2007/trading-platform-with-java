package app.models;

public class User {
    private String username;
    private String phoneNumber;
    private String role;
    private String fullname;

    public User(String username,String phoneNumber,String role,String fullname){
        this.username=username;
        this.phoneNumber=phoneNumber;
        this.role=role;
        this.fullname=fullname;
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
}
