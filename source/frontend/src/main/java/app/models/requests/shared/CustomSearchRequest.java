package app.models.requests.shared;

public class CustomSearchRequest {
    private String keyword;
    private String category;
    private String city;
    private Long priceFloor;
    private Long priceCeiling;
    public CustomSearchRequest() {}

    public void setCategory(String category) {
        this.category = category;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPriceCeiling(Long priceCeiling) {
        this.priceCeiling = priceCeiling;
    }

    public void setPriceFloor(Long priceFloor) {
        this.priceFloor = priceFloor;
    }

    public String getCategory() {
        return category;
    }

    public String getCity() {
        return city;
    }

    public Long getPriceCeiling() {
        return priceCeiling;
    }

    public Long getPriceFloor() {
        return priceFloor;
    }

    public String getKeyword() {
        return keyword;
    }
}
