package nz.ac.auckland.cer.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class LifecycleCategory extends ManyToManyCategory {

    @ManyToMany(mappedBy="lifecycleCategories")
    private Set<Product> products;

    public LifecycleCategory() {
        super();
    }

    public LifecycleCategory(String name) {
        super(name);
    }
}
