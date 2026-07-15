package app.entities.users;

import app.entities.users.enums.*;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COMMON_USER")
public class CommonUser extends User {


    public CommonUser() {
        super();
    }


    public CommonUser(String username, String password, String phoneNumber, String fullname) {
        super(username, password, phoneNumber, UserRole.COMMON_USER, fullname);
    }


    public CommonUser(int id, String username, String password, String phoneNumber, String fullname) {
        super(username, password, phoneNumber, UserRole.COMMON_USER, fullname);
        this.setId(id);
    }
}