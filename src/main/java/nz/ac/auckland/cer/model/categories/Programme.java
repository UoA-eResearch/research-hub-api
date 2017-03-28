package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.Product;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;


@Entity
public class Programme extends ManyToManyCategory {

    @ManyToMany(mappedBy="programme")
    private Set<Product> products;

    public Programme() {
        super();
    }

    public Programme(String name) {
        super(name);
    }
}
