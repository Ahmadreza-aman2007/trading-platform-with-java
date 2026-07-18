package app.models.requests.commonUser;

import java.time.LocalDateTime;

public class AddAdRequest {

    private String token;
    private String title;
    private String description;
    private long price;
    private String sellerUsername;
    private String city;
    private String category;
    private String createdAt;
    public AddAdRequest(String token,String title, String description, long price, String sellerUsername, String city, String category) {
        this.token = token;
        this.title = title;
        this.description = description;
        this.price = price;
        this.sellerUsername = sellerUsername;
        this.city = city;
        this.category = category;
        this.createdAt = LocalDateTime.now().toString();
    }


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
