package app.models.entities;

public class ProductCategory
{

    private Long id;
    private String name;

    public ProductCategory() {}
    public ProductCategory(String name){
        this.name=name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
