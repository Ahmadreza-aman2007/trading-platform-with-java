package app.services;

import app.dto.manager.ChangeAdStatusRequest;
import app.dto.manager.GetPendingAdsRequest;
import app.dto.user.AddAdRequest;
import app.dto.user.EditAdRequest;
import app.dto.user.RemoveAdRequset;
import app.entities.Advertisement;
import app.entities.users.enums.AdStatus;
import app.entities.users.enums.UserRole;
import app.entities.users.User;
import app.repository.DAOs.AdvertisementDAO;
import app.repository.DAOs.UserDAO;
import app.utils.TokenUtil;

import java.util.ArrayList;
import java.util.List;

// منطق اصلی آگهی ها: ثبت، ویرایش، حذف، جستجو و عکس ها
public class AdvertisementService {


    public static void removeAdvertisement(RemoveAdRequset removeAdRequset) throws Exception {
        TokenUtil.isTokenValid(removeAdRequset.getUsername(), removeAdRequset.getToken(), UserRole.All);
        User requester = UserDAO.loadUserByUsername(removeAdRequset.getUsername());
        if (requester.getUserRole() != UserRole.MANAGER) {
            Advertisement ad = AdvertisementDAO.getAdvertisementById(removeAdRequset.getAdId());
            if (!ad.getSellerUsername().equals(removeAdRequset.getUsername())) {
                throw new Exception("شما مالک این آگهی نیستید");
            }
        }
        AdvertisementDAO.removeAdvertisement(removeAdRequset.getAdId());
    }

    public static void markAsSold(RemoveAdRequset removeAdRequset) throws Exception {
        TokenUtil.isTokenValid(removeAdRequset.getUsername(), removeAdRequset.getToken(), UserRole.COMMON_USER);
        Advertisement ad = AdvertisementDAO.getAdvertisementById(removeAdRequset.getAdId());
        if (!ad.getSellerUsername().equals(removeAdRequset.getUsername())) {
            throw new Exception("شما مالک این آگهی نیستید");
        }
        AdvertisementDAO.updateAdvertisementStatus(removeAdRequset.getAdId().intValue(), AdStatus.SOLD.name());
    }
    public static void addAdvertisement(AddAdRequest addAdRequest) throws Exception {
        TokenUtil.isTokenValid(addAdRequest.getSellerUsername(), addAdRequest.getToken(), UserRole.COMMON_USER);
        Advertisement advertisement=new Advertisement(addAdRequest.getTitle(),addAdRequest.getDescription(),addAdRequest.getPrice(),addAdRequest.getSellerUsername(),addAdRequest.getCity(),addAdRequest.getCategory());
        Long adId = AdvertisementDAO.insertAdvertisementAndGetId(advertisement);
        try {
            app.repository.DAOs.AdImageDAO.saveImages(adId, addAdRequest.getImages());
        } catch (Exception e) {
            throw new Exception("آگهی ثبت شد اما ذخیره عکس‌ها ناموفق بود: " + e.getMessage());
        }
    }
    public static void editAdvertisement(EditAdRequest editAdRequest) throws Exception {
        TokenUtil.isTokenValid(editAdRequest.getSellerUsername(), editAdRequest.getToken(), UserRole.COMMON_USER);
        Advertisement current = AdvertisementDAO.getAdvertisementById(editAdRequest.getId());
        if (!current.getSellerUsername().equals(editAdRequest.getSellerUsername())) {
            throw new Exception("شما مالک این آگهی نیستید");
        }
        Advertisement advertisement=new Advertisement(editAdRequest.getId(), editAdRequest.getTitle(), editAdRequest.getDescription(), editAdRequest.getPrice(), editAdRequest.getSellerUsername(), editAdRequest.getCity(), editAdRequest.getCategory());
        AdvertisementDAO.update(advertisement);
        // اگر کاربر عکس جدید انتخاب کرده باشد، عکس‌های قبلی جایگزین می‌شوند
        if (editAdRequest.getImages() != null && !editAdRequest.getImages().isEmpty()) {
            app.repository.DAOs.AdImageDAO.deleteByAdId(editAdRequest.getId());
            app.repository.DAOs.AdImageDAO.saveImages(editAdRequest.getId(), editAdRequest.getImages());
        }
    }
    
    public static String publishAd(String title, String description, long price, String username, String city, String category) {
        if (title == null || title.isBlank()) return "Title cannot be empty!";
        if (price <= 0) return "Price must be greater than zero!";

        Advertisement ad = new Advertisement(title, description, price, username, city, category);
        boolean success = AdvertisementDAO.insertAdvertisement(ad);

        return success ? "Ad submitted successfully and is pending approval." : "Failed to submit ad.";
    }

    public static ArrayList<Advertisement> getPendingAdvertisements(GetPendingAdsRequest getPendingAdsRequest) throws Exception {
        TokenUtil.isTokenValid(getPendingAdsRequest.getUsername(),  getPendingAdsRequest.getToken(), UserRole.MANAGER);
        return AdvertisementDAO.getPendingAdvertisements();
    }
    public static ArrayList<Advertisement> getApprovedAds() {
        return AdvertisementDAO.getApprovedAdvertisements();
    }


    public static void changeAdStatus(ChangeAdStatusRequest changeAdStatusRequest) throws Exception {
        TokenUtil.isTokenValid(changeAdStatusRequest.getUsername(), changeAdStatusRequest.getToken(),UserRole.MANAGER);
        AdvertisementDAO.updateAdvertisementStatus(changeAdStatusRequest.getAdId(), changeAdStatusRequest.getStatus(), changeAdStatusRequest.getNote());
    }
}