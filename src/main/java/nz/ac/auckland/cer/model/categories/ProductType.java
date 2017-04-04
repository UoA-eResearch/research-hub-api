package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class ProductType extends OneToManyCategory {

    @OneToMany(mappedBy="productType")
    private Set<Product> products;

    public ProductType() {
        super();
    }

    public ProductType(String name) {
        super(name);
    }
}
