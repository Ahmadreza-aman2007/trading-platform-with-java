package app.entities.users;

import app.entities.users.enums.*;

public class Manager extends User {
    public Manager(String username, String password, String phoneNumber, String fullname) {
        super(username, password, phoneNumber, UserRole.MANAGER, fullname);
    }

    public Manager(int id, String username, String password, String phoneNumber, String fullname) {
        super(username, password, phoneNumber, UserRole.MANAGER, fullname);
    }
}
