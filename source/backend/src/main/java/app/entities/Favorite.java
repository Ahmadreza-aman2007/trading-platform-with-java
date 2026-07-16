package app.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Favorites")
public class Favorite {
    //table fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "ad_id",nullable = false)
    private Long adId;
// constructors
    public Favorite(Long userId, Long adId) {
        this.userId = userId;
        this.adId = adId;
    }
    public Favorite() {}
// getter methods
    public Long getId() {
        return id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAdId() {
        return adId;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }
}
