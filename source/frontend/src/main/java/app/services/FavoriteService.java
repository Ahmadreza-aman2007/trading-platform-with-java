package app.services;

import app.models.entities.Advertisement;
import app.models.requests.commonUser.AddFavoriteRequest;
import app.models.requests.commonUser.GetFavoritesRequest;
import app.models.requests.commonUser.RemoveFavoriteRequest;
import app.utils.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class FavoriteService {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean isFavorite(Long adId) throws Exception {
        GetFavoritesRequest request = new GetFavoritesRequest(SessionManager.getCurrentUser().getUsername(), SessionManager.getToken(), adId);
        String json = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/favorites/check"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return Boolean.parseBoolean(response.body());
        } else {
            throw new Exception("خطا در بررسی علاقه‌مندی: " + response.statusCode());
        }
    }
    public static void addFavorite(Long userId, Long adId) throws Exception {
        AddFavoriteRequest request = new AddFavoriteRequest(SessionManager.getCurrentUser().getUsername(), SessionManager.getToken(),adId,userId);
        String json = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/favorites/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("خطا در افزودن به علاقه‌مندی‌ها: " + response.statusCode() + " - " + response.body());
        }
    }
    public static void removeFavorite( Long adId) throws Exception {
        RemoveFavoriteRequest request = new RemoveFavoriteRequest(adId, SessionManager.getCurrentUser().getUsername(), SessionManager.getToken());
        String json = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/favorites/remove"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("خطا در حذف از علاقه‌مندی‌ها: " + response.statusCode() + " - " + response.body());
        }
    }
    public static List<Advertisement> getUserFavorites(Long userId) throws Exception {
        GetFavoritesRequest request = new GetFavoritesRequest(SessionManager.getCurrentUser().getUsername() ,SessionManager.getToken() ,userId);
        String json = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/favorites/list"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Advertisement>>() {});
        } else {
            throw new Exception("خطا در دریافت لیست علاقه‌مندی‌ها: " + response.statusCode()

            );
        }
    }}
