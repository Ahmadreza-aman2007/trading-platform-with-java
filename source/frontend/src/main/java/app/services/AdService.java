package app.services;

import app.models.entities.Advertisement;
import app.models.requests.commonUser.AddAdRequest;
import app.models.requests.commonUser.EditAdRequest;
import app.models.requests.commonUser.GetMyAdsRequest;
import app.models.requests.commonUser.RemoveAdRequest;
import app.models.requests.manager.ChangeAdStatusRequest;
import app.models.requests.manager.GetPendingAdsRequest;
import app.models.requests.shared.GetApprovedAdsRequest;
import app.utils.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

public class AdService {

    private static HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private static ObjectMapper objectMapper = new ObjectMapper();


    public static ArrayList<Advertisement> sendGetPendingAdRequest() throws Exception{
        ArrayList<Advertisement> advertisements = new ArrayList<>();
        GetPendingAdsRequest getPendingAdsRequest = new GetPendingAdsRequest(SessionManager.getCurrentUser().getUsername(), SessionManager.getToken());
            String json = objectMapper.writeValueAsString(getPendingAdsRequest);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/manager/get-pending-ads"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();

                    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                    ArrayList<Advertisement> ads = objectMapper.readValue(httpResponse.body(), new TypeReference<ArrayList<Advertisement>>() {
                    });
                    if (httpResponse.statusCode() == 200) {
                        for (Advertisement advertisement : ads) {
                            System.out.println(advertisement.getId());
                        }
                        System.out.println("successful");
                        return ads;
                    }
                    throw new Exception("get pending adds failed");
                }
    public static void sendRemoveAdRequest(Long id) throws Exception{
        RemoveAdRequest removeAdRequest = new RemoveAdRequest(id,SessionManager.getCurrentUser().getUsername(),SessionManager.getToken());
            String json = objectMapper.writeValueAsString(removeAdRequest);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/user/remove-ad"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();
                    HttpResponse<String> httpResponse=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
                    if(httpResponse.statusCode()==200){
                        System.out.println("successful");
                    }
                    throw new Exception("remove ad failed");
                }
    public static void sendEditAdRequest(Long id,String username,String token,String title,String description,long price,String city,String category) throws Exception{
        EditAdRequest request = new EditAdRequest(id,token,title,description,price,username,city,category);

            String json = objectMapper.writeValueAsString(request);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/user/edit-ad"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();
                    HttpResponse<String> httpResponse=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
                    if(httpResponse.statusCode()>=200&&httpResponse.statusCode()<300){
                        System.out.println("successful");
                    }
                    throw new Exception("edit ad failed");
                }

    public static void sendAddAdRequest(String title, String description, long price, String sellerUsername, String city, String category) throws Exception{
        AddAdRequest addAdRequest=new AddAdRequest(SessionManager.getToken(),title,description,price,sellerUsername,city,category);
            String json = objectMapper.writeValueAsString(addAdRequest);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/user/add-ad"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();

                    HttpResponse<String> httpResponse=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
                    if(httpResponse.statusCode()==200){
                        System.out.println("successful");
                    }
                    throw new Exception("add ad failed");
                }

    public static void changeAdStatus(Long adId, String newStatus, String username, String token) throws Exception {
        ChangeAdStatusRequest request = new ChangeAdStatusRequest(adId, newStatus, username, token);
        String json = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest

                = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/manager/change-ad-status"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("خطا در تغییر وضعیت آگهی: " + response.statusCode());
        }
    }

    public static void removeAd(Long adId, String username, String token) throws Exception {
        RemoveAdRequest request = new RemoveAdRequest(adId, username, token);
        String json = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/manager/remove-ad"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("خطا در حذف آگهی: " + response.statusCode());
        }
    }
    public static ArrayList<Advertisement> sendGetApprovedAdsRequest() throws Exception {
        GetApprovedAdsRequest request = new GetApprovedAdsRequest(
                SessionManager.getCurrentUser().getUsername(),
                SessionManager.getToken()
        );

        String json = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/public/get-approved-ads"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            return objectMapper.readValue(httpResponse.body(), new TypeReference<ArrayList<Advertisement>>() {});
        } else {
            throw new Exception("get approved ads failed: " + httpResponse.statusCode());
        }
    }
    public static ArrayList<Advertisement> sendGetMyAdsRequest(String username, String token) throws Exception {
        GetMyAdsRequest request = new GetMyAdsRequest(username, token);
        String json = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/user/my-ads"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<ArrayList<Advertisement>>() {});
        } else {
            throw new Exception("خطا در دریافت آگهی‌های کاربر: " + response.statusCode());
        }
    }

}
