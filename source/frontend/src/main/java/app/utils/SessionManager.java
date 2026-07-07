package app.utils;

import app.models.User;

public class SessionManager {
    private static User currentUser;
    private static String token;

    public static void setToken(String token) {
        SessionManager.token = token;
    }

    public static void setCurrentUser(User currentUser) {
        SessionManager.currentUser = currentUser;
    }

    public String getToken() {
        return token;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
