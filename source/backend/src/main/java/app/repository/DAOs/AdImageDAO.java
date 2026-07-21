package app.repository.DAOs;

import app.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// ذخیره و خواندن عکس های آگهی به صورت Base64 در جدول ad_images
public class AdImageDAO {

    // اطمینان از وجود جدول (برای دیتابیس‌های قدیمی که قبل از این قابلیت ساخته شده‌اند)
    private static void ensureTable() throws Exception {
        try (Connection c = DatabaseConnection.getConnection();
             java.sql.Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS ad_images (id INTEGER PRIMARY KEY AUTOINCREMENT, ad_id INTEGER NOT NULL, image TEXT NOT NULL, FOREIGN KEY (ad_id) REFERENCES advertisements(id))");
        }
    }

    // ذخیره عکس‌های یک آگهی (رشته Base64)
    public static void saveImages(Long adId, List<String> images) throws Exception {
        if (images == null || images.isEmpty()) return;
        ensureTable();
        String sql = "INSERT INTO ad_images (ad_id, image) VALUES (?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (String image : images) {
                if (image == null || image.isBlank()) continue;
                ps.setLong(1, adId);
                ps.setString(2, image);
                ps.executeUpdate();
            }
        }
    }

    public static ArrayList<String> getImagesByAdId(Long adId) throws Exception {
        ArrayList<String> images = new ArrayList<>();
        ensureTable();
        String sql = "SELECT image FROM ad_images WHERE ad_id = ? ORDER BY id";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, adId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                images.add(rs.getString("image"));
            }
        }
        return images;
    }

    public static void deleteByAdId(Long adId) throws Exception {
        ensureTable();
        String sql = "DELETE FROM ad_images WHERE ad_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, adId);
            ps.executeUpdate();
        }
    }
}
