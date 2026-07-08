package app.entities.categories;

import jakarta.persistence.*;

@Entity
@Table(name = "product_categories")
public class ProductCategory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_category",nullable = false,unique = true)
    private String name;
    public ProductCategory(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
