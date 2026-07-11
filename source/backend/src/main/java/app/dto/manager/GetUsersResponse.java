package app.dto.manager;

import app.entities.users.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class GetUsersResponse {
    private int id;
    private String username;
    private String phoneNumber;

    private boolean isBlocked;
    private String fullname;
    private String createdDate;

    public GetUsersResponse(int id,String username,  String phoneNumber, String fullname,boolean isBlocked,String createdDate) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.isBlocked = isBlocked;
        this.fullname = fullname;
        this.createdDate = createdDate;
        this.id = id;
    }
}
