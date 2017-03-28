package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.Product;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;


@Entity
public class Cost extends ManyToManyCategory {

    @ManyToMany(mappedBy="cost")
    private Set<Product> products;

    public Cost() {
        super();
    }

    public Cost(String name) {
        super(name);
    }
}
