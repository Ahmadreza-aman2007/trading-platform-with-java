package app.services;

import app.models.requests.manager.EditUserRequest;
import app.utils.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class UserService {
    private static HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private static ObjectMapper mapper = new ObjectMapper();

    private static void sendEditRequest(EditUserRequest editUserRequest) throws Exception {
        String json = mapper.writeValueAsString(editUserRequest);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/manager/editUser"))
                .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10)).build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        if (status == 200) {
            System.out.println("success");
        } else {
            throw new Exception("error in edit user from server");
        }
    }
    public static Long getUserIdByUsername(String username) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/public/by-username/" + username))
                .header("Content-Type", "application/json")
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return Long.parseLong(response.body());
        } else {
            throw new Exception("کاربری با این نام کاربری یافت نشد");
        }
    }
}
