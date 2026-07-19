package app.services;

import app.models.entities.User;
import app.models.requests.shared.LoginRequest;
import app.models.requests.shared.RegisterRequest;
import app.utils.SessionManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AuthService {


    private static HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private static ObjectMapper objectMapper = new ObjectMapper();

    public User sendLoginRequest(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);
            String json = objectMapper.writeValueAsString(loginRequest);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/auth/login"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());
            int statusCode = httpResponse.statusCode();
        return switch (statusCode) {
            case 200 -> objectMapper.readValue(httpResponse.body(), User.class);
            case 404 -> throw new Exception("User not found");
            case 401 -> throw new Exception("Unauthorized");
            case 500 -> throw new Exception("Internal Server Error");
            default -> null;
        };
    }
    public void sendRegisterRequest(RegisterRequest registerRequest) throws Exception {
        String json= objectMapper.writeValueAsString(registerRequest);
        HttpRequest httpRequest= HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/auth/register")).header("Content-Type","application/json").POST(HttpRequest.BodyPublishers.ofString(json)).timeout(Duration.ofSeconds(10)).build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest,
                HttpResponse.BodyHandlers.ofString());
        int statusCode = httpResponse.statusCode();
        if (statusCode == 200) {
            return;
        }
        throw new Exception("Internal Server Error");
    }

}
