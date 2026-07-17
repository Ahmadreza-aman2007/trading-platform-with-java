package app.dto.manager;

public class AddCategoryRequest {
    private String username;
    private String token;
    private String categoryName;
    public AddCategoryRequest(){}
    public AddCategoryRequest(String username, String token, String categoryName) {
        this.username = username;
        this.token = token;
        this.categoryName = categoryName;
    }
    //getter methods
    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getCategoryName() {
        return categoryName;
    }
    //setter methods

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
