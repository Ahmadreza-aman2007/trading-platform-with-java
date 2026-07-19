package app.services;

import app.models.entities.ProductCategory;
import app.models.requests.manager.AddCategoryRequest;
import app.utils.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class CategoryService {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<ProductCategory> sendGetAllCategories() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/public/get-categories"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<ProductCategory>>() {});
        } else {
            throw new Exception("خطا در دریافت دسته‌بندی‌ها: " + response.statusCode());
        }
    }
    public static void sendAddCategoryRequest(String categoryName) throws Exception {
        AddCategoryRequest requestBody = new AddCategoryRequest(
                SessionManager.getCurrentUser().getUsername(),
                SessionManager.getToken(),
                categoryName
        );

        String json = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/manager/add-product-category"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("خطا در افزودن دسته‌بندی: " + response.statusCode() + " - " + response.body());
        }
    }
}