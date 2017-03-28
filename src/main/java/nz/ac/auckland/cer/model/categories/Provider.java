package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.ManyToManyCategory;
import nz.ac.auckland.cer.model.Product;

import javax.persistence.*;
import java.util.Set;


@Entity
public class Provider extends ManyToManyCategory {

    @ManyToMany(mappedBy="provider")
    private Set<Product> products;

    public Provider() {
        super();
    }

    public Provider(String name) {
        super(name);
    }
}
