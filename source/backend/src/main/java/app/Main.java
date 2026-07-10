package app;

import app.entities.users.Manager;
import app.entities.users.User;
import app.entities.users.enums.UserRole;
import app.repository.UserDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import app.repository.DatabaseInitializer;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initDatabase();
        SpringApplication.run(Main.class, args);
    }
}
