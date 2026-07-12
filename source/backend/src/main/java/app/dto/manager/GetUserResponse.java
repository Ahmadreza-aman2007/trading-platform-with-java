package app.dto.manager;

public class GetUserResponse {
    private int id;
    private String username;
    private String phoneNumber;

    private boolean isBlocked;
    private String fullname;
    private String createdDate;

    public GetUserResponse(int id, String username, String phoneNumber, String fullname, boolean isBlocked, String createdDate) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.isBlocked = isBlocked;
        this.fullname = fullname;
        this.createdDate = createdDate;
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
