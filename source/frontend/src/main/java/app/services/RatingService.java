package app.services;

import app.models.entities.Rating;
import app.models.requests.commonUser.AddRatingRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class RatingService {

    private static HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static int getRatingCount(String sellerUsername) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/ratings/seller/" + sellerUsername + "/count"))
                .header("Content-Type", "application/json")
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return Integer.parseInt(response.body());
        } else {
            return 0;
        }
    }

    public static void addRating(Long raterId, Long sellerId, Long adId, int score, String comment, String username, String token) throws Exception {
        AddRatingRequest request = new AddRatingRequest(raterId, sellerId, adId, score, comment, username, token);
        String json = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/ratings/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("✅ امتیاز با موفقیت ثبت شد");
        } else {
            throw new Exception("خطا در ثبت امتیاز: " + response.statusCode() + " - " + response.body());
        }
    }


    public static double getAverageScore(String sellerUsername) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/ratings/seller/" + sellerUsername + "/average"))
                .header("Content-Type", "application/json")
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return Double.parseDouble(response.body());
        } else {
            return 0.0;
        }
    }


    public static List<Rating> getSellerRatingsByUsername(String username,Long adId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/ratings/seller/get-all-ratings/seller-username/" + username+"/ad-id/"+adId))
                .header("Content-Type", "application/json")
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("📥 Status Code: " + response.statusCode());
        System.out.println("📥 Response Body: " + response.body());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Rating>>() {});
        } else {
            throw new Exception("خطا در دریافت امتیازات: " + response.statusCode() + " - " + response.body());
        }
    }
}
