
package app.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initDatabase() {
//      initialize query for users table
        String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE,
                    phone_number TEXT UNIQUE,
                    password TEXT,
                    user_role TEXT CHECK(user_role IN ('MANAGER', 'COMMON_USER')),
                    is_blocked INTEGER DEFAULT 0,
                    fullname TEXT,
                    created_at TEXT
                )
                """;
//        initialize query for tokens table
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
//        initialize query for cities table
        String cities = """
                CREATE TABLE IF NOT EXISTS cities (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    city TEXT UNIQUE NOT NULL
                )
                """;
// initialize query for categories table
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
        // initialize query for conversations table
        String conversations= """
                CREATE TABLE IF NOT EXISTS conversations (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ad_id INTEGER NOT NULL,
                    seller_id INTEGER NOT NULL,
                    buyer_id INTEGER NOT NULL,
                    is_bloked INTEGER NOT NULL,
                    FOREIGN KEY (seller_id) REFERENCES users(id),
                    FOREIGN KEY (buyer_id) REFERENCES users(id),
                    FOREIGN KEY (ad_id) REFERENCES advertisements(id)
                )
                """;
        // initialize query for messages table
        String messages= """
                CREATE TABLE IF NOT EXISTS messages (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    content TEXT NOT NULL,
                    time DATETIME NOT NULL,
                    sender_id INTEGER NOT NULL,
                    conversation_id INTEGER NOT NULL,
                    is_read INTEGER NOT NULL,
                    FOREIGN KEY (sender_id) REFERENCES users(id),
                    FOREIGN KEY (conversation_id) REFERENCES convasations(id)
                    )
                """;
        // initialize query for favorites table
        String favorites= """
                CREATE TABLE IF NOT EXISTS favorites (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                ad_id INTEGER NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (ad_id) REFERENCES advertisements(id)
                )
                """;
        //initialize query for ratings table
        String ratings= """
                CREATE TABLE IF NOT EXISTS ratings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                rater_id INTEGER NOT NULL,
                seller_id INTEGER NOT NULL,
                ad_id INTEGER NOT NULL,
                score INTEGER NOT NULL,
                comment TEXT NOT NULL,
                created_at DATETIME NOT NULL,
                FOREIGN KEY (seller_id) REFERENCES users(id),
                FOREIGN KEY (ad_id) REFERENCES advertisements(id),
                FOREIGN KEY (seller_id) REFERENCES users(id)
                )
                """;

        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement()) {
            s.execute(createUsers);//build users table
            s.execute(tokens);//build tokens table
            s.execute(cities);//build cities table
            s.execute(productCategory);//build categories table
            s.execute(advertisements); // اجرای ساخت جدول آگهی
            s.execute(conversations);//build comments table
            s.execute(messages);//build messages table
            s.execute(favorites);//build favorites table
            s.execute(ratings);//build ratings table

            System.out.println("Database is ready with Advertisements table");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}