package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.Product;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;


@Entity
public class Provider extends OneToManyCategory {

    @OneToMany(mappedBy="provider")
    private Set<Product> products;

    public Provider() {
        super();
    }

    public Provider(String name) {
        super(name);
    }
}
