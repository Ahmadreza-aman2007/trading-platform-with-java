package app.repository.DAOs;

import app.entities.categories.City;
import app.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CityDAO {
    public static void save(City city) throws Exception{
        if(isCityExist(city.getName())){
            throw new Exception("City already exists");
        }
        String query = "INSERT INTO cities (city) VALUES (?)";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);){
            statement.setString(1, city.getName());
            int e=statement.executeUpdate();
            if(e==0){
                throw new Exception("error in method save");
            }
        }
    }
    public static City findById(Long id) throws Exception{
        String query = "SELECT * FROM cities WHERE id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                City city=new City();
                city.setName(rs.getString("city"));
                city.setId(rs.getLong("id"));
                return city;
            }
            throw new Exception("error in method findById");
        }
    }
    public static boolean isCityExist(String name) throws Exception{
        String query = "SELECT id FROM cities WHERE city = ?";
        try(Connection c= DatabaseConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(query);){
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
    public static ArrayList<City> getAllCities() throws Exception{
        ArrayList<City> cities = new ArrayList<>();
        String query = "SELECT * FROM cities";
        try(Connection c= DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement(query);)
        {
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                City city=new City();
                city.setName(rs.getString("city"));
                city.setId(rs.getLong("id"));
                cities.add(city);
            }
            return cities;
        }
    }
}
