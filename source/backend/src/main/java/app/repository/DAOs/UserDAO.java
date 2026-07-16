package app.repository.DAOs;

import app.entities.users.*;
import app.entities.users.enums.UserRole;
import app.repository.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO {
    public static app.repository.enums.UserDAO saveUser(User user) {
        if (isUsernameExist(user.getUsername())) {
            return app.repository.enums.UserDAO.USERNAME_ALREADY_EXISTS;
        }
        if (isPhoneNumberExist(user.getPhoneNumber())) {
            return app.repository.enums.UserDAO.PHONE_NUMBER_ALREADY_EXISTS;
        }
        String sqlOrder = "INSERT INTO users(username,password,phone_number,user_role, is_blocked,fullname) VALUES (?,?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sqlOrder)) {
            s.setString(1, user.getUsername());
            s.setString(2, user.getPassword());
            s.setString(3, user.getPhoneNumber());
            s.setString(4, user.getUserRole().name());
            s.setInt(5, user.isBlocked() ? 1 : 0);
            s.setString(6, user.getFullname());
            int e = s.executeUpdate();
            if (e == 0) {
                throw new SQLException("saving user failed");
            }
            try (Statement st = c.createStatement();
                    ResultSet res = st.executeQuery("SELECT last_insert_rowid()")) {
                if (res.next()) {
                    Long generatedId = res.getLong(1);
                    user.setId(generatedId);
                    String createdAt = getUserCreatedAt(user.getId());
                    user.setCreatedDate(createdAt);
                    return app.repository.enums.UserDAO.USER_SAVED_SUCCESSFULLY;
                } else {
                    throw new SQLException("user id not created");
                }
            }
        } catch (Exception e) {
            System.err.println("error in saving user: " + e.getMessage());
            return app.repository.enums.UserDAO.UNEXPECTED_ERROR;
        }
    }

    public static String getUserCreatedAt(Long userId) {
        String sqlOrder = "SELECT created_at FROM users WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement s = c.prepareStatement(sqlOrder)) {
            s.setLong(1, userId);
            ResultSet res = s.executeQuery();
            if (res.next()) {
                return res.getString("created_at");
            }
        } catch (Exception e) {
            System.err.println("error in finding user's created date" + e.getMessage());
        }
        return null;
    }

    public static ArrayList<User> getAllUsers() throws Exception {
        String sqlQuery = "SELECT * FROM users";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement s = c.prepareStatement(sqlQuery)) {
            ResultSet res = s.executeQuery();
            ArrayList<User> users = new ArrayList<User>();
            while (res.next()) {
                User user = resultSetTOUser(res);
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            throw new Exception("error in get all users");
        }
    }

    private static User resultSetTOUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUsername(rs.getString("username"));
        u.setBlocked(rs.getBoolean("is_blocked"));
        u.setFullname(rs.getString("fullname"));
        u.setPhoneNumber(rs.getString("phone_number"));
        u.setId(rs.getLong("id"));
        u.setCreatedDate(rs.getString("created_at"));
        u.setPassword(rs.getString("password"));
        u.setUserRole(rs.getString("user_role").equals("MANAGER") ? UserRole.MANAGER : UserRole.COMMON_USER);
        return u;
    }

    public static boolean isUsernameExist(String username) {
        String sqlQuery = "Select username FROM users WHERE username = ?";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement s = c.prepareStatement(sqlQuery)) {
            s.setString(1, username);
            ResultSet res = s.executeQuery();
            if (res.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("error in get username (isUsernameExist):" + e.getMessage());
            return false;
        }
    }

    public static boolean isPhoneNumberExist(String phoneNumber) {
        String sqlQuery = "SELECT phone_number FROM users WHERE phone_number = ?";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement s = c.prepareStatement(sqlQuery)) {
            s.setString(1, phoneNumber);
            ResultSet res = s.executeQuery();
            if (res.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("error in get phone number (isPhoneNumberExist):" + e.getMessage());
            return false;
        }
    }

    public static boolean isIdExist(Long id) {
        String sqlQuery = "Select username FROM users WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement s = c.prepareStatement(sqlQuery)) {
            s.setLong(1, id);
            ResultSet res = s.executeQuery();
            if (res.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("error in get id (isIdExist):" + e.getMessage());
            return false;
        }
    }

    public static boolean checkPasswordByUsername(String username, String password) {
        String sqlQuery = "SELECT password FROM users WHERE username=?";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement s = c.prepareStatement(sqlQuery)) {
            s.setString(1, username);
            ResultSet res = s.executeQuery();
            if (res.next()) {
                return password.equals(res.getString("password"));
            }
            return false;
        } catch (Exception e) {
            System.err.println("error in check password" + e.getMessage());
            return false;
        }
    }

    public static User loadUserByUsername(String username) {
        if (!isUsernameExist(username)) {
            return null;
        }
        String sqlQuery = "SELECT * FROM users WHERE username = ?";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement s = c.prepareStatement(sqlQuery)) {
            s.setString(1, username);
            ResultSet res = s.executeQuery();
            if (res.next()) {
                if (res.getString("user_role").equals("MANAGER")) {
                    return new Manager(res.getLong("id"), username, res.getString("password"),
                            res.getString("phone_number"), res.getString("fullname"));
                } else {
                    return new CommonUser(res.getLong("id"), username, res.getString("password"),
                            res.getString("phone_number"), res.getString("fullname"));
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("error in get user from database find by username" + e.getMessage());
            return null;
        }
    }

    public static User loadUserByPhoneNumber(String phoneNumber) {
        if (!isPhoneNumberExist(phoneNumber)) {
            return null;
        }
        String sqlQuery = "SELECT * FROM users WHERE phone_number = ?";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement s = c.prepareStatement(sqlQuery)) {
            s.setString(1, phoneNumber);
            ResultSet res = s.executeQuery();
            if (res.next()) {
                if (res.getString("user_role").equals("MANAGER")) {
                    return new Manager(res.getLong("id"), res.getString("username"), res.getString("password"),
                            phoneNumber, res.getString("fullname"));
                } else {
                    return new CommonUser(res.getLong("id"), res.getString("username"), res.getString("password"),
                            phoneNumber, res.getString("fullname"));
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("error in get user from database find by phone number" + e.getMessage());
            return null;
        }
    }

    public static User loadUserById(Long id) {
        if (!isIdExist(id)) {
            return null;
        }
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement s = c.prepareStatement(sqlQuery)) {
            s.setLong(1, id);
            ResultSet res = s.executeQuery();
            if (res.next()) {
                if (res.getString("user_role").equals("MANAGER")) {
                    return new Manager(id, res.getString("username"), res.getString("password"),
                            res.getString("phone_number"), res.getString("fullname"));
                } else {
                    return new CommonUser(id, res.getString("username"), res.getString("password"),
                            res.getString("phone_number"), res.getString("fullname"));
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("error in get user from database find by id" + e.getMessage());
            return null;
        }
    }

    public static app.repository.enums.UserDAO editUser(User u) {
        String sqlQuery = "UPDATE users SET password = ? ,is_blocked = ? ,fullname = ? WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement s = c.prepareStatement(sqlQuery)) {
            s.setString(1, u.getPassword());
            System.out.println(u.getPassword());
            s.setInt(2, u.isBlocked() ? 1 : 0);
            System.out.println(u.isBlocked());
            s.setString(3, u.getFullname());
            System.out.println(u.getFullname());
            s.setLong(4, u.getId());
            System.out.println(u.getId());
            int e = s.executeUpdate();
            if (e == 0) {
                throw new Exception("cant edit");
            } else {
                return app.repository.enums.UserDAO.USER_EDIT_SUCCESSFULLY;
            }
        } catch (Exception e) {
            System.err.println("error in updating user :" + e.getMessage());
            return app.repository.enums.UserDAO.UNEXPECTED_ERROR;
        }
    }

    public static app.repository.enums.UserDAO deleteUser(String username) {
        if (!isUsernameExist(username)) {
            return app.repository.enums.UserDAO.USERNAME_NOT_FOUND;
        }
        String sqlQuery = "DELETE FROM users WHERE username=?";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement s = c.prepareStatement(sqlQuery)) {
            s.setString(1, username);
            int e = s.executeUpdate();
            if (e == 0) {
                return app.repository.enums.UserDAO.UNEXPECTED_ERROR;
            }
            return app.repository.enums.UserDAO.USER_DELETE_SUCCESSFULLY;
        } catch (Exception e) {
            System.err.println("error in delete user :" + e.getMessage());
            return app.repository.enums.UserDAO.UNEXPECTED_ERROR;
        }
    }
}
