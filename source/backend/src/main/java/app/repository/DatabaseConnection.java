package app.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:database/secondhand.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    public static boolean testConnection (){
        try(Connection c=getConnection()){
            System.out.println("Communication was established");
            return true;
        }catch(SQLException e){
            System.err.println("Connection failed");
            return false;
        }
    }

}
