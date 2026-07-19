package app.services;

import app.entities.categories.City;
import app.repository.DAOs.CityDAO;

import java.util.ArrayList;

public class CityService {
    public static ArrayList<City> getCities() throws Exception {
        return CityDAO.getAllCities();
    }
}
