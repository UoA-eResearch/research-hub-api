package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.ManyToManyCategory;
import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class LifeCycle extends ManyToManyCategory {

    @ManyToMany(mappedBy="lifeCycle")
    private Set<Product> products;

    public LifeCycle() {
        super();
    }

    public LifeCycle(String name) {
        super(name);
    }
}
