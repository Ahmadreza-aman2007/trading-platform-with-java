package app.services;

import app.entities.categories.ProductCategory;
import app.repository.DAOs.CategoryDAO;

import java.util.ArrayList;

public class CategoryService {
    public static ArrayList<ProductCategory> getCategories(){
        try {
            return CategoryDAO.getAllCategories();
        } catch (Exception e) {
            System.err.println("خطا در دریافت دسته‌بندی‌ها: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
