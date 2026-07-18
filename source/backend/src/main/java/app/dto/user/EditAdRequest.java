package app.dto.user;

import app.entities.users.enums.AdStatus;

public class EditAdRequest {
    private Long id;
    private String token;
    private String title;
    private String description;
    private long price;
    private String sellerUsername;
    private String city;
    private String category;
    public EditAdRequest() {}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getSellerUsername() {
        return sellerUsername;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getCity() {
        return city;
    }

    public long getPrice() {
        return price;
    }
}
