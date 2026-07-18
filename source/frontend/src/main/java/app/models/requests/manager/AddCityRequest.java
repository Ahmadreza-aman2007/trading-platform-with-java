package app.models.requests.manager;


public class AddCityRequest {
    private String username;
    private String token;
    private String cityName;

    public AddCityRequest(String username, String token, String cityName) {
        this.username = username;
        this.token = token;
        this.cityName = cityName;
    }

    //getter methods

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getCityName() {
        return cityName;
    }
    //setter methods
    public void setToken(String token) {
        this.token = token;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
