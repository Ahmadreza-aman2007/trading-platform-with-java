package app.dto.register;

public class RegisterRequest {
    private String fullname;
    private String username;
    private String phoneNumber;
    private String password;
    public RegisterRequest(){}
    public RegisterRequest(String fullname ,String username ,String phoneNumber,String password){
        this.fullname=fullname;
        this.username=username;
        this.password=password;
        this.phoneNumber=phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
