package app;

import app.entities.users.Manager;
import app.repository.DAOs.UserDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import app.repository.DatabaseInitializer;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initDatabase();
        SpringApplication.run(Main.class, args);
        UserDAO.saveUser(new Manager("admin","admin","000","admin"));
    }
}
