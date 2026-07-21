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
import java.util.List;

public class AdService {

    private static HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private static ObjectMapper objectMapper = new ObjectMapper();


    public static List<Advertisement> sendAdvancedSearchRequest(
            String keyword, String category, String city, Long minPrice, Long maxPrice) throws Exception {
        app.models.requests.shared.CustomSearchRequest customSearchRequest = new app.models.requests.shared.CustomSearchRequest();
        customSearchRequest.setKeyword(keyword);
        customSearchRequest.setCategory(category.equals("همه دسته‌بندی‌ها")?null:category);
        customSearchRequest.setCity(city.equals("همه شهرها")?null:city);
        customSearchRequest.setPriceCeiling(maxPrice);
        customSearchRequest.setPriceFloor(minPrice);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/user/advanced-search"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(customSearchRequest)))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Advertisement>>() {});
        } else {
            throw new Exception("خطا در جستجو: " + response.statusCode() + " - " + response.body());
        }
    }
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
            return;
        }
        throw new Exception("remove ad failed");
    }
    public static void sendMarkSoldRequest(Long id) throws Exception{
        RemoveAdRequest request = new RemoveAdRequest(id,SessionManager.getCurrentUser().getUsername(),SessionManager.getToken());
        String json = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/user/mark-sold"))
                .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10)).build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if(httpResponse.statusCode()==200){
            System.out.println("successful");
            return;
        }
        throw new Exception("mark sold failed");
    }
    public static void sendEditAdRequest(Long id,String username,String token,String title,String description,long price,String city,String category) throws Exception{
        sendEditAdRequest(id, username, token, title, description, price, city, category, null);
    }

    public static void sendEditAdRequest(Long id,String username,String token,String title,String description,long price,String city,String category,List<String> images) throws Exception{
        EditAdRequest request = new EditAdRequest(id,token,title,description,price,username,city,category);
        request.setImages(images);

        String json = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/user/edit-ad"))
                .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10)).build();
        HttpResponse<String> httpResponse=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
        if(httpResponse.statusCode()>=200&&httpResponse.statusCode()<300){
            System.out.println("successful");
            return;
        }
        throw new Exception("ویرایش آگهی ناموفق (کد " + httpResponse.statusCode() + "): " + httpResponse.body());
    }

    public static void sendAddAdRequest(String title, String description, long price, String sellerUsername, String city, String category) throws Exception{
        sendAddAdRequest(title, description, price, sellerUsername, city, category, null);
    }

    public static void sendAddAdRequest(String title, String description, long price, String sellerUsername, String city, String category, List<String> images) throws Exception{
        AddAdRequest addAdRequest=new AddAdRequest(SessionManager.getToken(),title,description,price,sellerUsername,city,category);
        addAdRequest.setImages(images);
        String json = objectMapper.writeValueAsString(addAdRequest);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/user/add-ad"))
                .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10)).build();

        HttpResponse<String> httpResponse=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
        if(httpResponse.statusCode()==200){
            System.out.println("successful");
            return;
        }
        throw new Exception("ثبت آگهی ناموفق (کد " + httpResponse.statusCode() + "): " + httpResponse.body());
    }

    // دریافت عکس‌های آگهی (Base64) از سرور
    public static ArrayList<String> getAdImages(Long adId) throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/public/ad-images/" + adId))
                .GET().timeout(Duration.ofSeconds(10)).build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<ArrayList<String>>() {});
        }
        throw new Exception("خطا در دریافت عکس‌ها");
    }

    public static void changeAdStatus(Long adId, String newStatus, String username, String token) throws Exception {
        changeAdStatus(adId, newStatus, null, username, token);
    }

    public static void changeAdStatus(Long adId, String newStatus, String note, String username, String token) throws Exception {
        ChangeAdStatusRequest request = new ChangeAdStatusRequest(adId, newStatus, note, username, token);
        String json = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/manager/change-ad-status"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() <200 || response.statusCode()>=300) {
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
            System.err.println(response.statusCode());
            throw new Exception("خطا در حذف آگهی: " + response.statusCode());
        }
    }
    public static ArrayList<Advertisement> sendGetApprovedAdsRequest() throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/public/get-approved-ads"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
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
