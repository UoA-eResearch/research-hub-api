package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;


@Entity
public class ServiceType extends ManyToManyCategory {

    @ManyToMany(mappedBy="serviceTypes")
    private Set<Product> products;

    public ServiceType() {
        super();
    }

    public ServiceType(int id) {
        super(id);
    }

    public ServiceType(String name) {
        super(name);
    }
}
