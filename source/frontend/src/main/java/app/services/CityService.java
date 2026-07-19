package app.services;

import app.models.entities.City;
import app.models.requests.manager.AddCityRequest;
import app.utils.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class CityService {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<City> sendGetAllCities() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/public/get-cities"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<City>>() {});
        } else {
            throw new Exception("خطا در دریافت شهرها: " + response.statusCode());
        }
    }

    public static void sendAddCityRequest(String cityName) throws Exception {
        AddCityRequest requestBody = new AddCityRequest(
                SessionManager.getCurrentUser().getUsername(),
                SessionManager.getToken(),
                cityName
        );

        String json = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/manager/add-city"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("خطا در افزودن شهر: " + response.statusCode() + " - " + response.body());
        }
    }
}