package nz.ac.auckland.cer.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class LifeCycleCategory extends ManyToManyCategory {

    @ManyToMany(mappedBy="lifeCycleCategories")
    private Set<Product> products;

    public LifeCycleCategory() {
        super();
    }

    public LifeCycleCategory(String name) {
        super(name);
    }
}
