package app.entities.users;

import app.entities.users.enums.*;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MANAGER")
public class Manager extends User {


    public Manager() {
        super();
    }


    public Manager(String username, String password, String phoneNumber, String fullname) {
        super(username, password, phoneNumber, UserRole.MANAGER, fullname);
    }


    public Manager(int id, String username, String password, String phoneNumber, String fullname) {
        super(username, password, phoneNumber, UserRole.MANAGER, fullname);
        this.setId(id);
    }
}