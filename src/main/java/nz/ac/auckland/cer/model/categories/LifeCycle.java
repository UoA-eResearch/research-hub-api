package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class LifeCycle extends ManyToManyCategory {

    @ManyToMany(mappedBy="lifeCycleStages")
    private Set<Product> products;

    public LifeCycle() {
        super();
    }

    public LifeCycle(int id) {
        super(id);
    }

    public LifeCycle(String name) {
        super(name);
    }
}
