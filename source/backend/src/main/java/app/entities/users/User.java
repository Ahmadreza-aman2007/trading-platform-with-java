package app.entities.users;


import app.entities.users.enums.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "user_role")
    private UserRole userRole;
    @Column(name="is_blocked")
    private boolean isBlocked=false;
    @Column(name="fullname")
    private String fullname;
    @Column(name = "created_at")
    private String createdDate= LocalDateTime.now().toString();

    public User() {}
    public User(String username, String password, String phoneNumber, UserRole userRole, String fullname) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isBlocked = false;
        this.userRole = userRole;
        this.fullname = fullname;
        createdDate = LocalDateTime.now().toString();
    }
    public User(Long id ,String username, String password, String phoneNumber, UserRole userRole, String fullname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    @PrePersist
    protected void onCreate() {
        this.createdDate = java.time.LocalDateTime.now().toString();
    }
}
