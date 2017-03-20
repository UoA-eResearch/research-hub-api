package nz.ac.auckland.cer.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "product_category")
public class ProductCategory {
    private int id;
    private String name;
    private Set<Product> products;

    public ProductCategory(){

    }

    public ProductCategory(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL)
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        String result = String.format(
                "Category[id=%d, name='%s']%n",
                id, name);
        if (products != null) {
            for(Product product : products) {
                result += String.format(
                        "Product[id=%d, name='%s']%n",
                        product.getId(), product.getName());
            }
        }

        return result;
    }
}
