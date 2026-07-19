package app.repository.DAOs;

import app.entities.categories.ProductCategory;
import app.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class CategoryDAO {
    public static ProductCategory findById(Long id)throws Exception{
        String query = "SELECT * FROM product_categories WHERE id = ?";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(query)){
            s.setLong(1,id);
            ResultSet rs=s.executeQuery();
            if(rs.next()){
                ProductCategory p=new ProductCategory();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("product_category"));
                return p;
            }
            throw new Exception("error in method findById");
        }
    }
    public static void   save(ProductCategory productCategory) throws Exception{
        if(isCategoryExist(productCategory.getName())){
            throw new Exception("category already exists");
        }
        String query = "INSERT INTO product_categories (product_category) VALUES (?)";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(query)){
            s.setString(1, productCategory.getName());
            int e=s.executeUpdate();
            if(e!=1){
                throw new Exception("Error in method save");
            }
        }
    }
    public static boolean isCategoryExist(String name) throws Exception {
        String query="SELECT id FROM product_categories WHERE product_category=?";
        try(Connection c= DatabaseConnection.getConnection();
            PreparedStatement s=c.prepareStatement(query)){
            s.setString(1, name);
            ResultSet rs=s.executeQuery();
            return  rs.next();
        }
    }
    public static ArrayList<ProductCategory> getAllCategories() throws Exception{
        ArrayList<ProductCategory> categories=new ArrayList<>();
        String query="SELECT * FROM product_categories";
        try(Connection c=DatabaseConnection.getConnection();
        PreparedStatement s=c.prepareStatement(query)){
            ResultSet rs=s.executeQuery();
            while(rs.next()){
                ProductCategory p=new ProductCategory();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("product_category"));
                categories.add(p);
            }
            return categories;
        }
    }
}
