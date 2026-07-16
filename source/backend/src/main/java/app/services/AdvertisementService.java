package app.services;

import app.entities.Advertisement;
import app.entities.users.enums.AdStatus;
import app.repository.DAOs.AdvertisementDAO;

import java.util.List;

public class AdvertisementService {
    private final AdvertisementDAO advertisementDAO;

    public AdvertisementService() {
        this.advertisementDAO = new AdvertisementDAO();
    }

    
    public String publishAd(String title, String description, long price, String username, String city, String category) {
        if (title == null || title.isBlank()) return "Title cannot be empty!";
        if (price <= 0) return "Price must be greater than zero!";

        Advertisement ad = new Advertisement(title, description, price, username, city, category);
        boolean success = advertisementDAO.insertAdvertisement(ad);

        return success ? "Ad submitted successfully and is pending approval." : "Failed to submit ad.";
    }


    public List<Advertisement> getApprovedAds() {
        return advertisementDAO.getApprovedAdvertisements();
    }


    public boolean changeAdStatus(int adId, AdStatus status) {
        return advertisementDAO.updateAdvertisementStatus(adId, status);
    }
}