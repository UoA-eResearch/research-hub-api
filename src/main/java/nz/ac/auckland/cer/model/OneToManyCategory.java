package nz.ac.auckland.cer.model;


import nz.ac.auckland.cer.model.Product;

import javax.persistence.*;
import java.util.Set;

@MappedSuperclass
public abstract class OneToManyCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    @OneToMany
    private Set<Product> products;

    public OneToManyCategory(){

    }

    public OneToManyCategory(String name) {
        this.name = name;
    }

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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        String result = String.format(
                "OneToManyCategory[id=%d, name='%s']%n",
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
