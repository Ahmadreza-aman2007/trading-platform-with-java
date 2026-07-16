package app.entities.token;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true,name = "token")
    private String token;
    @Column(name = "username" ,nullable = false)
    private String username;
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @Column(name="expires_at",nullable = false)
    private LocalDateTime expiresAt;
    @Column(name = "is_revoked")
    private boolean revoked;
    public Token(){}
    public Token(String token,String username,LocalDateTime createdAt,LocalDateTime expiresAt,boolean revoked){
        this.token=token;
        this.username=username;
        this.createdAt=createdAt;
        this.expiresAt=expiresAt;
        this.revoked=revoked;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
