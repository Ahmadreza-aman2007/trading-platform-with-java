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
        try (Connection c = DatabaseConnection.getConnection();
                Statement s = c.createStatement()) {
            s.execute(createUsers);
            System.out.println("Database is ready");

        } catch (SQLException e) {
            System.err.println("Database error:" + e.getMessage());
        }
    }
}
