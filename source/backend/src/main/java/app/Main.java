package app;

import app.entities.users.User;
import app.entities.users.enums.UserRole;
import app.repository.DAOs.UserDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import app.repository.DatabaseInitializer;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initDatabase();
        SpringApplication.run(Main.class, args);
        UserDAO.saveUser(new User("admin","admin","000", UserRole.MANAGER,"admin"));
    }
}
