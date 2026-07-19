package app.models.entities;
public class Favorite {
    //table fields
    private Long id;

    private Long userId;

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
