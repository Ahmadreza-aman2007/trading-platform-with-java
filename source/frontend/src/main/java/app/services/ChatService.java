package app.services;

import app.utils.SessionManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ChatService {

    private static final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // ===== شروع یا دریافت گفتگوی موجود (خروجی: JSON گفتگو شامل id) =====
    public static JsonNode startOrGetConversation(Long adId, Long sellerId) throws Exception {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("adId", adId);
        body.put("buyerId", SessionManager.getCurrentUser().getId());
        body.put("sellerId", sellerId);
        body.put("username", SessionManager.getCurrentUser().getUsername());
        body.put("token", SessionManager.getToken());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/chat/conversations/start"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readTree(response.body());
        }
        throw new Exception("خطا در شروع گفتگو: " + response.statusCode());
    }

    // ===== ارسال پیام =====
    public static void sendMessage(Long conversationId, String content) throws Exception {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("conversationId", conversationId);
        body.put("senderId", SessionManager.getCurrentUser().getId());
        body.put("content", content);
        body.put("username", SessionManager.getCurrentUser().getUsername());
        body.put("token", SessionManager.getToken());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/chat/messages/send"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("خطا در ارسال پیام: " + response.statusCode() + " - " + response.body());
        }
    }

    // ===== دریافت پیام‌های یک گفتگو =====
    public static List<JsonNode> getMessages(Long conversationId) throws Exception {
        String url = "http://localhost:8080/api/chat/messages/" + conversationId
                + "?username=" + URLEncoder.encode(SessionManager.getCurrentUser().getUsername(), StandardCharsets.UTF_8)
                + "&token=" + URLEncoder.encode(SessionManager.getToken(), StandardCharsets.UTF_8);
        return getJsonList(url, "خطا در دریافت پیام‌ها");
    }

    // ===== دریافت گفتگوهای کاربر جاری =====
    public static List<JsonNode> getMyConversations() throws Exception {
        String url = "http://localhost:8080/api/chat/conversations/" + SessionManager.getCurrentUser().getId()
                + "?username=" + URLEncoder.encode(SessionManager.getCurrentUser().getUsername(), StandardCharsets.UTF_8)
                + "&token=" + URLEncoder.encode(SessionManager.getToken(), StandardCharsets.UTF_8);
        return getJsonList(url, "خطا در دریافت گفتگوها");
    }

    private static List<JsonNode> getJsonList(String url, String errorPrefix) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JsonNode array = objectMapper.readTree(response.body());
            List<JsonNode> list = new ArrayList<>();
            if (array.isArray()) {
                array.forEach(list::add);
            }
            return list;
        }
        throw new Exception(errorPrefix + ": " + response.statusCode());
    }
}
