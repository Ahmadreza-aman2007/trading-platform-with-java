
package app.repository.DAOs;

import app.dto.user.CustomSearchRequest;
import app.entities.Advertisement;
import app.entities.users.enums.AdStatus;
import app.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdvertisementDAO {


    public static boolean insertAdvertisement(Advertisement ad) {
        String sql = "INSERT INTO advertisements (title, description, price, seller_username, city, category, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ad.getTitle());
            pstmt.setString(2, ad.getDescription());
            pstmt.setLong(3, ad.getPrice());
            pstmt.setString(4, ad.getSellerUsername());
            pstmt.setString(5, ad.getCity());
            pstmt.setString(6, ad.getCategory());
            pstmt.setString(7, ad.getStatus());


            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting advertisement: " + e.getMessage());
            return false;
        }
    }

    // درج آگهی و برگرداندن id ساخته‌شده (برای ذخیره عکس‌ها)
    public static Long insertAdvertisementAndGetId(Advertisement ad) throws Exception {
        String sql = "INSERT INTO advertisements (title, description, price, seller_username, city, category, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, ad.getTitle());
                pstmt.setString(2, ad.getDescription());
                pstmt.setLong(3, ad.getPrice());
                pstmt.setString(4, ad.getSellerUsername());
                pstmt.setString(5, ad.getCity());
                pstmt.setString(6, ad.getCategory());
                pstmt.setString(7, ad.getStatus());
                pstmt.executeUpdate();
            }
            // درایور SQLite از getGeneratedKeys پشتیبانی نمی‌کند؛ از تابع خود SQLite روی همان اتصال استفاده می‌کنیم
            try (java.sql.Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) return rs.getLong(1);
            }
            throw new Exception("error in insertAdvertisementAndGetId");
        }
    }

    public static void removeAdvertisement(Long id) throws Exception {
        try { AdImageDAO.deleteByAdId(id); } catch (Exception ignored) { /* عکس‌ها اختیاری‌اند */ }
        String sql = "DELETE FROM advertisements WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1, id);
            int e=pstmt.executeUpdate();
            if(e==0){
                throw new Exception("advertisement does not exist");
            }
        }
    }

    public static ArrayList<Advertisement> advancedSearch(CustomSearchRequest customSearchRequest) throws Exception {
        ArrayList<Advertisement> advertisements = getApprovedAdvertisements();
        ArrayList<Advertisement> results = new ArrayList<>();
        for (Advertisement advertisement : advertisements) {
            boolean matchCategory = customSearchRequest.getCategory()==null || advertisement.getCategory().equals(customSearchRequest.getCategory());
            boolean matchCity = customSearchRequest.getCity()==null || advertisement.getCity().equals(customSearchRequest.getCity());
            boolean matchMaxPrice = customSearchRequest.getPriceCeiling()==null || advertisement.getPrice() <= customSearchRequest.getPriceCeiling();
            boolean matchMinPrice = customSearchRequest.getPriceFloor()==null || advertisement.getPrice() >= customSearchRequest.getPriceFloor();
            String keyword = customSearchRequest.getKeyword();
            boolean matchKeyword = keyword==null || keyword.isBlank()
                    || advertisement.getTitle().contains(keyword)
                    || (advertisement.getDescription()!=null && advertisement.getDescription().contains(keyword));
            if(matchCategory && matchCity && matchMaxPrice && matchMinPrice && matchKeyword){
                results.add(advertisement);
            }
        }
        return results;
    }
    public static ArrayList<Advertisement> getApprovedAdvertisements() {
        ArrayList<Advertisement> ads = new ArrayList<>();
        String sql = "SELECT * FROM advertisements WHERE status = 'APPROVED' ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Advertisement ad = new Advertisement(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getLong("price"),
                        rs.getString("seller_username"),
                        rs.getString("city"),
                        rs.getString("category"),
                        rs.getString("status"),
                        rs.getString("created_at")
                );
                ad.setRejectNote(rs.getString("reject_note"));
                ads.add(ad);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching approved advertisements: " + e.getMessage());
        }
        return ads;
    }

    public static void update(Advertisement ad) throws Exception {
        String query="UPDATE advertisements SET title=? ,description=?,price=?,city=?,category=?,status='PENDING' WHERE id = ?";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement pstmt=c.prepareStatement(query);){
            pstmt.setString(1, ad.getTitle());
            pstmt.setString(2, ad.getDescription());
            pstmt.setLong(3, ad.getPrice());
            pstmt.setString(4, ad.getCity());
            pstmt.setString(5, ad.getCategory());
            pstmt.setLong(6, ad.getId());
            int e=pstmt.executeUpdate();
            if (e==0){
                throw new Exception("Error updating advertisement");
            }
        }
    }
    public static boolean updateAdvertisementStatus(int adId,String newStatus) {
        String sql = "UPDATE advertisements SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, adId);
            System.out.println(newStatus);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating ad status: " + e.getMessage());
            return false;
        }
    }
    // تغییر وضعیت به همراه ثبت توضیح مدیر (برای رد آگهی)
    public static boolean updateAdvertisementStatus(int adId, String newStatus, String note) {
        String sql = "UPDATE advertisements SET status = ?, reject_note = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, note);
            pstmt.setInt(3, adId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating ad status with note: " + e.getMessage());
            return false;
        }
    }
    public static ArrayList<Advertisement> getPendingAdvertisements() throws Exception {
        ArrayList<Advertisement> results = new ArrayList<>();
        String sql = "SELECT * FROM advertisements WHERE status = 'PENDING' ORDER BY created_at DESC";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement pstmt=c.prepareStatement(sql);){
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(toAd(rs));
            }
            return results;
        }

    }
    public static Advertisement toAd(ResultSet rs) throws Exception {
        Advertisement advertisement=new Advertisement(rs.getLong("id"),rs.getString("title"),rs.getString("description"),rs.getLong("price"),rs.getString("seller_username"),rs.getString("city"),rs.getString("category") ,rs.getString("status"),rs.getString("created_at"));
        advertisement.setRejectNote(rs.getString("reject_note"));
        return advertisement;
    }
    public static  Advertisement getAdvertisementById(long id) throws Exception {
        String sql = "SELECT * FROM advertisements WHERE id = ?";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement pstmt=c.prepareStatement(sql);){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return toAd(rs);
            }
            throw new Exception("No advertisement with id " + id);
        }
    }
    public static ArrayList<Advertisement> getAdsByUsername(String username) throws Exception {
        ArrayList<Advertisement> result = new ArrayList<>();
        String sql = "SELECT * FROM advertisements WHERE seller_username = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(toAd(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new Exception("خطا در دریافت آگهی‌های کاربر: " + e.getMessage());
        }
    }
}