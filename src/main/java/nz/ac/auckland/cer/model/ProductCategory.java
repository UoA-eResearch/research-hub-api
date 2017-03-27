package nz.ac.auckland.cer.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class ProductCategory extends OneToManyCategory {

    @OneToMany(mappedBy="productCategory")
    private Set<Product> products;

    public ProductCategory() {
        super();
    }

    public ProductCategory(String name) {
        super(name);
    }
}
