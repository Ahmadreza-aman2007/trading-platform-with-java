package app.services;

import app.dto.manager.ChangeAdStatusRequest;
import app.dto.user.AddAdRequest;
import app.dto.user.EditAdRequest;
import app.dto.user.RemoveAdRequset;
import app.entities.Advertisement;
import app.entities.users.enums.AdStatus;
import app.entities.users.enums.UserRole;
import app.repository.DAOs.AdvertisementDAO;
import app.utils.TokenUtil;

import java.util.List;

public class AdvertisementService {


    public static void removeAdvertisement(RemoveAdRequset removeAdRequset) throws Exception {
        TokenUtil.isTokenValid(removeAdRequset.getUsername(), removeAdRequset.getToken(), UserRole.COMMON_USER);
    }
    public static void addAdvertisement(AddAdRequest addAdRequest) throws Exception {
        TokenUtil.isTokenValid(addAdRequest.getSellerUsername(), addAdRequest.getToken(), UserRole.COMMON_USER);
        Advertisement advertisement=new Advertisement(addAdRequest.getTitle(),addAdRequest.getDescription(),addAdRequest.getPrice(),addAdRequest.getSellerUsername(),addAdRequest.getCity(),addAdRequest.getCategory());
        AdvertisementDAO.insertAdvertisement(advertisement);
    }
    public static void editAdvertisement(EditAdRequest editAdRequest) throws Exception {
        TokenUtil.isTokenValid(editAdRequest.getSellerUsername(), editAdRequest.getToken(), UserRole.COMMON_USER);
        Advertisement advertisement=new Advertisement(editAdRequest.getId(), editAdRequest.getTitle(), editAdRequest.getDescription(), editAdRequest.getPrice(), editAdRequest.getSellerUsername(), editAdRequest.getCity(), editAdRequest.getCategory());
        AdvertisementDAO.update(advertisement);
    }
    
    public static String publishAd(String title, String description, long price, String username, String city, String category) {
        if (title == null || title.isBlank()) return "Title cannot be empty!";
        if (price <= 0) return "Price must be greater than zero!";

        Advertisement ad = new Advertisement(title, description, price, username, city, category);
        boolean success = AdvertisementDAO.insertAdvertisement(ad);

        return success ? "Ad submitted successfully and is pending approval." : "Failed to submit ad.";
    }


    public List<Advertisement> getApprovedAds() {
        return AdvertisementDAO.getApprovedAdvertisements();
    }


    public static void changeAdStatus(ChangeAdStatusRequest changeAdStatusRequest) throws Exception {
        TokenUtil.isTokenValid(changeAdStatusRequest.getUsername(), changeAdStatusRequest.getToken(),UserRole.MANAGER);
        AdvertisementDAO.updateAdvertisementStatus(changeAdStatusRequest.getAdId(), changeAdStatusRequest.getStatus());
    }
}