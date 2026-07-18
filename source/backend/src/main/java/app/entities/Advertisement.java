
package app.entities;

import app.entities.users.enums.AdStatus;
public class Advertisement {
    private Long id;
    private String title;
    private String description;
    private long price;
    private String sellerUsername;
    private String city;
    private String category;
    private AdStatus status;
    private String createdAt;


    public Advertisement(String title, String description, long price, String sellerUsername, String city, String category) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.sellerUsername = sellerUsername;
        this.city = city;
        this.category = category;
        this.status = AdStatus.PENDING;
    }


    public Advertisement(Long id, String title, String description, long price, String sellerUsername, String city, String category, AdStatus status, String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.sellerUsername = sellerUsername;
        this.city = city;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
    }
    public Advertisement(Long id, String title, String description, long price, String sellerUsername, String city, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.sellerUsername = sellerUsername;
        this.city = city;
        this.category = category;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public long getPrice() { return price; }
    public String getSellerUsername() { return sellerUsername; }
    public String getCity() { return city; }
    public String getCategory() { return category; }
    public AdStatus getStatus() { return status; }
    public void setStatus(AdStatus status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
}