package app.entities.users;

import app.entities.users.enums.*;

public class CommonUser extends User {
    public CommonUser(String username, String password, String phoneNumber, String fullname) {
        super(username, password, phoneNumber, UserRole.COMMON_USER, fullname);
    }

    public CommonUser(int id, String username, String password, String phoneNumber, String fullname) {
        super(username, password, phoneNumber, UserRole.COMMON_USER, fullname);
    }
}
