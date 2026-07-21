package app.dto.user;

import app.entities.users.enums.AdStatus;

public class AddAdRequest {

    private String token;
    private String title;
    private String description;
    private long price;
    private String sellerUsername;
    private String city;
    private String category;
    private String createdAt;
    private java.util.List<String> images; // عکس‌های آگهی به‌صورت Base64
    public AddAdRequest() {}

    public java.util.List<String> getImages() { return images; }

    public void setImages(java.util.List<String> images) { this.images = images; }


    public void setToken(String token) {
        this.token = token;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public String getToken() {
        return token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
