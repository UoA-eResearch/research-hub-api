package nz.ac.auckland.cer.model;

import javax.persistence.*;
import java.util.Set;


@Entity
public class ProviderCategory extends OneToManyCategory {

    @OneToMany(mappedBy="providerCategory")
    private Set<Product> products;

    public ProviderCategory() {
        super();
    }

    public ProviderCategory(String name) {
        super(name);
    }
}
