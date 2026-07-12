package app.utils;

import app.models.entities.User;

public class SessionManager {
    private static User currentUser;
    private static String token;

    public static void setToken(String token) {
        SessionManager.token = token;
    }

    public static void setCurrentUser(User currentUser) {
        SessionManager.currentUser = currentUser;
    }

    public static String getToken() {
        return token;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    public  static void logout(){
        currentUser = null;
        token = null;
    }
}
