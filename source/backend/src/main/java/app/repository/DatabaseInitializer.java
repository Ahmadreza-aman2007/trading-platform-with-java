
package app.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initDatabase() {
        String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    phone_number TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    user_role TEXT CHECK(user_role IN ('MANAGER', 'COMMON_USER')) NOT NULL,
                    is_blocked INTEGER DEFAULT 0,
                    fullname TEXT NOT NULL,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP NOT NULL
                )
                """;

        String tokens = """
                CREATE TABLE IF NOT EXISTS tokens (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    token TEXT NOT NULL,
                    username TEXT NOT NULL,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    expires_at DATETIME NOT NULL,
                    is_revoked INTEGER DEFAULT 0,
                    FOREIGN KEY (username) REFERENCES users(username)
                )
                """;

        String cities = """
                CREATE TABLE IF NOT EXISTS cities (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    city TEXT UNIQUE NOT NULL
                )
                """;

        String productCategory = """
                CREATE TABLE IF NOT EXISTS product_categories (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    product_category TEXT UNIQUE NOT NULL
                )
                """;

        // *** بخش جدید: اضافه شدن جدول آگهی‌ها ***
        String advertisements = """
                CREATE TABLE IF NOT EXISTS advertisements (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    description TEXT NOT NULL,
                    price REAL NOT NULL,
                    seller_username TEXT NOT NULL,
                    city TEXT NOT NULL,
                    category TEXT NOT NULL,
                    status TEXT CHECK(status IN ('PENDING', 'APPROVED', 'REJECTED')) DEFAULT 'PENDING',
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (seller_username) REFERENCES users(username),
                    FOREIGN KEY (city) REFERENCES cities(city),
                    FOREIGN KEY (category) REFERENCES product_categories(product_category)
                )
                """;

        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement()) {

            s.execute(createUsers);
            s.execute(tokens);
            s.execute(cities);
            s.execute(productCategory);
            s.execute(advertisements); // اجرای ساخت جدول آگهی

            System.out.println("Database is ready with Advertisements table");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}