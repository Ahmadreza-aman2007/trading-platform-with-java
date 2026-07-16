
package app.repository.DAOs;

import app.entities.Advertisement;
import app.entities.users.enums.AdStatus;
import app.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdvertisementDAO {


    public boolean insertAdvertisement(Advertisement ad) {
        String sql = "INSERT INTO advertisements (title, description, price, seller_username, city, category, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ad.getTitle());
            pstmt.setString(2, ad.getDescription());
            pstmt.setLong(3, ad.getPrice());
            pstmt.setString(4, ad.getSellerUsername());
            pstmt.setString(5, ad.getCity());
            pstmt.setString(6, ad.getCategory());
            pstmt.setString(7, ad.getStatus().name());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting advertisement: " + e.getMessage());
            return false;
        }
    }


    public List<Advertisement> getApprovedAdvertisements() {
        List<Advertisement> ads = new ArrayList<>();
        String sql = "SELECT * FROM advertisements WHERE status = 'APPROVED' ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Advertisement ad = new Advertisement(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getLong("price"),
                        rs.getString("seller_username"),
                        rs.getString("city"),
                        rs.getString("category"),
                        AdStatus.valueOf(rs.getString("status")),
                        rs.getString("created_at")
                );
                ads.add(ad);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching approved advertisements: " + e.getMessage());
        }
        return ads;
    }


    public boolean updateAdvertisementStatus(int adId, AdStatus newStatus) {
        String sql = "UPDATE advertisements SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus.name());
            pstmt.setInt(2, adId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating ad status: " + e.getMessage());
            return false;
        }
    }
}